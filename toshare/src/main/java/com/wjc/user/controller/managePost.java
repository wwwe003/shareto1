package com.wjc.user.controller;

import com.wjc.admin.mapper.AdminMapper;
import com.wjc.pager.PageBean;
import com.wjc.post.mapper.PostMapper;
import com.wjc.post.pojo.Post;
import com.wjc.post.service.PostService;
import com.wjc.type.pojo.Type_first;
import com.wjc.type.service.TypeService;
import com.wjc.user.pojo.User;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller

public class managePost {
    @Autowired(required = false)
    PostService postService;
    @Autowired(required = false)
    PostMapper postMapper;
    @Autowired(required = false)
    TypeService typeService;
    @Autowired(required = false)
    AdminMapper adminMapper;

    @GetMapping("/{username}/myPost")
    public String showMyPost(@PathVariable("username")String username, HttpServletRequest request, Model model){
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        int pageCode=1;
        PageBean<Post> myPosts = postService.findManagePost(sessionUser.getUserno(), pageCode);
        model.addAttribute("myPosts",myPosts);

        List<Type_first> typeFirsts = typeService.find1stType();
        model.addAttribute("typeFirsts",typeFirsts);
        model.addAttribute("sessionUser",sessionUser);
        model.addAttribute("newMessage",adminMapper.historyUnreadCount(username));
        return "/toshare/user/managePost";
    }

    @ResponseBody
    @PostMapping("/{username}/getType2")
    public Map<String,List<String>> typeSeconds(){
        return typeService.findAllType();
    }
    @ResponseBody
    @PostMapping("/{username}/getPost")
    public PageBean<Post> getPostByType(String typeName1, String typeName2,int pageCode,HttpServletRequest request){
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        return postService.findPostByType(typeName1, typeName2, pageCode,sessionUser.getUserno());
    }
    @ResponseBody
    @PostMapping("/{username}/getPostByPC")
    public PageBean<Post> getPostByPageCode(String currentPage,HttpServletRequest request){
        int pageCode=Integer.parseInt(currentPage);
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        return postService.findManagePost(sessionUser.getUserno(), pageCode);
    }

    @ResponseBody
    @PostMapping("/{username}/deleted")
    public PageBean<Post> deletedArticle(String currentPage,HttpServletRequest request){
        int pageCode=Integer.parseInt(currentPage);
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        //        for (Post post:deletedArticle.getBeanList()){
//           if (post.getType_second_name()==null){
//               post.setType_second_name("deleted type");
//           }if (post.getType_first_name()==null){
//               post.setType_first_name("deleted type");
//            }
//        }
        return postService.findDeletedArticle(sessionUser.getUserno(), pageCode);
    }
    @ResponseBody
    @PostMapping("/{username}/draft")
    public PageBean<Post> draftArticle(String currentPage,HttpServletRequest request){
        int pageCode=Integer.parseInt(currentPage);
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        return postService.findDraftArticle(sessionUser.getUserno(), pageCode);
    }
    @ResponseBody
    @PostMapping("/{username}/underReview")
    public PageBean<Post> underReviewArticle(String currentPage,HttpServletRequest request){
        int pageCode=Integer.parseInt(currentPage);
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        return postService.findUnderReviewArticle(sessionUser.getUserno(), pageCode);
    }


    @PostMapping("/back")
    public String back(String username,String pid){
        postService.changeState(pid);
        return "redirect:/"+username+"/myPost";
    }
    @GetMapping("/{username}/completeDelete/{pid}")
    public String completeDelete(@PathVariable("username")String username, @PathVariable("pid")String pid, RedirectAttributes attributes){
        Post deletePost=postMapper.findPostByID(pid);
        String name=deletePost.getTitle();
        Integer id= postMapper.completeDelete(pid);
        if (id==1){
            attributes.addFlashAttribute("message","success");
            attributes.addFlashAttribute("message_tip","article \""+name+"\" is completely deleted");
        }else {
            attributes.addFlashAttribute("message","fail to post");
            attributes.addFlashAttribute("message_tip","article \""+name+"\" is not completely deleted");
        }
        return "redirect:/"+username+"/myPost";
    }

    @GetMapping("/{username}/message")
    public String message(@PathVariable("username")String username, @RequestParam(defaultValue = "1",required = false)String currentPage,Model model){
        Integer pageCode=Integer.parseInt(currentPage);
        PageBean<String> historyMessages=postService.historyUserArticle(username,pageCode);
        model.addAttribute("newMessage",adminMapper.historyUnreadCount(username));
        model.addAttribute("historyMessages",historyMessages);

        postMapper.unreadToRead(username);
        return "/toshare/user/message";
    }
}
