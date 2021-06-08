package com.wjc.admin.service;

import com.wjc.admin.mapper.AdminMapper;
import com.wjc.admin.pojo.Admin;
import com.wjc.admin.pojo.Review_message;
import com.wjc.admin.pojo.TypeHistory;
import com.wjc.admin.pojo.UserDetail;
import com.wjc.pager.PageBean;
import com.wjc.pager.PageConstants;
import com.wjc.post.mapper.PostMapper;
import com.wjc.post.pojo.Post;
import com.wjc.type.mapper.TypeMapper;
import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;
import com.wjc.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired(required = false)
    private AdminMapper adminMapper;
    @Autowired(required = false)
    private TypeMapper typeMapper;
    @Autowired(required = false)
    private PostMapper postMapper;
    @Override
    public Admin login(Admin admin) {
        return adminMapper.findByAdminnameAndPassword(admin.getAdminname(), admin.getPassword());
    }

    @Override
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
    public Boolean addSupertype (Type_first type_first,String adminname) {
        Integer id1 = typeMapper.find1stID(type_first.getType_first_name());
        //System.out.println(id1);
        if (id1!=null){
            return false;
        }else {
            Integer id = adminMapper.addSupertype(type_first);
            Date date = new Date();
            adminMapper.addSupertypeHistory(type_first.getType_first_name(),new Timestamp(date.getTime()),adminname);
//            Integer id = adminMapper.addSupertype(type_first);
        //System.out.println(id);1
        return id>0;}
//        System.out.println(type_first.getType_first_id());
    }

    @Override
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
    public Boolean addSubtype(Type_second type_second,String adminname,String superTypeName) {
        Integer id2 = typeMapper.find2ndID(type_second.getType_second_name());
        if (id2!=null){
            return false;
        }else
        {
            Integer id = adminMapper.addSubtype(type_second);
            Date date = new Date();
            adminMapper.addSubtypeHistory(type_second.getType_second_name(),superTypeName,new Timestamp(date.getTime()),adminname);
            //Integer id = adminMapper.addSubtype(type_second);
            return id>0;}
    }

    @Override
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
    public Boolean editType(String oldSuperName,String currentTypeName,String typeName,Integer supOrSubType,String adminname) {
        if (supOrSubType==1){
            Integer id1 = typeMapper.find1stID(typeName);
            if (id1!=null){
                return false;
            }else {
                adminMapper.editSupertype(typeName,currentTypeName);
                Date date = new Date();
                adminMapper.editTypeHistory(currentTypeName,typeName,new Timestamp(date.getTime()),adminname);
                return true;
            }
        }else {
            Integer id2 = typeMapper.find2ndID(typeName);
            if (id2!=null){
                return false;
            }else {
                adminMapper.editSubtype(typeName,currentTypeName);
                Date date = new Date();
                adminMapper.editTypeHistory(oldSuperName+"("+currentTypeName+")",oldSuperName+"("+typeName+")",new Timestamp(date.getTime()),adminname);
                return true;
            }
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
    public Integer deleteType(String oldSuperName,String typeName, Integer supOrSubType,String adminname) {
        if (supOrSubType==1){
            Integer typeID=typeMapper.find1stID(typeName);
            Integer subtypeCount = adminMapper.findSubtypeCount(typeID);
            if (subtypeCount>0){
                return 1;
            }else {
                adminMapper.deleteSupertype(typeName);
                Date date = new Date();
                adminMapper.deleteTypeHistory(typeName,new Timestamp(date.getTime()),adminname);
            return 0;}
        }else {
            Integer typeID=typeMapper.find2ndID(typeName);
            Integer totalType_2Records = adminMapper.deleteSubtypeConfirm(typeID);
            if (totalType_2Records>0){
                return 2;
            }else {
                if (adminMapper.checkDraftOrTrash(typeID)>0){
                    adminMapper.changeDeletedSubtype(typeID);
                }
                adminMapper.deleteSubtype(typeName);
                Date date = new Date();
                adminMapper.deleteTypeHistory(oldSuperName + "(" + typeName + ")", new Timestamp(date.getTime()), adminname);
            return 0;}
        }
    }

    @Override
    public PageBean<Post> findPostsBySubtypeID(Integer id2, Integer pageCode) {
        int pageSize= PageConstants.POST_PAGE_SIZE;
        int totalRecords;

        totalRecords=postMapper.findTotalType_2Records(id2);

        List<Post> currentPosts = adminMapper.findCurrent2ndPosts(id2, (pageCode-1)*pageSize, pageSize);
        PageBean<Post> pb = new PageBean<>();
        pb.setBeanList(currentPosts);
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);
        pb.setTotalRecord(totalRecords);
        return pb;
    }

    @Override
    @Transactional(rollbackForClassName = {"RuntimeException","Exception"},propagation = Propagation.REQUIRED)
    public void changeType(Review_message reviewMessage) {
        String newSubtype=reviewMessage.getNewtype().substring(reviewMessage.getNewtype().indexOf("(")+1,reviewMessage.getNewtype().lastIndexOf(")"));
        String newSupertype=reviewMessage.getNewtype().substring(0,reviewMessage.getNewtype().lastIndexOf("("));
        Integer firstID = typeMapper.find1stID(newSupertype);
        Integer secondID = typeMapper.find2ndID(newSubtype);
        adminMapper.changeType(firstID,secondID,reviewMessage.getPost_id());

        reviewMessage.setChange_type(1);
        reviewMessage.setPass(0);
        reviewMessage.setDelete(0);
        reviewMessage.setNopass(0);
        reviewMessage.setState(0);

        Date date = new Date();
        reviewMessage.setUpdate_time(new Timestamp(date.getTime()));
        adminMapper.saveReviewMessage(reviewMessage);

    }

    @Override
    public PageBean<Post> showUnderReviewPosts(int state,int pageCode,String subtypeID) {
        int pageSize= PageConstants.POST_PAGE_SIZE;
        int totalRecords;

        PageBean<Post> pb = new PageBean<>();
        if (subtypeID==null){
            totalRecords=adminMapper.findTotalUnderReviewPostsRecords(state);
            pb.setTotalRecord(totalRecords);
            List<Post> underReviewPostList = adminMapper.showUnderReviewPosts(1,(pageCode-1)*pageSize,pageSize);
            pb.setBeanList(underReviewPostList);
        }else {
            totalRecords=adminMapper.findTotalRecordsBySubtype(state,Integer.parseInt(subtypeID));
            pb.setTotalRecord(totalRecords);
            List<Post> underReviewPostList = adminMapper.showReviewResultBySubtype(1,Integer.parseInt(subtypeID),(pageCode-1)*pageSize,pageSize);
            pb.setBeanList(underReviewPostList);
        }
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);

        return pb;
    }

    @Override
    public Map<Type_first, List<Type_second>> findAllReviewTypeAndCount() {
        Map<Type_first,List<Type_second>> allReviewTypeAndCount=new LinkedHashMap<>();
        List<Type_first> type_firsts=findSupertypeAndCount();
        List<Type_second> type_seconds=findSubtypeAndCount();


//        for (Type_second type_second:type_seconds){
//            System.out.println(type_second);
//        }
//        for (Type_first type_first:type_firsts){
//            System.out.println(type_first);
//        }
        for(Type_first parent:type_firsts){
            List<Type_second> type2 = new ArrayList<>();
            for(Type_second child:type_seconds){
                if (parent.getType_first_id().equals(child.getT_first_id())){
                    type2.add(child);
                }
            }
            allReviewTypeAndCount.put(parent,type2);
        }

        //List<Type_first> type_firsts1 = adminMapper.supertypeAndPostCount();
//        for (Type_first f:type_firsts1){
//            System.out.println("name:"+f.getType_first_name()+",id:"+f.getType_first_id()+",count:"+f.getCount());
//        }
//        for (Map.Entry<Type_first,List<Type_second>> linkedHashMap:allReviewTypeAndCount.entrySet()){
//            System.out.println(linkedHashMap.getKey()+"...");
//            for (Type_second type_second:linkedHashMap.getValue()){
//                System.out.println(type_second);
//            }
//        }
        return allReviewTypeAndCount;
    }

    @Override
    public Map<Type_first, List<Type_second>> findAllTypeAndCount() {
        List<Type_first> type_firsts=adminMapper.supertypeAndPostCount();
        List<Type_second> type_seconds=adminMapper.subtypeAndPostCount();


        Map<Type_first,List<Type_second>> allTypeAndCount=new LinkedHashMap<>();
        for(Type_first parent:type_firsts){
            List<Type_second> type2 = new ArrayList<>();
            for(Type_second child:type_seconds){
                if (parent.getType_first_id().equals(child.getT_first_id())){
                    type2.add(child);
                }
            }
            allTypeAndCount.put(parent,type2);
        }


//        for (Type_second s:type_seconds){
//            System.out.println("name:"+s.getType_second_name()+",id:"+s.getType_second_id()+",count:"+s.getCount());
//        }

        return allTypeAndCount;
    }

    @Override
    @Transactional(rollbackForClassName = {"RuntimeException","Exception"},propagation = Propagation.REQUIRED)
    public void saveReview(Review_message reviewMessage) {
        reviewMessage.setState(0);

        if (reviewMessage.getPass()==0){
            Date date = new Date();
            reviewMessage.setUpdate_time(new Timestamp(date.getTime()));
            if (reviewMessage.getDelete()==0){
                adminMapper.changeStateToDraft(0,1,reviewMessage.getPost_id());
                adminMapper.saveReviewNopass(reviewMessage);
            }if (reviewMessage.getDelete()==1) {
                //delete
                Integer id=adminMapper.deleteArticle(reviewMessage.getPost_id());
                System.out.println(id);
                adminMapper.saveReviewDelete(reviewMessage);
            }
        }if (reviewMessage.getPass()==1){
            adminMapper.changeState(2,reviewMessage.getPost_id());
            Date date = new Date();
            reviewMessage.setUpdate_time(new Timestamp(date.getTime()));
            adminMapper.saveReviewPass(reviewMessage);
        }
    }

    @Override
    public PageBean<String> history_article(Integer pageCode) {
        List<String> history_articles=new ArrayList<>();
        int pageSize=5;
        List<Review_message> reviewMessages=adminMapper.findArticleHistory((pageCode-1)*pageSize, pageSize);
        PageBean<String> pb=new PageBean<>();
        pb.setPageSize(pageSize);
        pb.setTotalRecord(adminMapper.historyCount());
        pb.setPageCode(pageCode);

        for (Review_message reviewMessage:reviewMessages){
            String time=reviewMessage.getUpdate_time().toString().substring(0,reviewMessage.getUpdate_time().toString().indexOf("."));
            String messagePrefix="<div class='input-group'>"+
                    "<h5><span class='text-primary'>"+reviewMessage.getAdminname()+"</span>";

            if (reviewMessage.getChange_type()==1){
                String messageSuffixes="<span class='operation'> changed</span> the article: <span class='text-danger'>\""+reviewMessage.getTitle()+"</span>(author: <span class='text-success'>"+reviewMessage.getAuthor()+"</span>)\""
                        +" from <span class='text-secondary'>"+reviewMessage.getOldtype()+"</span> to <span class='text-warning'>"+reviewMessage.getNewtype()+"</span> in <span class='text-info'>"+
                        time+"</span></h5></div>";
                history_articles.add(messagePrefix+messageSuffixes);
            }else if (reviewMessage.getPass()==1){
                String messageSuffixes="<span class='operation'> passed</span> the article: <span class='text-danger'>\""+reviewMessage.getTitle()+"</span>(author: <span class='text-success'>"+reviewMessage.getAuthor()+"</span>)\""
                        +"</span> in <span class='text-info'>"+time+"</span></h5></div>";
                history_articles.add(messagePrefix+messageSuffixes);
            }else if (reviewMessage.getNopass()==1){
                String messageSuffixes="<span class='operation'> didn't pass</span> the article: <span class='text-danger'>\""+reviewMessage.getTitle()+"</span>(author: <span class='text-success'>"+reviewMessage.getAuthor()+"</span>)\""
                        +"</span> in <span class='text-info'>"+time+"</span></h5></div>";
                history_articles.add(messagePrefix+messageSuffixes);
            }else if (reviewMessage.getDelete()==1){
                String messageSuffixes="<span class='operation'> deleted</span> the article: <span class='text-danger'>\""+reviewMessage.getTitle()+"</span>(author: <span class='text-success'>"+reviewMessage.getAuthor()+"</span>)\""
                        +"</span> in <span class='text-info'>"+time+"</span></h5></div>";
                history_articles.add(messagePrefix+messageSuffixes);
            }
        }
        pb.setBeanList(history_articles);
      return pb;
    }
    @Override
    public PageBean<String> history_type(Integer pageCode) {
        List<String> history_type=new ArrayList<>();
        int pageSize=5;
        List<TypeHistory> typeHistorys=adminMapper.findTypeHistory((pageCode-1)*pageSize, pageSize);
        PageBean<String> pb=new PageBean<>();
        pb.setPageSize(pageSize);
        pb.setTotalRecord(adminMapper.historyTypeCount());
        pb.setPageCode(pageCode);

        for (TypeHistory typeHistory:typeHistorys){
            String time=typeHistory.getChange_time().toString().substring(0,typeHistory.getChange_time().toString().indexOf("."));
            String messagePrefix="<div class='input-group'>"+
                    "<h5><span class='text-primary'>"+typeHistory.getAdminname()+"</span>";
            if (typeHistory.getAdd()==1){
                String messageSuffixes;
                if (typeHistory.getOldtype()==null){
                    messageSuffixes = "<span class='operation'> added</span> the supertype: <span class='text-warning'>\"" + typeHistory.getNewtype() + "\"</span>"
                            + " in <span class='text-info'>" + time + "</span></h5></div>";
                }else {
                    messageSuffixes = "<span class='operation'> added</span> the subtype: <span class='text-primary'>\"" + typeHistory.getNewtype() + "\"</span>"
                            + " in the supertype: <span class='text-warning'>\"" + typeHistory.getOldtype() + "\"</span> in <span class='text-info'>" + time + "</span></h5></div>";
                }
                history_type.add(messagePrefix+messageSuffixes);
            }else if (typeHistory.getChange()==1){
                String messageSuffixes;
                if (!typeHistory.getOldtype().contains("(")){
                    messageSuffixes = "<span class='operation'> changed</span> the supertype: <span class='text-secondary'>\"" + typeHistory.getOldtype() + "\"</span>"
                            + " to <span class='text-warning'>\""+typeHistory.getNewtype()+"\"</span> in <span class='text-info'>" + time + "</span></h5></div>";
                }else {
                    String oldtype=typeHistory.getOldtype().substring(typeHistory.getOldtype().indexOf("(")+1,typeHistory.getOldtype().indexOf(")"));
                    String newtype=typeHistory.getNewtype().substring(typeHistory.getNewtype().indexOf("(")+1,typeHistory.getNewtype().indexOf(")"));
                    String supertype=typeHistory.getOldtype().substring(0,typeHistory.getOldtype().indexOf("("));
                    messageSuffixes = "<span class='operation'> changed</span> the subtype: <span class='text-secondary'>\"" + oldtype + "\"</span>"
                            + " to <span class='text-primary'>\""+newtype+"\"</span> in the supertype: <span class='text-warning'>\"" + supertype + "\"</span> in <span class='text-info'>" + time + "</span></h5></div>";
                }
                history_type.add(messagePrefix+messageSuffixes);
            }else if (typeHistory.getDelete()==1){
                String messageSuffixes;
                if (!typeHistory.getOldtype().contains("(")){
                    messageSuffixes = "<span class='operation'> deleted</span> the supertype: <span class='text-danger'>\"" + typeHistory.getOldtype() + "\"</span>"
                            + " in <span class='text-info'>" + time + "</span></h5></div>";
                }else {
                    String oldtype=typeHistory.getOldtype().substring(typeHistory.getOldtype().indexOf("(")+1,typeHistory.getOldtype().indexOf(")"));
                    String supertype=typeHistory.getOldtype().substring(0,typeHistory.getOldtype().indexOf("("));
                    messageSuffixes = "<span class='operation'> deleted</span> the subtype: <span class='text-danger'>\"" + oldtype + "\"</span>"
                            + " in the supertype: <span class='text-warning'>\"" + supertype + "\"</span> in <span class='text-info'>" + time + "</span></h5></div>";
                }
                history_type.add(messagePrefix+messageSuffixes);
            }
        }
        pb.setBeanList(history_type);
        return pb;
    }

    @Override
    public PageBean<User> getUserList(Integer pageCode) {
        int pageSize= PageConstants.POST_PAGE_SIZE;

        PageBean<User> pb = new PageBean<>();
        pb.setTotalRecord(adminMapper.getUserListCount());
        pb.setBeanList(adminMapper.getUserList((pageCode-1)*pageSize,pageSize));
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);

        return pb;
    }

    @Override
    public UserDetail getUserDetail(String uno) {
        UserDetail userDetail = adminMapper.getUserDetail(uno);
        if (userDetail.getPostedCount()==null){
            userDetail.setPostedCount(0);
        }if (userDetail.getUnderReviewCount()==null){
            userDetail.setUnderReviewCount(0);
        }
        return userDetail;
    }

    @Override
    public Integer findTotallikes(String uno) {
        int num=0;
        List<Integer> totallikes = adminMapper.findTotallikes(uno);
        for (int i:totallikes){
            num=num+i;
        }
        return num;
    }

    @Override
    public PageBean<Post> reviewArticles(String uno,Integer pageCode) {
        int pageSize= PageConstants.USER_PAGE_SIZE;

        PageBean<Post> pb = new PageBean<>();
        pb.setTotalRecord(adminMapper.getReviewArticlesCount(uno));
        pb.setBeanList(adminMapper.getReviewArticles(uno,(pageCode-1)*pageSize,pageSize));
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);

        return pb;
    }

    @Override
    public PageBean<Post> favoriteArticles(String uno,Integer pageCode) {
        int pageSize= PageConstants.USER_PAGE_SIZE;

        PageBean<Post> pb = new PageBean<>();
        pb.setTotalRecord(adminMapper.getFavoriteCount(uno));
        pb.setBeanList(adminMapper.getFavoriteArticles(uno,(pageCode-1)*pageSize,pageSize));
        pb.setPageCode(pageCode);
        pb.setPageSize(pageSize);

        return pb;
    }


    private List<Type_first> findSupertypeAndCount() {
        List<HashMap<String, Object>> supertypeAndCount = adminMapper.findSupertypeAndCount();
        List<Type_first> type_firsts=new ArrayList<>();
        if (supertypeAndCount!=null && !supertypeAndCount.isEmpty()){
            for (HashMap<String, Object> hashMap:supertypeAndCount){
                Type_first type_first = new Type_first();
                for (Map.Entry<String,Object> entry: hashMap.entrySet()){
                    if (entry.getKey().equals("count(*)")){
                        type_first.setCount(((Long)entry.getValue()).intValue());
                    }else if (entry.getKey().equals("type_first_name")){
                        type_first.setType_first_name((String) entry.getValue());
                    }else {
                        type_first.setType_first_id((Integer)entry.getValue());
                    }
                }type_firsts.add(type_first);
            }
        }
        return type_firsts;
    }

    private List<Type_second> findSubtypeAndCount() {
        List<HashMap<String, Object>> subtypeAndCount = adminMapper.findSubtypeAndCount();
        List<Type_second> type_seconds=new ArrayList<>();
        if (subtypeAndCount!=null && !subtypeAndCount.isEmpty()){
            for (HashMap<String, Object> hashMap:subtypeAndCount){
                Type_second type_second = new Type_second();
                for (Map.Entry<String,Object> entry: hashMap.entrySet()){
                    switch (entry.getKey()) {
                        case "count(*)":
                            type_second.setCount(((Long) entry.getValue()).intValue());
                            break;
                        case "type_second_name":
                            type_second.setType_second_name((String) entry.getValue());
                            break;
                        case "t_first_id":
                            type_second.setT_first_id((Integer) entry.getValue());
                            break;
                        default:
                            type_second.setType_second_id((Integer) entry.getValue());
                            break;
                    }
                }type_seconds.add(type_second);
            }
        }
        return type_seconds;
    }
}
