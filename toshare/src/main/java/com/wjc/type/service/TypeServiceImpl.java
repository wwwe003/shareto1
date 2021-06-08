package com.wjc.type.service;

import com.wjc.type.mapper.TypeMapper;
import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional

public class TypeServiceImpl implements TypeService {
        final
        TypeMapper typeMapper;

        @Autowired(required = false)
        public TypeServiceImpl(TypeMapper typeMapper) {
            this.typeMapper = typeMapper;
        }
//    @Autowired
//    private TypeMapper typeMapper;

//
//    @Autowired
//    public void setTypeMapper (TypeMapper typeMapper) {
//        this.typeMapper = typeMapper;
//    }
//    @Resource
//    TypeMapper typeMapper;
    
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
//    public List<Type_first> findAllType() {
//        List<Type_first> parents = typeMapper.findType_first();
//        List<Type_second> type_second = typeMapper.findType_second();
//
//        for(Type_first parent:parents){
//            List<Type_second> children = new ArrayList<>();
//            for(Type_second child:type_second){
//            if (parent.getType_first_id().equals(child.getT_first_id())){
//                children.add(child);
//                }
//            }
//            parent.setChildren(children);
//        }
//        return parents;
//    }
    public Map<String,List<String>> findAllType() {
            Map<String,List<String>> typeSeconds=new LinkedHashMap<>();
        List<Type_first> parents = typeMapper.findType_first();
        List<Type_second> type_second = typeMapper.findType_second();
        for(Type_first parent:parents){
            List<String> type2 = new ArrayList<>();
            for(Type_second child:type_second){
            if (parent.getType_first_id().equals(child.getT_first_id())){
                type2.add(child.getType_second_name());
                }
            }
            typeSeconds.put(parent.getType_first_name(),type2);
        }
        return typeSeconds;
    }

    @Override
    public List<Type_first> find1stType() {
        return typeMapper.findType_first();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<String> find2ndNameBy1stName(String firstName) {
        Integer firstID = typeMapper.find1stID(firstName);
        return typeMapper.find2ndName(firstID);
    }

    //传入一个一级标题名字，得到该标题下的二级标题对象
    @Override
    public List<Type_second> find2ndType(String firstName) {
        Integer ID = typeMapper.find1stID(firstName);
        return typeMapper.findType_secondBy1stName(ID);
    }


    //传入一个一级标题ID，得到该标题下的二级标题对象
    @Override
    public List<Type_second> find2ndTypeByID(String firstID) {
            Integer ID=Integer.valueOf(firstID);
           return typeMapper.findType_secondBy1stName(ID);
    }

    @Override
    public List<Type_second> findAllSame2ndTypeByOne2ndName(String secondName) {
        Integer firstID = typeMapper.firstID(secondName);
        return typeMapper.findType_secondBy1stName(firstID);
    }

    @Override
    public String find1stTypeNameBy2ndTypeName(String secondName) {
        Integer firstID = typeMapper.firstID(secondName);
        return typeMapper.find1stTypeNameBy1stTypeID(firstID);
    }


}
