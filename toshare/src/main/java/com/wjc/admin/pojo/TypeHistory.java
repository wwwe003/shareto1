package com.wjc.admin.pojo;

import java.sql.Timestamp;

public class TypeHistory {
    private Timestamp change_time;
    private String oldtype;
    private String newtype;
    private String adminname;
    private Integer change;
    private Integer add;
    private Integer delete;

    public Timestamp getChange_time() {
        return change_time;
    }

    public void setChange_time(Timestamp change_time) {
        this.change_time = change_time;
    }

    public String getOldtype() {
        return oldtype;
    }

    public void setOldtype(String oldtype) {
        this.oldtype = oldtype;
    }

    public String getNewtype() {
        return newtype;
    }

    public void setNewtype(String newtype) {
        this.newtype = newtype;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public Integer getChange() {
        return change;
    }

    public void setChange(Integer change) {
        this.change = change;
    }

    public Integer getAdd() {
        return add;
    }

    public void setAdd(Integer add) {
        this.add = add;
    }

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }
}
