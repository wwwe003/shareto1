package com.wjc.action.service;

import com.wjc.action.pojo.Favorite;

import java.sql.Timestamp;
import java.util.List;

public interface ActionService {
    boolean updateFollow (String author, String follower_uid, Timestamp update_time);

    boolean updateLike (String like_postid, String like_uid, Timestamp update_time);

    List<Favorite> findFavoriteList(String uid);

    boolean updateFavorite(String favorite_postid, String favorite_uid, Timestamp up_time);
}
