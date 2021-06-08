package com.wjc.admin.pojo;

public class UserDetail {
    private String uid;
    private String username;
    private String email;
    private String userno;
    private Integer followernum;
    private Integer follownum;
    private Integer totalLikes;
    private Integer postedCount;
    private Integer underReviewCount;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public Integer getFollowernum() {
        return followernum;
    }

    public void setFollowernum(Integer followernum) {
        this.followernum = followernum;
    }

    public Integer getFollownum() {
        return follownum;
    }

    public void setFollownum(Integer follownum) {
        this.follownum = follownum;
    }

    public Integer getPostedCount() {
        return postedCount;
    }

    public void setPostedCount(Integer postedCount) {
        this.postedCount = postedCount;
    }

    public Integer getUnderReviewCount() {
        return underReviewCount;
    }

    public void setUnderReviewCount(Integer underReviewCount) {
        this.underReviewCount = underReviewCount;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }
}