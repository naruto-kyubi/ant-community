package org.naruto.framework.security.service;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 资源（URL）的读写权限验证过滤器。
 */
@Component
public class HttpMethodAuthorizationFilter extends AuthorizationFilter {


    @Override
    protected void postHandle(ServletRequest request, ServletResponse response){
        request.setAttribute("httpMethodAuthorizationFilter.FILTERED", true);
    }


    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Boolean afterFiltered = (Boolean)(request.getAttribute("httpMethodAuthorizationFilter.FILTERED"));
        if( BooleanUtils.isTrue(afterFiltered))
            return true;

        String[] value = (String[]) mappedValue;
        //不指定过滤httpmethod类型，则允许访问；
        if(null==value || value.length<1) return true;

        String method = ((HttpServletRequest)request).getMethod();
        for (String s : value) {
            if(s.equals(method)) return true;
        }
        return false;
    }
}
