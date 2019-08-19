package org.naruto.framework.security.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.naruto.framework.security.domain.ResourceRole;
import org.naruto.framework.security.repository.ResourceRoleReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ResourceRoleService {
    @Autowired
    private ResourceRoleReponsitory resourceRoleReponsitory;

//    @Autowired
//    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    @Autowired
    private ShiroFilterFactoryBean shiroFilter;

    /**
     * 重新加载权限
     */
    public void updatePermission() {

        synchronized (shiroFilter) {

            AbstractShiroFilter obj = null;
            try {
                obj = (AbstractShiroFilter) shiroFilter.getObject();
            } catch (Exception e) {
                throw new RuntimeException(
                        "get ShiroFilter from shiroFilterFactoryBean error!");
            }


            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) obj.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空已有权限控制
            manager.getFilterChains().clear();

            shiroFilter.getFilterChainDefinitionMap().clear();
            shiroFilter.setFilterChainDefinitionMap(loadFilterChainDefinitions().getFilterChainMap());
            // 重新构建生成
            Map<String, String> chains = shiroFilter.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
            log.info("update perm success");
        }
    }

    public DefaultShiroFilterChainDefinition loadFilterChainDefinitions() {
        List<ResourceRole> permissions =(List) resourceRoleReponsitory.findAll();
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        for (ResourceRole obj : permissions) {
            chainDefinition.addPathDefinition(obj.getResourceUrl(), obj.getPermission());
        }
        chainDefinition.addPathDefinition("/login","anon");
        chainDefinition.addPathDefinition("/**","anon");
        return chainDefinition;
    }

    public List queryUserFunctions(String userId){
        List<Map> list = resourceRoleReponsitory.queryUserFunctions(userId);
        return list;
    }

    public List queryRoleFunctions(String roleId){
        List<Map> list = resourceRoleReponsitory.queryRoleFunctions(roleId);
        return list;
    }
}
