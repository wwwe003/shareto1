package com.wjc.type.pojo;

import java.util.List;

public class Type_first {
    private Integer type_first_id;
    private String type_first_name;

    private Integer count;
    private Type_first parent;
    private List<Type_second> children;

    public Integer getType_first_id() {
        return type_first_id;
    }

    public void setType_first_id(Integer type_first_id) {
        this.type_first_id = type_first_id;
    }

    public String getType_first_name() {
        return type_first_name;
    }

    public void setType_first_name(String type_first_name) {
        this.type_first_name = type_first_name;
    }

    public Type_first getParent() {
        return parent;
    }

    public void setParent(Type_first parent) {
        this.parent = parent;
    }

    public List<Type_second> getChildren() {
        return children;
    }

    public void setChildren(List<Type_second> children) {
        this.children = children;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Type_first{" +
                "type_first_id=" + type_first_id +
                ", type_first_name='" + type_first_name + '\'' +
                ", count=" + count +
                ", parent=" + parent +
                ", children=" + children +
                '}';
    }
}
