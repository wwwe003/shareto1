package com.wjc.shiro.realms;

import com.wjc.action.service.ActionService;
import com.wjc.admin.mapper.AdminMapper;
import com.wjc.admin.pojo.Admin;
import com.wjc.admin.service.AdminService;
import com.wjc.post.service.PostService;
import com.wjc.type.service.TypeService;
import com.wjc.user.pojo.User;
import com.wjc.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    TypeService typeService;
    @Autowired
    ActionService actionService;
    @Autowired
    AdminService adminService;
    @Autowired
    AdminMapper adminMapper;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //get current user
        Subject subject = SecurityUtils.getSubject();
        String name = (String) subject.getPrincipal();

        System.out.println("許可");

        if (userService.findUserByName(name)!=null){
            info.addRole(userService.findUserByName(name).getRole());
        }else {
            info.addRole(adminMapper.findByAdminName(name).getRole());
        }

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//        String principal =(String)token.getPrincipal();
        UsernamePasswordToken userToken= (UsernamePasswordToken) token;

        User user = userService.findUserByName(userToken.getUsername());

        if (user!=null){
            System.out.println("useruseruseruser");
            return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(), ByteSource.Util.bytes(user.getSalt()),this.getName());
        }else {
            System.out.println("adminadminadmin");
            Admin admin = adminMapper.findByAdminName(userToken.getUsername());
            if (admin!=null) {
                return new SimpleAuthenticationInfo(admin.getAdminname(), admin.getPassword(), this.getName());
            }else {
                return null;
            }
        }
    }
}
