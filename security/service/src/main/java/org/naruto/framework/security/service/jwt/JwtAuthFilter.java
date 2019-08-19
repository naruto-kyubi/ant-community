package org.naruto.framework.security.service.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.naruto.framework.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

@Component
@Slf4j
public class JwtAuthFilter extends AuthenticatingFilter {

    private static final int tokenRefreshInterval = 300;

    public JwtAuthFilter(){
        this.setLoginUrl("/v1/logon/account");
    }

    @Override
    public boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) //对于OPTION请求做拦截，不做token校验
            return false;

        return super.preHandle(request, response);
    }

    @Override
    public void postHandle(ServletRequest request, ServletResponse response){

        this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
        request.setAttribute("jwtShiroFilter.FILTERED", true);
    }

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断是否是登录的URL页面；
        if(this.isLoginRequest(request, response)) return true;

        Boolean afterFiltered = (Boolean)(request.getAttribute("jwtShiroFilter.FILTERED"));
        if( BooleanUtils.isTrue(afterFiltered)) return true;

        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch(IllegalStateException e){ //not found any token
            log.error("Not found any token");
        }catch (Exception e) {
            log.error("Error occurs when login", e);
        }

        boolean isRememberMe =false;
//        boolean isAnon = false;
        if(null!=mappedValue){
            String[] array = (String[]) mappedValue;
            isRememberMe = Arrays.asList(array).contains("RememberMe");
//            isAnon = Arrays.asList(array).contains("anon");
        }

        if(!allowed && isRememberMe) {
            //
            Subject subject = SecurityUtils.getSubject();
            allowed = null!= subject.getPrincipals();
        }

//        if(!allowed && isAnon)  allowed = true;
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    public AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String jwtToken = getAuthzHeader(servletRequest);
        if(StringUtils.isNotBlank(jwtToken)&&!JwtUtils.isTokenExpired(jwtToken))
            return new JWTToken(jwtToken);

        return null;
    }

    @Override
    public boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse);
        return false;
    }

    @Override
    public boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String newToken = null;
        if(token instanceof JWTToken){
            JWTToken jwtToken = (JWTToken)token;
            User user = (User) subject.getPrincipal();
            boolean shouldRefresh = shouldTokenRefresh(JwtUtils.getIssuedAt(jwtToken.getToken()));
            if(shouldRefresh) {
                newToken = JwtUtils.sign(user.getId(),user.getPasswordSalt(),3600);
            }
        }
        if(StringUtils.isNotBlank(newToken)){
            Cookie cToken = new Cookie("x-auth-token", newToken);
            cToken.setMaxAge(259200);
            cToken.setPath(((HttpServletRequest)request).getContextPath());
            ((HttpServletResponse)response).addCookie(cToken);
        }
//            httpResponse.setHeader("x-auth-token", newToken);
        return true;
    }

    @Override
    public boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("Validate token fail, token:{}, error:{}", token.toString(), e.getMessage());
        return false;
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
//        String header = httpRequest.getHeader("x-auth-token");
        Cookie[] cookies = httpRequest.getCookies();
        String token = null;
        if(null!= cookies && cookies.length>0){
            for (Cookie cookie : cookies) {
                if("x-auth-token".equals(cookie.getName())){
                    token = cookie.getValue();
                }
            }
        }
//        return ElasticSearchUtils.removeStart(header, "Bearer ");
        return token;
    }

    protected boolean shouldTokenRefresh(Date issueAt){
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }

    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
    }
}
