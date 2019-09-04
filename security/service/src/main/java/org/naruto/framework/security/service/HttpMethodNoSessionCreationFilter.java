package org.naruto.framework.security.service;

import org.apache.shiro.web.filter.session.NoSessionCreationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;

@Component
public class HttpMethodNoSessionCreationFilter extends NoSessionCreationFilter {

    private static final String DEFAULT_PATH_SEPARATOR = "/";

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
}
