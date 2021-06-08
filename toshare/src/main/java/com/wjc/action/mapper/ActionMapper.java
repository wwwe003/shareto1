package com.wjc.action.mapper;

import com.wjc.action.pojo.Favorite;
import com.wjc.action.pojo.Follow;
import com.wjc.action.pojo.Like;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActionMapper {
    Follow ifFollow (@Param("follow_uid") String follow_uid, @Param("follower_uid") String follower_uid);
    void saveFollowInfo(Follow follow);
    void deleteFollowInfo(@Param("follow_uid") String follow_uid,@Param("follower_uid") String follower_uid);

    Like ifLike(@Param("like_postid")String like_postid,@Param("like_uid") String like_uid);
    void saveLikeInfo(Like leke);
    void deleteLikeInfo(@Param("like_postid") String like_postid,@Param("like_uid") String like_uid);

    Favorite ifFavorite(String favorite_postid, String favorite_uid);
    void saveFavoriteInfo(Favorite favorite);
    void deleteFavoriteInfo(@Param("favorite_postid") String favorite_postid,@Param("favorite_uid") String favorite_uid);

    List<Favorite> findByUid(@Param("uid") String uid);
}
