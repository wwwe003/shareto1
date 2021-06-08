package com.wjc.post.service;

import com.wjc.pager.PageBean;
import com.wjc.post.pojo.Comment;
import com.wjc.post.pojo.Post;

import java.util.List;

public interface PostService {
//    PageBean<Post> findByCriteria(List<Expression> expressionList, int pageCode);
    //PageBean<Post> findByType(Integer type,int pageCode);

    //分页显示所有文章
    PageBean<Post> findAllPost(int pageCode);

    //分页显示某一种分类(一级标题)的文章
    PageBean<Post> findByType(String firstName, int pageCode);

    //分页显示某一种分类(二级标题)的文章
    PageBean<Post> findByType2nd(String secondName, int pageCode);

    //find specific article by post_id
    Post findPostByID(String pid);
    //estimate whether follow
    Boolean followState(String author,String follower_uid);

    Boolean likeState(String like_postid, String like_uid);

    Boolean favoriteState(String favorite_postid, String favorite_uid);

    Integer findLikeNumByPid(String pid);

    Integer findFavoriteNumByPid(String pid);

    PageBean<Post> findDynamicPost(String userno, int pageCode);
    PageBean<Post> findFavoritePost(String userno, int pageCode, String sessionUseruid);

    PageBean<Post> findTimeLinePost(String userno, int pageCode);

    PageBean<Post> findTotallikesPost(String userno, int pageCode);

    Integer findTotallikes(String username);

    PageBean<Post> findTotallikesPostLow(String userno, int pageCode);

    PageBean<Post> findManagePost(String userno, int pageCode);

    PageBean<Post> findPostByType(String typeName1,String typeName2,int pageCode,String userno);

    boolean savePost(Post post);
    boolean saveDraft(Post post);

    PageBean<Post> findPostByKeyword(String limit, String keyword,Integer pageCode);

    PageBean<Post> findDeletedArticle(String userno, int pageCode);
    PageBean<Post> findDraftArticle(String userno, int pageCode);
    PageBean<Post> findUnderReviewArticle(String userno, int pageCode);

    void changeState(String pid);


    PageBean<String> historyUserArticle(String username,Integer pageCode);

    List<Comment> listCommentByBlogId(String postID);
    Comment saveComment(Comment comment);

    int commentCount(String pid);
}
