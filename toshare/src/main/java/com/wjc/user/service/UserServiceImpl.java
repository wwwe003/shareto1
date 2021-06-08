package com.wjc.user.service;

import com.wjc.pager.PageBean;
import com.wjc.pager.PageConstants;
import com.wjc.post.mapper.PostMapper;
import com.wjc.post.pojo.Post;
import com.wjc.user.mapper.UserMapper;
import com.wjc.user.pojo.User;
import com.wjc.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private PostMapper postMapper;

    @Override
    public boolean ajaxValidateUsername(String username) {
        int count= userMapper.ajaxValidateUsername(username);
        return count==0;
    }

    @Override
    public boolean ajaxValidateEmail(String email) {
        int count= userMapper.ajaxValidateEmail(email);
        return count==0;

    }

    @Override
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
    public User regist(User user) {
        user.setUid(UUID.randomUUID().toString());

        Integer maxno=userMapper.findMaxUserNo();
        user.setUserno(String.valueOf(maxno+1));

        user.setRole("user");

        //md5+salt+hashIterations
        String salt= SaltUtils.getSalt(8);
        user.setSalt(salt);
        Md5Hash md5Hash=new Md5Hash(user.getPassword(),salt,1024);
        user.setPassword(md5Hash.toHex());

        Date date = new Date();
        Timestamp nousedate = new Timestamp(date.getTime());
        user.setRegistdate(nousedate);
        userMapper.save(user);
        return user;
    }

    @Override
    public User login(User user) {
        return userMapper.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @Override
    public User findUserByName(String username) {
        return userMapper.findUserByName(username);
    }

    @Override
    public boolean validateOldPwd(String uid, String oldPwd) {
        int count = userMapper.findByUidAndPassword(uid, oldPwd);
        return count>0;
    }
    @Override
    public void updatePassword(String uid, String newPwd) {
        userMapper.updatePassword(uid,newPwd);
    }

    @Override
    public Integer findFollowNumByPid(String pid) {
        Post post = postMapper.findPostByID(pid);
        return userMapper.findFollowNumByAuthor( post.getAuthor());
    }

    @Override
    public PageBean<User> findFollowingUser(String userno,Integer pageCode,String sessionUserUid) {
        int totalRecords;
        String uid = userMapper.findUidByUserNo(userno);
        int pageSize= PageConstants.USER_PAGE_SIZE;
        totalRecords=userMapper.findTotalFollowingRecords(uid);
        PageBean<User> pb=new PageBean<>();

        List<User> followerUser = userMapper.findFollowingUser(uid,(pageCode-1)*pageSize, pageSize);

        pb.setBeanList(followerUser);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        if (sessionUserUid==null){
            pb.setFollowingUser(null);
        }else if (!sessionUserUid.equals(uid)) {
            List<String> followingUsername = userMapper.findFollowingUsername(sessionUserUid);
            pb.setFollowingUser(followingUsername);
            return pb;
        }
//        if (!sessionUserUid.equals(uid)) {
//            List<String> followingUsername = userMapper.findFollowingUsername(sessionUserUid);
//            pb.setFollowingUser(followingUsername);
//            return pb;
//        }
        return pb;
    }

    @Override
    public PageBean<User> findFollowerUser(String userno,Integer pageCode,String sessionUserUid) {
        int totalRecords;
        String uid = userMapper.findUidByUserNo(userno);
        int pageSize= PageConstants.USER_PAGE_SIZE;
        totalRecords=userMapper.findTotalFollowerRecords(uid);

        List<User> followerUser = userMapper.findFollowerUser(uid,(pageCode-1)*pageSize, pageSize);

        PageBean<User> pb=new PageBean<>();
        pb.setBeanList(followerUser);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        if (sessionUserUid==null){
            pb.setFollowingUser(null);
        }else {
            List<String> followerUsername = userMapper.findFollowingUsername(sessionUserUid);
            pb.setFollowingUser(followerUsername);
        }
        return pb;
    }
}
