package com.wjc.admin.controller;

import com.wjc.admin.pojo.Review_message;
import com.wjc.admin.service.AdminService;
import com.wjc.exception.ArticleNotFoundException;
import com.wjc.pager.PageBean;
import com.wjc.post.pojo.Post;
import com.wjc.post.service.PostService;
import com.wjc.type.mapper.TypeMapper;
import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;
import com.wjc.type.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ArticleManage {
    private static final String PAGE_CODE_DEFAULT = "1";
    private static final String SUBTYPE_ID = "1";
    @Autowired
    private AdminService adminService;
    @Autowired
    private TypeService typeService;
    @Autowired(required = false)
    private TypeMapper typeMapper;

    @GetMapping("/article")
    public String findAllArticle(Model model,
                                 @RequestParam(value = "currentPage",required = false,defaultValue = PAGE_CODE_DEFAULT)String currentPage,
                                 @RequestParam(value = "subtypeID",required = false,defaultValue = SUBTYPE_ID)String subtypeID) {
        Map<Type_first, List<Type_second>> allTypeAndCount = adminService.findAllTypeAndCount();
        model.addAttribute("allType",allTypeAndCount);
//        Map<String, List<String>> allType = typeService.findAllType();
//        model.addAttribute("allType",allType);

        int pageCode=Integer.parseInt(currentPage);
        PageBean<Post> postsBySubtypeID = adminService.findPostsBySubtypeID(Integer.parseInt(subtypeID), pageCode);
        model.addAttribute("postsBySubtypeID",postsBySubtypeID);
        return "/toshare/admin/adminArticles";
    }
    @ResponseBody
    @PostMapping("/articlePG")
    public PageBean<Post> articlePG(String currentPage,String subtypeID){
        return adminService.findPostsBySubtypeID(Integer.parseInt(subtypeID),Integer.parseInt(currentPage));
    }
    @ResponseBody
    @PostMapping("/typeMap")
    public Map<String, List<String>> typeMap(){
        return typeService.findAllType();
    }

    @PostMapping("/changeType")
    public String changeType(Review_message reviewMessage,String supertype,String subtype,String passed_article){
        reviewMessage.setNewtype(supertype+"("+subtype+")");

        if (!reviewMessage.getOldtype().equals(reviewMessage.getNewtype())) {
            adminService.changeType(reviewMessage);
        }
        if (passed_article.equals("1")){
            return "redirect:/admin/article";
        }else{
            return "redirect:/admin/review/"+reviewMessage.getPost_id();
        }

    }

    @ResponseBody
    @PostMapping("/showByType")
    public PageBean<Post> showByType(String subtype,
                                     @RequestParam(value = "currentPage",required = false,defaultValue = PAGE_CODE_DEFAULT)String currentPage){
        int pageCode=Integer.parseInt(currentPage);
        return adminService.findPostsBySubtypeID(typeMapper.find2ndID(subtype), pageCode);
    }
}
