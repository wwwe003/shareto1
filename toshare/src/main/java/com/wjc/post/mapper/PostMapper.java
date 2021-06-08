package com.wjc.post.mapper;

import com.wjc.admin.pojo.Review_message;
import com.wjc.pager.PageBean;
import com.wjc.post.pojo.Comment;
import com.wjc.post.pojo.Post;
import org.apache.ibatis.annotations.Param;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public interface PostMapper {
    //PageBean<Post> findByCriteria(List<Expression> expressionList, int pageCode);

    //查询总文章数
    Integer findAllRecords();

    //分页显示所有文章
    List<Post> findAllPost(int pageCode,int pageSize);

    //查询某一种分类的文章的总记录数
    Integer findTotalType_1Records(Integer firstID);
    Integer findTotalType_2Records(Integer secondID);

    //分页显示某一种分类的文章
    List<Post> findCurrent1stPosts(Integer type,int pageCode, int pageSize);
    List<Post> findCurrent2ndPosts(Integer type,int pageCode, int pageSize);

    //find posts by type_second_id
    List<Post> find2ndPostsBy2ndID(Integer ID);

    //find specific article by post_id
    Post findPostByID(String pid);


    void updateLikeNum(@Param("likes") Integer likeNum , @Param("post_id") String like_postid);

    void updateFavoriteNum(@Param("favorites") Integer favoriteNum , @Param("post_id") String favorite_postid);

    Integer findLikeNumByPid(String pid);

    Integer findFavoriteNumByPid(String pid);

    List<Post> findFavoritePost(String uid, int pageCode ,int pageSize);

    int findTotalRecords(String uid);

    int findTotalDynamicRecords(String uid);

    List<Post> findDynamicPost(String uid, int pageCode, int pageSize);

    int findTotalTimeLineRecords(String userno);

    List<Post> findTimeLinePost(String userno, int pageCode, int pageSize);

    int findTotallikesRecords(String userno);

    List<Post> findTotallikesPost(String userno, int pageCode, int pageSize);

    List<Integer> findTotallikes(String username);

    List<Post> findTotallikesPostLow(String userno, int pageCode, int pageSize);

    List<Post> ManagePostShow(String userno, int pageCode, int pageSize,int state);

    List<Post> findPostByType(String userno,String typeName1,String typeName2,int pageCode, int pageSize);

    int findTotalRecordsByType(String typeName2,String userno);

    Integer savePost(Post post);
    Integer saveDraft(Post post);
//    Integer findPostByPid(String pid);

//    void changeDraft(String pid);

    void changeDeleted(String pid,Timestamp time);


    //search

    List<Post> searchByAuthor(String keyword,int pageCode, int pageSize);

    List<Post> searchByTitle(String keyword,int pageCode, int pageSize);

    List<Post> searchByContent(String keyword,int pageCode, int pageSize);

    int findTotalRecordsByAuthor(String keyword);

    int findTotalRecordsByTitle(String keyword);

    int findTotalRecordsByContent(String keyword);

    //drafts, trash, underReview
    int findTotalDraftRecords(String userno, int state, int draft,int deleted);
    int findTotalDeletedRecords(String userno, int state, int deleted, int draft);
    int findTotalUnderReviewRecords(String userno, int state, int draft, int deleted);

    List<Post> findDeletedArticles(String userno, int pageCode, int pageSize, int state, int deleted,int draft);
    List<Post> findDraftArticles(String userno, int pageCode, int pageSize, int state, int draft,int deleted);
    List<Post> findUnderReviewArticles(String userno, int pageCode, int pageSize, int state, int draft, int deleted);

    void changeState(String pid, Timestamp time);

    Integer completeDelete(String pid);


    List<Review_message> findArticleHistory(String username,Integer pageCode, Integer pageSize);

    int historyCount(String username);

    void unreadToRead(String username);

    List<Comment> findComByPID(String postID);

    List<Comment> findComByPIDtest(Integer parent_id);

    Integer saveComment(Comment comment);

    int commentCount(String pid);
}
