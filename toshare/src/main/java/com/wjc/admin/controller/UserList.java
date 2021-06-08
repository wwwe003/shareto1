package com.wjc.admin.controller;

import com.wjc.admin.mapper.AdminMapper;
import com.wjc.admin.pojo.UserDetail;
import com.wjc.admin.service.AdminService;
import com.wjc.pager.PageBean;
import com.wjc.post.pojo.Post;
import com.wjc.post.service.PostService;
import com.wjc.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class UserList {
    @Autowired
    private AdminService adminService;
    @Autowired
    private PostService postService;
    @Autowired(required = false)
    private AdminMapper adminMapper;
    @GetMapping("/user")
    public String getUserList(Model model, @RequestParam(required = false,defaultValue = "1")String currentPage){
        Integer pageCode=Integer.parseInt(currentPage);
        model.addAttribute("userList", adminService.getUserList(pageCode));
        return "/toshare/admin/userList";
    }
    @GetMapping("/detail/{uno}")
    public String getUserDetail(@PathVariable("uno")String uno, Model model, @RequestParam(defaultValue = "1",required = false)String currentPage, HttpServletRequest request){
        int pageCode=Integer.parseInt(currentPage);

        Integer totallikes = adminService.findTotallikes(uno);
        model.addAttribute("totallikes",totallikes);
        model.addAttribute("postedArticles",postService.findTotallikesPost(uno, pageCode));
        model.addAttribute("userDetail",adminService.getUserDetail(uno));
        model.addAttribute("favoriteCount",adminMapper.getFavoriteCount(uno));
        return "/toshare/admin/userDetail";
    }
    @ResponseBody
    @PostMapping("/user/review")
    public PageBean<Post> reviewArticles(String uno,@RequestParam(defaultValue = "1",required = false)String currentPage){
        return adminService.reviewArticles(uno, Integer.parseInt(currentPage));
    }
    @ResponseBody
    @PostMapping("/user/favorite")
    public PageBean<Post> favoriteArticles(String uno,@RequestParam(defaultValue = "1",required = false)String currentPage){
        PageBean<Post> postPageBean = adminService.favoriteArticles(uno, Integer.parseInt(currentPage));
        for (Post post: postPageBean.getBeanList()){
            System.out.println(post.getUserno());
        }
        return adminService.favoriteArticles(uno, Integer.parseInt(currentPage));
    }
}
