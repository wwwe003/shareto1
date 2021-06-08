package com.wjc.post.pojo;

import java.sql.Timestamp;

public class Post {
    private java.sql.Timestamp create_time;
    private java.sql.Timestamp update_time;
    private String author;
    private String title;
    private Integer type;
    private Integer type_second_id;
    private String description;
    private String post_id;
    private String html;
    private String markdown;
    private Integer likes;
    private String userno;
    private String type_first_name;
    private String type_second_name;
    private Integer deleted;
    private Integer allow_chat;
    private Integer allow_top;
    private Integer draft;
    private String keywords;
    private Integer favorites;
    private Integer state;
    private Integer original;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOriginal() {
        return original;
    }

    public void setOriginal(Integer original) {
        this.original = original;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getAllow_chat() {
        return allow_chat;
    }

    public void setAllow_chat(Integer allow_chat) {
        this.allow_chat = allow_chat;
    }

    public Integer getAllow_top() {
        return allow_top;
    }

    public void setAllow_top(Integer allow_top) {
        this.allow_top = allow_top;
    }

    public Integer getDraft() {
        return draft;
    }

    public void setDraft(Integer draft) {
        this.draft = draft;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getType_first_name() {
        return type_first_name;
    }

    public void setType_first_name(String type_first_name) {
        this.type_first_name = type_first_name;
    }

    public String getType_second_name() {
        return type_second_name;
    }

    public void setType_second_name(String type_second_name) {
        this.type_second_name = type_second_name;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public Integer getFavorites() {
        return favorites;
    }

    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Integer getType_second_id() {
        return type_second_id;
    }

    public void setType_second_id(Integer type_second_id) {
        this.type_second_id = type_second_id;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
