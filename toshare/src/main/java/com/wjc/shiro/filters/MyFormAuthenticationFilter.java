package com.wjc.shiro.filters;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    private static final String USER_LOGIN_URL="/user/login";
    private static final String ADMIN_LOGIN_URL="/admin/login";

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        String requestURL = String.valueOf(httpServletRequest.getRequestURI());
        if (requestURL.startsWith("/toshare/admin/")){
            WebUtils.issueRedirect(request,response,ADMIN_LOGIN_URL);
        }else {
            WebUtils.issueRedirect(request,response,USER_LOGIN_URL);
        }
    }


}
