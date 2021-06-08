package com.wjc.type.service;

import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;

import java.util.List;
import java.util.Map;

public interface TypeService {

    // find all type
    Map<String,List<String>> findAllType();


    //find first type when load main.html
    List<Type_first> find1stType();


    //find type_second_name by type_first_name
    List<String> find2ndNameBy1stName(String firstName);

//    List<String> find2ndNames(String secondName);


    //find type_second by type_first_name
    List<Type_second> find2ndType(String firstName);

    //find type_second by type_first_id
    List<Type_second> find2ndTypeByID(String firstID);

    //通过一个二级标题找到所有同类二级标题
    List<Type_second> findAllSame2ndTypeByOne2ndName(String secondName);


    //通过一个二级标题找到其所属的一级标题的名称
    String find1stTypeNameBy2ndTypeName(String secondName);
}
