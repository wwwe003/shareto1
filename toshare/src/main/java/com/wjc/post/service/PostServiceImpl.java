package com.wjc.post.service;

import com.wjc.action.mapper.ActionMapper;
import com.wjc.action.pojo.Favorite;
import com.wjc.action.pojo.Follow;
import com.wjc.action.pojo.Like;
import com.wjc.admin.mapper.AdminMapper;
import com.wjc.admin.pojo.Review_message;
import com.wjc.post.pojo.Comment;
import com.wjc.type.mapper.TypeMapper;
import com.wjc.pager.PageBean;
import com.wjc.pager.PageConstants;
import com.wjc.post.mapper.PostMapper;
import com.wjc.post.pojo.Post;
import com.wjc.user.mapper.UserMapper;
import com.wjc.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;



@Service
@Transactional
public class PostServiceImpl implements PostService {

    final PostMapper postMapper;
    @Autowired(required = false)
    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Autowired(required = false)
    UserMapper userMapper;
    @Autowired(required = false)
    TypeMapper typeMapper;
    @Autowired(required = false)
    ActionMapper actionMapper;
    @Autowired(required = false)
    AdminMapper adminMapper;

    //分页显示所有文章
    @Override
    public PageBean<Post> findAllPost(int pageCode) {
        Integer totalRecords = postMapper.findAllRecords();
        int pageSize = PageConstants.POST_PAGE_SIZE;
//        totalRecords=postMapper.findAllRecords();

        List<Post> allPosts = postMapper.findAllPost((pageCode-1)*pageSize, pageSize);
        PageBean<Post> pb = new PageBean<>();
        pb.setBeanList(allPosts);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    //show type1 post by pagination
    @Override
    public PageBean<Post> findByType(String firstName,int pageCode) {

        int pageSize= PageConstants.POST_PAGE_SIZE;
        int totalRecords;

        Integer firstID = typeMapper.find1stID(firstName);
        totalRecords=postMapper.findTotalType_1Records(firstID);

        List<Post> current1stPosts = postMapper.findCurrent1stPosts(firstID, (pageCode - 1)*pageSize, pageSize);
        PageBean<Post> pb = new PageBean<>();
        pb.setBeanList(current1stPosts);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public PageBean<Post> findByType2nd(String secondName, int pageCode) {
        int pageSize= PageConstants.POST_PAGE_SIZE;
        int totalRecords;

        Integer secondID = typeMapper.find2ndID(secondName);

        totalRecords=postMapper.findTotalType_2Records(secondID);

        List<Post> currentPosts = postMapper.findCurrent2ndPosts(secondID, (pageCode-1)*pageSize, pageSize);
        PageBean<Post> pb = new PageBean<>();
        pb.setBeanList(currentPosts);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public Post findPostByID(String pid) {
        return postMapper.findPostByID(pid);
    }

    @Override
    public Boolean followState(String author, String follower_uid) {
        User uidByUsername = userMapper.findUidByUsername(author);
        String follow_uid = uidByUsername.getUid();
//        System.out.println(follow_uid);
        Follow follow = actionMapper.ifFollow(follow_uid, follower_uid);
        return follow != null;
    }

    @Override
    public Boolean likeState(String like_postid, String like_uid) {
        Like like = actionMapper.ifLike(like_postid, like_uid);
        return like != null;
    }

    @Override
    public Boolean favoriteState(String favorite_postid, String favorite_uid) {
        Favorite favorite = actionMapper.ifFavorite(favorite_postid, favorite_uid);
        return favorite != null;
    }

    @Override
    public Integer findLikeNumByPid(String pid) {
        return postMapper.findLikeNumByPid(pid);
    }

    @Override
    public Integer findFavoriteNumByPid(String pid) {
        return postMapper.findFavoriteNumByPid(pid);
    }

    @Override
    public PageBean<Post> findDynamicPost(String userno, int pageCode) {
        int totalRecords;
        String uid = userMapper.findUidByUserNo(userno);
        int pageSize= PageConstants.USER_PAGE_SIZE;
        totalRecords=postMapper.findTotalDynamicRecords(uid);
        PageBean<Post> pb=new PageBean<>();

        List<Post> followerUser = postMapper.findDynamicPost(uid, (pageCode-1)*pageSize, pageSize);

        pb.setBeanList(followerUser);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public PageBean<Post> findFavoritePost(String userno, int pageCode, String sessionUseruid) {
        int totalRecords;
        String uid = userMapper.findUidByUserNo(userno);
        int pageSize= PageConstants.USER_PAGE_SIZE;
        totalRecords=postMapper.findTotalRecords(uid);
        PageBean<Post> pb=new PageBean<>();

        List<Post> followerUser = postMapper.findFavoritePost(uid, (pageCode-1)*pageSize, pageSize);

        pb.setBeanList(followerUser);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public PageBean<Post> findTimeLinePost(String userno, int pageCode) {
        int totalRecords;

        int pageSize= PageConstants.USER_PAGE_SIZE;
        totalRecords=postMapper.findTotalTimeLineRecords(userno);
        PageBean<Post> pb=new PageBean<>();

        List<Post> followerUser = postMapper.findTimeLinePost(userno, (pageCode-1)*pageSize, pageSize);

        pb.setBeanList(followerUser);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public PageBean<Post> findTotallikesPost(String userno, int pageCode) {
        int totalRecords;

        int pageSize= PageConstants.USER_PAGE_SIZE;
        totalRecords=postMapper.findTotallikesRecords(userno);
        PageBean<Post> pb=new PageBean<>();

        List<Post> TotallikesPost = postMapper.findTotallikesPost(userno, (pageCode-1)*pageSize, pageSize);

        pb.setBeanList(TotallikesPost);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public Integer findTotallikes(String username) {
        int num=0;
        List<Integer> totallikes = postMapper.findTotallikes(username);
        for (int i:totallikes){
            num=num+i;
        }
        return num;
    }

    @Override
    public PageBean<Post> findTotallikesPostLow(String userno, int pageCode) {
        int totalRecords;

        int pageSize= PageConstants.USER_PAGE_SIZE;
        totalRecords=postMapper.findTotallikesRecords(userno);
        PageBean<Post> pb=new PageBean<>();

        List<Post> TotallikesPost = postMapper.findTotallikesPostLow(userno, (pageCode-1)*pageSize, pageSize);

        pb.setBeanList(TotallikesPost);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public PageBean<Post> findManagePost(String userno, int pageCode) {
        int totalRecords;
        int state=2;

        int pageSize= PageConstants.POST_PAGE_SIZE;
        totalRecords=postMapper.findTotalTimeLineRecords(userno);
        PageBean<Post> pb=new PageBean<>();

        List<Post> followerUser = postMapper.ManagePostShow(userno, (pageCode-1)*pageSize, pageSize,state);

        pb.setBeanList(followerUser);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public PageBean<Post> findPostByType(String typeName1,String typeName2,int pageCode,String userno) {
        int totalRecords;

        int pageSize= PageConstants.POST_PAGE_SIZE;
        totalRecords=postMapper.findTotalRecordsByType(typeName2,userno);
        PageBean<Post> pb=new PageBean<>();

        List<Post> postByType = postMapper.findPostByType(userno,typeName1,typeName2, (pageCode - 1) * pageSize, pageSize);

        pb.setBeanList(postByType);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public boolean savePost(Post post) {
        Date date = new Date();
        //首次创建文章，创建时间等于更新时间
        if(post.getCreate_time()==null){
            post.setCreate_time(new Timestamp(date.getTime()));
            post.setUpdate_time(post.getCreate_time());
        }else {//更新文章 设置新的更新时间
            post.setUpdate_time(new Timestamp(date.getTime()));
        }


        post.setKeywords("");

        post.setDeleted(0);
        post.setLikes(0);
        post.setFavorites(0);
        post.setOriginal(1);

        SnowflakeIdUtils idWorker = new SnowflakeIdUtils(0, 0);
        String pid=Long.toString(idWorker.nextId());
        post.setPost_id(pid);
//        System.out.println(pid);

            Integer id = postMapper.savePost(post);
            return id > 0;

//        System.out.println(id);
//        System.out.println(post.getId());

    }

    @Override
    public boolean saveDraft(Post post) {
        post.setDeleted(0);
        Date date = new Date();
        post.setUpdate_time(new Timestamp(date.getTime()));
        Integer id = postMapper.saveDraft(post);
        return id==1;
    }

    @Override
    public PageBean<Post> findPostByKeyword(String limit, String keyword,Integer pageCode) {
        int pageSize= PageConstants.POST_PAGE_SIZE;
        if (keyword==null||keyword.trim().isEmpty()){
            return null;
        }else{
            keyword=keyword.trim();
            PageBean<Post> pb=new PageBean<>();
            if (limit.equals("author")){
                pb.setBeanList(postMapper.searchByAuthor(keyword, (pageCode - 1) * pageSize, pageSize));
                pb.setTotalRecord(postMapper.findTotalRecordsByAuthor(keyword));
            }else if (limit.equals("title")){
                pb.setBeanList(postMapper.searchByTitle(keyword, (pageCode - 1) * pageSize, pageSize));
                pb.setTotalRecord(postMapper.findTotalRecordsByTitle(keyword));
            }else {
                pb.setBeanList(postMapper.searchByContent(keyword, (pageCode - 1) * pageSize, pageSize));
                pb.setTotalRecord(postMapper.findTotalRecordsByContent(keyword));
            }
            pb.setPageCode(pageCode);
            pb.setPageSize(pageSize);
            return pb;
        }
    }



    @Override
    public PageBean<Post> findDeletedArticle(String userno, int pageCode) {
        int totalRecords;
        int state=0;
        int deleted=1;
        int draft=0;
        int pageSize= PageConstants.POST_PAGE_SIZE;
        totalRecords=postMapper.findTotalDeletedRecords(userno,state,deleted,draft);
        PageBean<Post> pb=new PageBean<>();

        List<Post> followerUser = postMapper.findDeletedArticles(userno, (pageCode-1)*pageSize, pageSize,state,deleted,draft);

        pb.setBeanList(followerUser);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public PageBean<Post> findDraftArticle(String userno, int pageCode) {
        int totalRecords;
        int state=0;
        int draft=1;
        int deleted=0;

        int pageSize= PageConstants.POST_PAGE_SIZE;
        totalRecords=postMapper.findTotalDraftRecords(userno,state,draft,deleted);
        PageBean<Post> pb=new PageBean<>();

        List<Post> followerUser = postMapper.findDraftArticles(userno, (pageCode-1)*pageSize, pageSize,state,draft,deleted);

        pb.setBeanList(followerUser);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public PageBean<Post> findUnderReviewArticle(String userno, int pageCode) {
        int totalRecords;
        int state=1;
        int draft=0;
        int deleted=0;

        int pageSize= PageConstants.POST_PAGE_SIZE;
        totalRecords=postMapper.findTotalUnderReviewRecords(userno,state,draft,deleted);
        PageBean<Post> pb=new PageBean<>();

        List<Post> followerUser = postMapper.findUnderReviewArticles(userno, (pageCode-1)*pageSize, pageSize,state,draft,deleted);

        pb.setBeanList(followerUser);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    public void changeState(String pid) {
        Date date=new Date();
        postMapper.changeState(pid,new Timestamp(date.getTime()));
    }

    @Override
    public PageBean<String> historyUserArticle(String username,Integer pageCode) {
        List<String> historyArticles=new ArrayList<>();
        int pageSize=5;
        List<Review_message> reviewMessages=postMapper.findArticleHistory(username,(pageCode-1)*pageSize, pageSize);
        PageBean<String> pb=new PageBean<>();
        pb.setPageSize(pageSize);
        pb.setTotalRecord(postMapper.historyCount(username));
        pb.setPageCode(pageCode);

        for (Review_message reviewMessage:reviewMessages){
            String time=reviewMessage.getUpdate_time().toString().substring(0,reviewMessage.getUpdate_time().toString().indexOf("."));
            String messagePrefix="<div class='input-group'>"+
                    "<h5>";

            if (reviewMessage.getChange_type()==1){
                String messageSuffixes="The type of article: <span class='text-danger'>\""+reviewMessage.getTitle()+"\"</span> has been <span class='operation'>changed</span>"
                        +" from <span class='text-secondary'>"+reviewMessage.getOldtype()+"</span> to <span class='text-warning'>"+reviewMessage.getNewtype()+"</span> in <span class='text-info'>"+
                        time+"</span></h5></div>";
                historyArticles.add(messagePrefix+messageSuffixes);
            }else if (reviewMessage.getPass()==1){
                String messageSuffixes="The article: <span class='text-danger'>\""+reviewMessage.getTitle()+"\"</span><span class='operation'> has been passed</span>"
                        +"</span> in <span class='text-info'>"+time+"</span></h5></div>";
                historyArticles.add(messagePrefix+messageSuffixes);
            }else if (reviewMessage.getNopass()==1){
                String messageSuffixes="The article: <span class='text-danger'>\""+reviewMessage.getTitle()+"\"</span><span class='operation'> hasn't been passed</span>"
                        +"</span> in <span class='text-info'>"+time+"</span></h5></div>";
                historyArticles.add(messagePrefix+messageSuffixes);
            }else if (reviewMessage.getDelete()==1){
                String messageSuffixes="The article: <span class='text-danger'>\""+reviewMessage.getTitle()+"\"</span><span class='operation'> has been deleted</span> in <span class='text-info'>"+time+"</span></h5></div>";
                historyArticles.add(messagePrefix+messageSuffixes);
            }
        }
        pb.setBeanList(historyArticles);
        return pb;
    }


    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Date date=new Date();
        comment.setCreate_time(new Timestamp(date.getTime()));
        if (comment.getParent_id()==-1){
            comment.setParent_id(null);
        }
        if (comment.getPname().equals("null")){
            comment.setPname(null);
        }
        if (comment.getPno().equals("null")){
            comment.setPno(null);
        }
        Integer id = postMapper.saveComment(comment);
//        System.out.println(id);
       // System.out.println(comment.getCid());
        return null;
    }

    @Override
    public int commentCount(String pid) {
        return postMapper.commentCount(pid);
    }

    @Override
    public List<Comment> listCommentByBlogId(String postID) {
        List<Comment> comByPID = postMapper.findComByPID(postID);
        return combineChildren(comByPID);
        //return comByPID;
    }

    private List<Comment> combineChildren(List<Comment> comments) {
        List<Comment> commentsParents = new ArrayList<>();
        List<Comment> commentsChildren = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getParent_id()==null){
                commentsParents.add(comment);
            }else {
                commentsChildren.add(comment);
            }
        }

        for (Comment commentParent:commentsParents){
            if (commentsChildren.size()>0){
                childRecursion(commentsChildren,commentParent.getCid());
            }
            tempReplys.sort(Comparator.comparing(Comment::getCreate_time).reversed());
            commentParent.setReplyComments(tempReplys);//tempReplys是全局变量，所以放在这个for循环的前中后都可以？
            tempReplys = new ArrayList<>();
        }
        return commentsParents;
    }

    private void childRecursion(List<Comment> commentsChildren,Integer cid) {
        List<Comment> nextParents=new ArrayList<>();
        for (int i=0;i<commentsChildren.size();i++){
            if (commentsChildren.get(i).getParent_id().equals(cid)) {
                tempReplys.add(commentsChildren.get(i));
                nextParents.add(commentsChildren.get(i));
                commentsChildren.remove(commentsChildren.get(i));
                i--;
            }
        }
        if (nextParents.size()>0){
            for (Comment comment:nextParents)
            childRecursion(commentsChildren,comment.getCid());
        }
    }

    private List<Comment> tempReplys = new ArrayList<>();
}
