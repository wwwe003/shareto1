package com.wjc.shiro.filters;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MyRolesAuthorizationFilter extends RolesAuthorizationFilter {

    private static final Logger log = LoggerFactory.getLogger(MyRolesAuthorizationFilter.class);

    private static final String USER_UNAUTHORIZED_URL="/user/login";
    private static final String ADMIN_UNAUTHORIZED_URL="/admin/login";
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) {
            log.trace("Forwarding to the " +
                    "Authentication url [" + getLoginUrl() + "]");
            saveRequestAndRedirectToLogin(request, response);
        } else {
            HttpServletRequest httpServletRequest= WebUtils.toHttp(request);
             {
                String requestURL = String.valueOf(httpServletRequest.getRequestURI());
                log.trace("Login");
                if (requestURL.startsWith("/toshare/admin/")) {
                    WebUtils.issueRedirect(request, response, ADMIN_UNAUTHORIZED_URL);
                } else {
                    System.out.println("asdnjwqke");
                    WebUtils.issueRedirect(request, response, USER_UNAUTHORIZED_URL);
                }
            }
        }
        return false;
    }
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
