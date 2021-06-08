package com.wjc.admin.service;

import com.wjc.admin.pojo.Admin;
import com.wjc.admin.pojo.Review_message;
import com.wjc.admin.pojo.UserDetail;
import com.wjc.pager.PageBean;
import com.wjc.post.pojo.Post;
import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;
import com.wjc.user.pojo.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface AdminService {
    Admin login(Admin admin);

    Boolean addSupertype(Type_first supertype,String adminname);

    Boolean addSubtype(Type_second type_second,String adminname,String superTypeName);

    Boolean editType(String oldSuperName,String currentTypeName,String typeName,Integer typeID,String adminname);

    Integer deleteType(String oldSuperName,String typeName, Integer supOrSubType,String adminname);

    PageBean<Post> findPostsBySubtypeID(Integer di2,Integer pageCode);
    void changeType(Review_message reviewMessage);

    PageBean<Post> showUnderReviewPosts(int state,int pageCode,String subtypeID);

    Map<Type_first,List<Type_second>> findAllReviewTypeAndCount();

    Map<Type_first,List<Type_second>> findAllTypeAndCount();

    void saveReview(Review_message reviewMessage);

    PageBean<String> history_article(Integer pageCode);
    PageBean<String> history_type(Integer pageCode);

    PageBean<User> getUserList(Integer pageCode);

    UserDetail getUserDetail(String uno);

    Integer findTotallikes(String uno);

    PageBean<Post> reviewArticles(String userno,Integer pageCode);

    PageBean<Post> favoriteArticles(String uid,Integer pageCode);

//    List<Type_first> findSupertypeAndCount();
//    List<Type_second> findSubtypeAndCount();
}
