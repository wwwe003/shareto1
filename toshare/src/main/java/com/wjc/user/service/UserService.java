package com.wjc.user.service;

import com.wjc.pager.PageBean;
import com.wjc.user.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface UserService {
    //regist
    User regist(User user);

    //validate username and email exist or not
    boolean ajaxValidateUsername(String username);
    boolean ajaxValidateEmail(String email);

    //login
    User login(User user);
    User findUserByName(String username);

    //before update validate old password
    boolean validateOldPwd(String uid,String oldPwd);
    //update password
    void updatePassword(String uid, String newPwd);

    Integer findFollowNumByPid(String pid);

    PageBean<User> findFollowingUser(String no,Integer pageCode,String sessionUserUid);

    PageBean<User> findFollowerUser(String no, Integer pageCode, String sessionUseruid);
}
