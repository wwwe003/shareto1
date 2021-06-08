package com.wjc.post.controller;

import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;
import com.wjc.type.service.TypeService;
import com.wjc.pager.PageBean;
import com.wjc.post.pojo.Post;
import com.wjc.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostPaginationController {
    private static final String PAGE_CODE_DEFAULT = "1";
    @Autowired
    private PostService postService;
    @Autowired
    private TypeService typeService;

    @GetMapping("/type/{secondName}")
    public String getPostBy2ndType(@PathVariable(value = "secondName",required = false) String secondName, @RequestParam(value = "currentPage",required = false,defaultValue = PAGE_CODE_DEFAULT) String currentPage, Model model) {

        int pageCode=Integer.parseInt(currentPage);

        //分页显示某一种分类(二级标题)的文章
        PageBean<Post> PostBy2ndType = postService.findByType2nd(secondName,pageCode);
        model.addAttribute("PostBy2ndType",PostBy2ndType);

        List<Type_first> firstType = typeService.find1stType();
        model.addAttribute("type_first",firstType);

        List<Type_second> type_seconds = typeService.findAllSame2ndTypeByOne2ndName(secondName);
        model.addAttribute("type_second", type_seconds);

        String firstName = typeService.find1stTypeNameBy2ndTypeName(secondName);

        Map<String,String> allHref=new HashMap<>();
        allHref.put("All",firstName);
        model.addAttribute("allHref",allHref);
        return "/toshare/main";
    }
}
