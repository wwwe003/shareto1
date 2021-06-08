package com.wjc.type.mapper;

import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TypeMapper {
    List<Type_first> findType_first();

    //find all second type Object
    List<Type_second> findType_second();

    //find all second type Object by 1stName
    List<Type_second> findType_secondBy1stName(Integer ID);

    //find type_first_id by type_first_name
    Integer find1stID(@Param("firstName") String firstName);

    //find type_second_name by type_first_id
    List<String> find2ndName(Integer firstID);

    //find type_second_id by type_second_name
    Integer find2ndID(String secondName);

    //根据一个二级标题的名字查出其一级标题的ID
    Integer firstID(String name);

    //根据一个二级标题的一级ID查出所有相同一级ID的二级标题
    List<String> find2ndNamelist(Integer ID);



    //根据一个一级标题的ID获取一级标题的Name
    String find1stTypeNameBy1stTypeID(Integer firstID);

    String find2ndNameBy1stID(Integer type1Id);
}
