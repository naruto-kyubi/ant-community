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
import org.naruto.framework.security.service.jwt.JwtAuthFilter;
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

    @Autowired
    private List<Realm> authorizingRealmList;

    @Autowired
    private ResourceRoleReponsitory resourceRoleReponsitory;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private AnyRolesAuthorizationFilter anyRolesAuthorizationFilter;

//    @Bean
//    public Authenticator authenticator() {
//        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
//        authenticator.setRealms(Arrays.asList(userPasswordRealm));
//        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
//        return authenticator;
//    }

    @Bean
    public DefaultWebSecurityManager securityManager() {

        DefaultWebSecurityManager  securityManager = new DefaultWebSecurityManager ();
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
        //System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        //System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new NarutoCookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

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

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = factoryBean.getFilters();
        //token权限验证；
        filterMap.put("jwtAuthToken", jwtAuthFilter);

        //角色验证。
        filterMap.put("anyRole", anyRolesAuthorizationFilter);

        factoryBean.setFilters(filterMap);
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());

        return factoryBean;
    }

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