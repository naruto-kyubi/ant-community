package org.naruto.framework.security.service;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.naruto.framework.security.domain.ResourceRole;
import org.naruto.framework.security.repository.ResourceRoleReponsitory;
import org.naruto.framework.security.service.jwt.JwtAuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //自动感知平台所有实现Ream的Bean；
    @Autowired
    private List<Realm> authorizingRealmList;

    @Autowired
    private ResourceRoleReponsitory resourceRoleReponsitory;

    @Autowired
    private HttpMethodNoSessionCreationFilter httpMethodNoSessionCreationFilter;

    //web页面访问时，身份认证的Filter
    @Autowired
    private JwtAuthenticatingFilter jwtAuthenticatingFilter;

    //web页面访问时，身份权限验证的Filter
    @Autowired
    private AnyRolesAuthorizationFilter anyRolesAuthorizationFilter;

    @Autowired
    private HttpMethodAuthorizationFilter httpMethodAuthorizationFilter;

    //创建securityManager bean
    @Bean
    public DefaultWebSecurityManager securityManager() {

        DefaultWebSecurityManager  securityManager = new DefaultWebSecurityManager ();
        //设置shiro的Realm处理实现类；
        securityManager.setRealms(authorizingRealmList);

        securityManager.setSessionManager(new DefaultSessionManager() {
            {
                setSessionValidationSchedulerEnabled(false);
            }
        });
        securityManager.setSubjectFactory(new DefaultWebSubjectFactory(){
            public Subject createSubject(SubjectContext context) {
                //不创建session
                context.setSessionCreationEnabled(false);
                return super.createSubject(context);
            }
        });

        securityManager.setSubjectDAO(new DefaultSubjectDAO() {
            {
                setSessionStorageEvaluator(new DefaultWebSessionStorageEvaluator() {
                    {
                        //session不存储；
                        setSessionStorageEnabled(false);
                    }
                });
            }
        });

        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * cookie对象;
     * rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * RememberMe cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        //平台自定义NarutoCookieRememberMeManager，
        CookieRememberMeManager cookieRememberMeManager = new NarutoCookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

    //springboot使用FilterRegistrationBean注册shiroFilter()返回的Filter对象.
    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean(SecurityManager securityManager) throws Exception{
        FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean<Filter>();

        filterRegistration.setFilter((Filter)shiroFilter(securityManager).getObject());

        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setEnabled(true);
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST,DispatcherType.ASYNC);

        return filterRegistration;
    }

    //给过滤器(jwtAuthenticatingFilter,anyRolesAuthorizationFilter)，初始化过滤资源对象列表；
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = factoryBean.getFilters();



        filterMap.put("httpMethodNoSessionCreation", httpMethodNoSessionCreationFilter);
        //token权限验证；
        filterMap.put("jwtAuthToken", jwtAuthenticatingFilter);

        //token权限验证；
        filterMap.put("httpmethod", httpMethodAuthorizationFilter);
        //角色验证。
        filterMap.put("anyRole", anyRolesAuthorizationFilter);

        factoryBean.setFilters(filterMap);
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());

        return factoryBean;
    }

    //数据库中加载权限相关数据，返回DefaultShiroFilterChainDefinition对象。
    @Bean
    public  ShiroFilterChainDefinition shiroFilterChainDefinition() {

        List<ResourceRole> permissions = (List)resourceRoleReponsitory.findAll();

        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        for (ResourceRole permission : permissions) {
            chainDefinition.addPathDefinition(permission.getResourceUrl(), permission.getPermission());
        }
        chainDefinition.addPathDefinition("/v1/user/currentUser","jwtAuthToken[RememberMe]");
        chainDefinition.addPathDefinition("/v1/logon/function","noSessionCreation,jwtAuthToken[RememberMe,permissive]");

        chainDefinition.addPathDefinition("/v1/logon/account","jwtAuthToken");
        chainDefinition.addPathDefinition("/**","noSessionCreation,anon");

        return chainDefinition;
    }
}