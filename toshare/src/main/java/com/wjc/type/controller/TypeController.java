package com.wjc.type.controller;

import com.wjc.admin.mapper.AdminMapper;
import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;
import com.wjc.type.service.TypeService;
import com.wjc.pager.PageBean;
import com.wjc.post.pojo.Post;
import com.wjc.post.service.PostService;
import com.wjc.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TypeController {
    private static final String PAGE_CODE_DEFAULT = "1";
    @Autowired
    private TypeService typeService;

    @Autowired
    private PostService postService;
    @Autowired(required = false)
    private AdminMapper adminMapper;

    @GetMapping("/main")
    public String find1stType(@RequestParam(value = "currentPage",defaultValue = PAGE_CODE_DEFAULT) String currentPage, Model model, HttpServletRequest request){
        List<Type_first> type_first = typeService.find1stType();
        model.addAttribute("type_first",type_first);

        int pageCode=Integer.parseInt(currentPage);
        PageBean<Post> beanList = postService.findAllPost(pageCode);
        model.addAttribute("allPost",beanList);

        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        if(sessionUser!=null) {
            int message_count = adminMapper.historyUnreadCount(sessionUser.getUsername());
            model.addAttribute("message", message_count);
        }
        return "/toshare/main";
    }

    @GetMapping("/type/share_{firstName}")
    public String find2ndType(@PathVariable(value = "firstName",required = false) String firstName, @RequestParam(value = "currentPage",required = false,defaultValue = PAGE_CODE_DEFAULT)String currentPage, Model model){

        List<Type_first> type_first = typeService.find1stType();
        model.addAttribute("type_first",type_first);

        //分页显示某一种分类(一级标题)的文章
        int pageCode=Integer.parseInt(currentPage);
        PageBean<Post> PostBy1stType = postService.findByType(firstName, pageCode);
        model.addAttribute("PostBy1stType",PostBy1stType);

        List<Type_second> type_seconds=typeService.find2ndType(firstName);

        model.addAttribute("type_second",type_seconds);

        Map<String,String> allHref=new HashMap<>();
        allHref.put("All",firstName);
        model.addAttribute("allHref",allHref);

        return "/toshare/main";
    }
}
