package com.wjc.admin.controller;

import com.wjc.admin.mapper.AdminMapper;
import com.wjc.admin.pojo.Review_message;
import com.wjc.admin.service.AdminService;
import com.wjc.pager.PageBean;
import com.wjc.post.pojo.Post;
import com.wjc.post.service.PostService;
import com.wjc.type.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ReviewManage {
    @Autowired
    private AdminService adminService;
    @Autowired(required = false)
    private AdminMapper adminMapper;
    @Autowired
    private TypeService typeService;

    @GetMapping("/review")
    public String showUnderReviewPosts(Model model,@RequestParam(required = false)String supertypeID){
        PageBean<Post> underReviewPostList=adminService.showUnderReviewPosts(1,1,supertypeID);
//        Map<String, List<String>> allType = typeService.findAllType();
//        model.addAttribute("allType",allType);
        model.addAttribute("underReviewPostList",underReviewPostList);

        model.addAttribute("needReview",adminService.findAllReviewTypeAndCount());
        return "/toshare/admin/adminReview";
    }
    @ResponseBody
    @PostMapping("/ajaxUnderReviewPosts")
    public PageBean<Post> ajaxUnderReviewPosts(String currentPage, @RequestParam(required = false)String subtypeID){
        return adminService.showUnderReviewPosts(1,Integer.parseInt(currentPage),subtypeID);
    }



    @GetMapping("/review/{pid}")
    public String review(@PathVariable("pid") String pid,Model model,@RequestParam(required = false) String mode){
        model.addAttribute("reviewAgain",mode);
        model.addAttribute("ReviewArticle",adminMapper.articleByPostID(pid));
        return "/toshare/admin/Article";
    }
    @PostMapping("/review/result")
    public String reviewResult(Review_message reviewMessage, RedirectAttributes attributes,String passed_article,String nopassReason,String deleteReason){
        if (nopassReason.isEmpty()){
            reviewMessage.setReason(deleteReason);
        }else {
            reviewMessage.setReason(nopassReason);
        }
        adminService.saveReview(reviewMessage);

        if (reviewMessage.getPass()==1){
            attributes.addFlashAttribute("success","success");
            attributes.addFlashAttribute("result_0","article: "+reviewMessage.getTitle()+" has been passed.");
        } else if (reviewMessage.getDelete()==1){
            attributes.addFlashAttribute("success","success");
            attributes.addFlashAttribute("result_1","article: "+reviewMessage.getTitle()+" has been deleted.");
        }else {
            attributes.addFlashAttribute("success","success");
            attributes.addFlashAttribute("result_1","article: "+reviewMessage.getTitle()+" didn't pass.");
        }

        if (passed_article.equals("1")){
            return "redirect:/admin/article";
        }else{
            return "redirect:/admin/review";
        }
    }
}
