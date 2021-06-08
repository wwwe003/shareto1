package com.wjc.action.service;

import com.wjc.action.mapper.ActionMapper;
import com.wjc.action.pojo.Favorite;
import com.wjc.action.pojo.Follow;
import com.wjc.action.pojo.Like;
import com.wjc.post.mapper.PostMapper;
import com.wjc.post.pojo.Post;
import com.wjc.user.mapper.UserMapper;
import com.wjc.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class ActionServiceImpl implements ActionService {
    @Autowired(required = false)
    private ActionMapper actionMapper;
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private PostMapper postMapper;
    @Override
    public boolean updateFollow(String author, String follower_uid, Timestamp update_time ) {
        User user=userMapper.findUidByUsername(author);
        String follow_uid=user.getUid();
        User followerUser = userMapper.findUserByUid(follower_uid);


        Follow follow = actionMapper.ifFollow(follow_uid, follower_uid);
        if (follow==null&&(!user.getUid().equals(follower_uid))){//没有关注记录时，且不是自己关注自己时，插入
            follow=new Follow();
            follow.setFollow_uid(user.getUid());
            follow.setFollower_uid(follower_uid);
            follow.setUpdate_time(update_time);
            actionMapper.saveFollowInfo(follow);
            //+1
            userMapper.updateFollowerNum(user.getFollowernum()+1,user.getUid());
            userMapper.updateFollowNum(followerUser.getFollownum()+1,follower_uid);
            return true;
        }else {
            actionMapper.deleteFollowInfo(user.getUid(),follower_uid);
            //-1
            userMapper.updateFollowerNum(user.getFollowernum()-1,user.getUid());
            userMapper.updateFollowNum(followerUser.getFollownum()-1,follower_uid);
            return false;
        }
    }

    @Override
    public boolean updateLike(String like_postid, String like_uid, Timestamp update_time) {
        Post post=postMapper.findPostByID(like_postid);

        Like like = actionMapper.ifLike(like_postid, like_uid);
        if (like==null){//没有点赞记录时，插入，可以自己给自己点赞
            like=new Like();
            like.setLike_postid(like_postid);
            like.setLike_uid(like_uid);
            like.setUpdate_time(update_time);
            actionMapper.saveLikeInfo(like);
            //+1
            postMapper.updateLikeNum(post.getLikes()+1,like_postid);
            return true;
        }else {
            actionMapper.deleteLikeInfo(like_postid,like_uid);
            //-1
            postMapper.updateLikeNum(post.getLikes()-1,like_postid);
            return false;
        }
    }

    @Override
    public List<Favorite> findFavoriteList(String uid) {
        return actionMapper.findByUid(uid);
    }

    @Override
    public boolean updateFavorite(String favorite_postid, String favorite_uid, Timestamp update_time) {
        Post post=postMapper.findPostByID(favorite_postid);

        Favorite favorite = actionMapper.ifFavorite(favorite_postid, favorite_uid);
        if (favorite==null){//没有点赞记录时，插入，可以自己给自己点赞
            favorite=new Favorite();
            favorite.setFavorite_postid(favorite_postid);
            favorite.setFavorite_uid(favorite_uid);
            favorite.setUpdate_time(update_time);
            actionMapper.saveFavoriteInfo(favorite);
            //+1
            postMapper.updateFavoriteNum(post.getFavorites()+1,favorite_postid);
            return true;
        }else {
            actionMapper.deleteFavoriteInfo(favorite_postid,favorite_uid);
            //-1
            postMapper.updateFavoriteNum(post.getFavorites()-1,favorite_postid);
            return false;
        }
    }
}
