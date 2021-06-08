package com.wjc.user.pojo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;


public class User {

    private String uid;
    //@NotBlank(message = "usernameを入力してください。")
    //@Size(min = 4,max = 20,message = "4から20までの文字を入力してください。")
    private String username;
//    @NotBlank(message = "passwordを入力してください。")
//    @Size(min = 6,max = 20,message = "6から20までの文字を入力してください。")
    private String password;

//    @Email(regexp = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$"
//    ,message = "正しいメールアドレスの形式を入力してください。")
    private String email;

    private String userno;

    private String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    //    @NotBlank(message ="確認パスワードを入力してください。")
    private String confirmPwd;

    private String newPwd;

    private Integer followernum;

    private Integer follownum;

    private Timestamp registdate;

    private String role;

    public Timestamp getRegistdate() {
        return registdate;
    }

    public void setRegistdate(Timestamp registdate) {
        this.registdate = registdate;
    }

    public Integer getFollownum() {
        return follownum;
    }

    public void setFollownum(Integer follownum) {
        this.follownum = follownum;
    }

    public Integer getFollowernum() {
        return followernum;
    }

    public void setFollowernum(Integer followernum) {
        this.followernum = followernum;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userno='" + userno + '\'' +
                ", salt='" + salt + '\'' +
                ", confirmPwd='" + confirmPwd + '\'' +
                ", newPwd='" + newPwd + '\'' +
                ", followernum=" + followernum +
                ", follownum=" + follownum +
                ", registdate=" + registdate +
                ", role='" + role + '\'' +
                '}';
    }
}
