package com.wjc;

import com.wjc.admin.mapper.AdminMapper;
import com.wjc.post.mapper.PostMapper;
import com.wjc.post.pojo.Comment;
import com.wjc.post.service.PostService;
import com.wjc.type.pojo.Type_first;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class toshareApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostService postService;
    @Autowired
    private AdminMapper adminMapper;
    @Test
    public void findComByPIDTest(){

        List<Comment> comments = postMapper.findComByPIDtest(null);
        for (Comment item:comments){
            System.out.println(item);
        }
    }
    @Test
    public void findComByPID(){
        List<Comment> comments = postService.listCommentByBlogId("a5144653154516613456");
        for (Comment comment:comments){
            System.out.println("comment:"+comment);
        }
    }
    @Test
    public void supertypeAndPostCount(){
        List<Type_first> type_firsts=adminMapper.supertypeAndPostCount();
        for(Type_first type_first:type_firsts){
            System.out.println("supertype:"+type_first);
        }
    }

}
