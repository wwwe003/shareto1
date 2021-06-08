package com.wjc.type.pojo;

public class Type_second {
    private Integer type_second_id;
    private String type_second_name;
    private Integer t_first_id;
    private Integer count;

    public Integer getType_second_id() {
        return type_second_id;
    }

    public void setType_second_id(Integer type_second_id) {
        this.type_second_id = type_second_id;
    }

    public String getType_second_name() {
        return type_second_name;
    }

    public void setType_second_name(String type_second_name) {
        this.type_second_name = type_second_name;
    }

    public Integer getT_first_id() {
        return t_first_id;
    }

    public void setT_first_id(Integer t_first_id) {
        this.t_first_id = t_first_id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Type_second{" +
                "type_second_id=" + type_second_id +
                ", type_second_name='" + type_second_name + '\'' +
                ", t_first_id=" + t_first_id +
                ", count=" + count +
                '}';
    }
}
