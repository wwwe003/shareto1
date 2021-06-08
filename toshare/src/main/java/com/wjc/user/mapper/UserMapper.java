package com.wjc.user.mapper;


import com.wjc.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    //registration
    void save(User user);
    //对用户名，邮箱进行校验，校验是否注册
    int ajaxValidateUsername(@Param("username") String username);


    int ajaxValidateEmail(@Param("email") String email);

    //login
    User findByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    //update password
    int findByUidAndPassword(@Param("uid") String uid, @Param("password") String password);

    void updatePassword(@Param("uid") String uid, @Param("password") String password);

    //save user object
    User saveUserInfo(@Param("username") String username,@Param("password") String password);

    User findUidByUsername(String author);

    void updateFollowerNum(@Param("followernum") Integer followernum ,@Param("uid") String uid);

    Integer findFollowNumByAuthor(String author);

    String findUidByUserNo(String userno);

    List<User> findFollowingUser(@Param("follower_uid") String follower_uid,Integer pageCode,Integer pageSize);

    User findUserByUid(@Param("uid") String uid);

    void updateFollowNum(@Param("follownum") int i,@Param("uid") String follower_uid);

    Integer findMaxUserNo();

    List<User> findFollowerUser(@Param("follow_uid") String follow_uid,Integer pageCode,Integer pageSize);

    int findTotalFollowerRecords(String uid);
    int findTotalFollowingRecords(String uid);

    List<String> findFollowingUsername(String uid);

    User findUserByName(String username);
}
