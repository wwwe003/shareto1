package com.wjc.config;

import com.wjc.shiro.filters.MyFormAuthenticationFilter;
import com.wjc.shiro.filters.MyRolesAuthorizationFilter;
import com.wjc.shiro.realms.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro api config
 */
@Configuration
public class ShiroConfig {
    //create shiro filter
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultSM") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        Map<String,String> map = new LinkedHashMap<>();

        map.put("/user/login","anon");
        map.put("/user/registration","anon");
        map.put("/main","anon");
        map.put("/*","anon");
        map.put("/search","anon");

        map.put("/*/myPost","roles[user]");
        map.put("/*/message","roles[user]");
        map.put("/*/post/*","roles[user]");
        map.put("/*/edit/*","roles[user]");
        map.put("/*/edit/","roles[user]");
        map.put("/*/delete/*","roles[user]");
        map.put("/*/delete/","roles[user]");
        map.put("/user/password","roles[user]");
        map.put("/user/*","anon");

        map.put("/admin/login","anon");

        map.put("/admin/*","roles[admin]");

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("roles",new MyRolesAuthorizationFilter());

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        shiroFilterFactoryBean.setFilters(filters);

        return shiroFilterFactoryBean;
    }
    //create web security manager
    @Bean(name = "getDefaultSM")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("getRealm") Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }
    //create customer realm
    @Bean
    public Realm getRealm(){
        CustomerRealm customerRealm = new CustomerRealm();
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
//        hashedCredentialsMatcher.setHashIterations(1024);
//        customerRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customerRealm;
    }
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
