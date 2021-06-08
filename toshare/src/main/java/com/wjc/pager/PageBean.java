package com.wjc.pager;

import java.util.List;

public class PageBean<T> {
    private int pageCode;
    private int totalRecord;
    private int pageSize;
    private String url;
    private List<T> beanList;
    private List<String> followingUsername;

    public void setFollowingUsername(List<String> followingUsername) {
        this.followingUsername = followingUsername;
    }

    public List<String> getFollowingUsername() {
        return followingUsername;
    }

    public void setFollowingUser(List<String> followingUsername) {
        this.followingUsername = followingUsername;
    }

    //    public int getTp(){
//        int totalPage = totalRecord/pageSize;
//        return totalRecord % pageSize == 0 ? totalPage : totalPage + 1;
//    }
    public int getPageCode() {
        return pageCode;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<T> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<T> beanList) {
        this.beanList = beanList;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "pageCode=" + pageCode +
                ", totalRecord=" + totalRecord +
                ", pageSize=" + pageSize +
                ", url='" + url + '\'' +
                ", beanList=" + beanList +
                ", followingUsername=" + followingUsername +
                '}';
    }
}
