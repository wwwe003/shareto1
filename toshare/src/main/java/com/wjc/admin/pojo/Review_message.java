package com.wjc.admin.pojo;

import java.sql.Timestamp;

public class Review_message {
    private String post_id;
    private String title;
    private String author;
    private String adminname;
    private String newtype;
    private String oldtype;

    private String reason;

    private Timestamp update_time;
    private Integer pass;
    private Integer delete;
    private Integer change_type;
    private Integer nopass;
    private Integer state;

    public Integer getNopass() {
        return nopass;
    }

    public void setNopass(Integer nopass) {
        this.nopass = nopass;
    }

    public Integer getChange_type() {
        return change_type;
    }

    public void setChange_type(Integer change_type) {
        this.change_type = change_type;
    }

    public String getNewtype() {
        return newtype;
    }

    public void setNewtype(String newtype) {
        this.newtype = newtype;
    }

    public String getOldtype() {
        return oldtype;
    }

    public void setOldtype(String oldtype) {
        this.oldtype = oldtype;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }
}
