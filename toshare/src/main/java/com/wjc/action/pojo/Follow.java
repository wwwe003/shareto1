package com.wjc.action.pojo;

import java.sql.Timestamp;

public class Follow {
    private String follow_uid;
    private String follower_uid;
    private Timestamp update_time;

    public String getFollow_uid() {
        return follow_uid;
    }

    public void setFollow_uid(String follow_uid) {
        this.follow_uid = follow_uid;
    }

    public String getFollower_uid() {
        return follower_uid;
    }

    public void setFollower_uid(String follower_uid) {
        this.follower_uid = follower_uid;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }
}
