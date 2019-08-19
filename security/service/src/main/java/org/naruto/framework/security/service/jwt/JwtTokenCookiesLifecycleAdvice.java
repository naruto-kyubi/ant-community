package org.naruto.framework.security.service.jwt;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.user.domain.User;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class JwtTokenCookiesLifecycleAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        String methodName=returnType.getMethod().getName();
        return "logon".equals(methodName) || "logout".equals(methodName);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String methodName=returnType.getMethod().getName();
        HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse res = ((ServletServerHttpResponse) response).getServletResponse();

        if("logon".equals(methodName)){
            //logon ok ,generate x-auth-token cookie;
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            String newToken = JwtUtils.sign(user.getId(), user.getPasswordSalt(), 3600);
            Cookie cToken = new Cookie("x-auth-token", newToken);
            cToken.setMaxAge(259200);
            cToken.setPath(req.getContextPath());
            res.addCookie(cToken);
            return body;
        }else{
            //logout ok; clear cookies
            Cookie cToken = new Cookie("x-auth-token", "");
            cToken.setMaxAge(0);
            cToken.setPath(req.getContextPath());
            res.addCookie(cToken);
            return body;
        }
    }
}
