package com.wjc.post.pojo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Comment {
    private Integer cid;

    private String username;
    private String content;
    private String post_id;
    private String userno;

    private Timestamp create_time;
    private Integer parent_id;
    private String pname;

    private int author_comment;

    //association select
    private List<Comment> replyComments = new ArrayList<>();

    private Post post;
    private String pno;

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public int getAuthor_comment() {
        return author_comment;
    }

    public void setAuthor_comment(int author_comment) {
        this.author_comment = author_comment;
    }

    public List<Comment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<Comment> replyComments) {
        this.replyComments = replyComments;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "cid=" + cid +
                ", username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", post_id='" + post_id + '\'' +
                ", userno='" + userno + '\'' +
                ", create_time=" + create_time +
                ", parent_id=" + parent_id +
                ", pname='" + pname + '\'' +
                ", author_comment=" + author_comment +
                ", replyComments=" + replyComments +
                ", post=" + post +
                ", pno='" + pno + '\'' +
                '}';
    }
}
