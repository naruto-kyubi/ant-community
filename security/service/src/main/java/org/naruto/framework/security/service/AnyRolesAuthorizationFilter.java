package org.naruto.framework.security.service;


import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AnyRolesAuthorizationFilter  extends AuthorizationFilter {
    private static final String DEFAULT_PATH_SEPARATOR = "/";

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response){
        request.setAttribute("anyRolesAuthFilter.FILTERED", true);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        Boolean afterFiltered = (Boolean)(servletRequest.getAttribute("anyRolesAuthFilter.FILTERED"));
        if( BooleanUtils.isTrue(afterFiltered))
            return true;

        Subject subject = getSubject(servletRequest, servletResponse);
        String[] rolesArray = (String[]) mappedValue;
        if (rolesArray == null || rolesArray.length == 0) { //没有角色限制，有权限访问
            return true;
        }
        for (String role : rolesArray) {
            if (subject.hasRole(role)) //若当前用户是rolesArray中的任何一个，则有权限访问
                return true;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
        return false;
    }

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = this.getPathWithinApplication(request);
        if (requestURI != null && requestURI.endsWith(DEFAULT_PATH_SEPARATOR)) {
            requestURI = requestURI.substring(0, requestURI.length() - 1);
        }

//        path = POST:/v1/articles/**
        String[] paths = path.split(":",2);
//        paths[0]=POST  path[1]=/v1/articles/**
        if (paths.length>1 && paths[1] != null && paths[1].endsWith(DEFAULT_PATH_SEPARATOR)) {
            paths[1] = paths[1].substring(0 , paths[0].length() - 1);
        }
        if (paths.length <= 1) {
            // 分割出来只有URL
            return this.pathsMatch(paths[0], requestURI);
        } else {
            // 分割出url+httpMethod,判断httpMethod和request请求的method是否一致,不一致直接false
            String httpMethod = WebUtils.toHttp(request).getMethod().toUpperCase();
            return httpMethod.equals(paths[0].toUpperCase()) && this.pathsMatch(paths[1], requestURI);
        }
    }
//@Override
//protected boolean pathsMatch(String path, ServletRequest request) {
//    String requestURI = this.getPathWithinApplication(request);
//    if (requestURI != null && requestURI.endsWith(DEFAULT_PATH_SEPARATOR)) {
//        requestURI = requestURI.substring(0, requestURI.length() - 1);
//    }
//    // path: url==method eg: http://api/menu==GET   需要解析出path中的url和httpMethod
//    String[] strings = path.split("==");
//    if (strings[0] != null && strings[0].endsWith(DEFAULT_PATH_SEPARATOR)) {
//        strings[0] = strings[0].substring(0 , strings[0].length() - 1);
//    }
//    if (strings.length <= 1) {
//        // 分割出来只有URL
//        return this.pathsMatch(strings[0], requestURI);
//    } else {
//        // 分割出url+httpMethod,判断httpMethod和request请求的method是否一致,不一致直接false
//        String httpMethod = WebUtils.toHttp(request).getMethod().toUpperCase();
//        return httpMethod.equals(strings[1].toUpperCase()) && this.pathsMatch(strings[0], requestURI);
//    }
//}
}