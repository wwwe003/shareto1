package com.wjc.action.pojo;

import java.sql.Timestamp;

public class Like {
    private String like_postid;
    private String like_uid;
    private Timestamp update_time;

    public String getLike_postid() {
        return like_postid;
    }

    public void setLike_postid(String like_postid) {
        this.like_postid = like_postid;
    }

    public String getLike_uid() {
        return like_uid;
    }

    public void setLike_uid(String like_uid) {
        this.like_uid = like_uid;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }
}
