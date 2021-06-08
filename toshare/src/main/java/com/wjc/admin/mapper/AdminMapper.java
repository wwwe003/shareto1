package com.wjc.admin.mapper;

import com.wjc.admin.pojo.Admin;
import com.wjc.admin.pojo.Review_message;
import com.wjc.admin.pojo.TypeHistory;
import com.wjc.admin.pojo.UserDetail;
import com.wjc.post.pojo.Post;
import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;
import com.wjc.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public interface AdminMapper {
    Admin findByAdminnameAndPassword(String adminname, String password);
    Admin findByAdminName(String adminname);

    Integer addSupertype(Type_first type_first);

    Integer addSubtype(Type_second type_second);

    Integer editSupertype(String typeName,String currentTypeName);

    Integer editSubtype(String typeName,String currentTypeName);

    void deleteSupertype(String typeName);
    void deleteSubtype(String typeName);

    Integer findSubtypeCount(Integer typeID);

    List<Post> findCurrent2ndPosts(Integer id2, int pageCode, int pageSize);
    void changeType(Integer supertypeID,Integer subtypeID,String pid);


    //review manage
    int findTotalUnderReviewPostsRecords(int state);
    List<Post> showUnderReviewPosts(Integer state,int pageCode,int pageSize);
    void review(Integer state,Integer draft,String pid);

    List<HashMap<String, Object>> findSupertypeAndCount();
    List<HashMap<String, Object>> findSubtypeAndCount();
//    modified methods based on above
    List<Type_first> supertypeAndPostCount();
    List<Type_second> subtypeAndPostCount();

    int findTotalRecordsBySubtype(int state, int subtypeID);
    List<Post> showReviewResultBySubtype(int state, int subtypeID, int pageCode, int pageSize);
    Post articleByPostID(String pid);

    void changeState(Integer state, String pid);

    void saveReason(Review_message reviewMessage);

 //   void historyArticleType(HistoryArticleType historyArticleType);

    Integer deleteArticle(String pid);

//    Integer saveReviewMessage(Review_message reviewMessage);
    void saveReviewMessage(Review_message reviewMessage);

    List<Review_message> findArticleHistory(Integer pageCode,Integer pageSize);

    int historyCount();
    int historyUnreadCount(String author);

    void saveReviewDelete(Review_message reviewMessage);
    void saveReviewNopass(Review_message reviewMessage);
    void saveReviewPass(Review_message reviewMessage);

    List<User> getUserList(Integer pageCode,Integer pageSize);

    int getUserListCount();

    UserDetail getUserDetail(String uno);

    List<Integer> findTotallikes(String uno);

    Integer getFavoriteCount(String uno);

    int getReviewArticlesCount(String uno);

    List<Post> getReviewArticles(String uno,int pageCode, int pageSize);

    List<Post> getFavoriteArticles(String uno,int pageCode, int pageSize);
//type history
    void addSupertypeHistory(String type_first_name, Timestamp change_time, String adminname);
    void addSubtypeHistory(String type_second_name, String superTypeName, Timestamp change_time, String adminname);
    void editTypeHistory(String oldtype, String newtype, Timestamp change_time, String adminname);
    void deleteTypeHistory(String oldtype,Timestamp change_time, String adminname);

    Integer deleteSubtypeConfirm(Integer typeID);
    Integer checkDraftOrTrash(Integer typeID);
    void changeDeletedSubtype(Integer typeID);

    List<TypeHistory> findTypeHistory(int pageCode, int pageSize);

    int historyTypeCount();

    void changeStateToDraft(int state,int draft, String post_id);
}
