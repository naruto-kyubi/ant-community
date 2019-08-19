package org.naruto.framework.article.controller;

import org.naruto.framework.article.domain.AccessLog;
import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.service.AccessLogService;
import org.naruto.framework.article.service.ArticleService;
import org.naruto.framework.core.security.SessionUtils;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class ArticleReadedStatInterceptor implements HandlerInterceptor {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AccessLogService accessLogService;

    @Autowired
    private SessionUtils sessionUtils;


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String methodName = method.getName();

        if(!"queryById".equals(methodName)) return;
        //add read count;
        switch (request.getMethod()) {
            case "GET":
                Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                String id = (String) pathVariables.get("id");

                AccessLog accessLog = new AccessLog();

                User user = sessionUtils.getCurrentUser(request);
                String userId=null;
                if(null!=user){
                    //login user access;
                    userId = user.getId();
                }
                accessLog.setUserId(userId);
                accessLog.setArticle(new Article(id));
                accessLogService.save(accessLog);

                articleService.increaseViewCount(id);
                break;
            case "POST":
            case "PUT":
            case "DELETE":

            default:
                break;
        }
    }
}
