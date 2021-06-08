package com.wjc.action.pojo;

import java.sql.Timestamp;

public class Favorite {
    private String favorite_uid;
    private String favorite_postid;
    private Timestamp update_time;

    public String getFavorite_uid() {
        return favorite_uid;
    }

    public void setFavorite_uid(String favorite_uid) {
        this.favorite_uid = favorite_uid;
    }

    public String getFavorite_postid() {
        return favorite_postid;
    }

    public void setFavorite_postid(String favorite_postid) {
        this.favorite_postid = favorite_postid;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }
}
