package com.wjc.post.controller;

import com.wjc.admin.mapper.AdminMapper;
import com.wjc.exception.ArticleNotFoundException;
import com.wjc.pager.PageBean;
import com.wjc.post.pojo.Comment;
import com.wjc.post.pojo.Post;
import com.wjc.post.service.PostService;
import com.wjc.user.pojo.User;
import com.wjc.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class postShow {
    private static final String PAGE_CODE_DEFAULT = "1";
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private AdminMapper adminMapper;

    @GetMapping("{pid}")
    public String showPost(@PathVariable String pid, Model model, HttpServletRequest request){
        Post postByID = postService.findPostByID(pid);
        if (postByID==null){
            throw new ArticleNotFoundException("Article may not exist or is not posted yet.");
        }
        model.addAttribute("postDetails",postByID);
        //estimate whether follow
        //先通过author找到uid，在查follow表，follow_uid找follower_uid；
        //follower_uid保存到model
        //前端判断是否与当前用户的uid一致
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        if (sessionUser!=null){
            Boolean followState = postService.followState(postByID.getAuthor(), sessionUser.getUid());
    //        System.out.println(followState);
            Boolean likeState = postService.likeState(postByID.getPost_id(), sessionUser.getUid());
            Boolean favoriteState = postService.favoriteState(postByID.getPost_id(), sessionUser.getUid());

            model.addAttribute("followState",followState);
            model.addAttribute("likeState",likeState);
            model.addAttribute("favoriteState",favoriteState);

            int message_count = adminMapper.historyUnreadCount(sessionUser.getUsername());
            model.addAttribute("message",message_count);
        }else {
            model.addAttribute("followState",false);
            model.addAttribute("likeState",false);
            model.addAttribute("favoriteState",false);
        }
        //find numbers of follower like favorite
        Integer followNum=userService.findFollowNumByPid(pid);
        Integer likeNum=postService.findLikeNumByPid(pid);
        Integer favoriteNum=postService.findFavoriteNumByPid(pid);
        model.addAttribute("followNum",followNum);
        model.addAttribute("likeNum",likeNum);
        model.addAttribute("favoriteNum",favoriteNum);

        model.addAttribute("comments",postService.listCommentByBlogId(pid));
        model.addAttribute("commentCount",postService.commentCount(pid));



        return "/toshare/article";
    }
    @GetMapping("/comments/{postID}")
    public String comments(@PathVariable String postID, Model model){
        //System.out.println(postID);
        model.addAttribute("comments",postService.listCommentByBlogId(postID));
        //model.addAttribute("comments","这是评论");
        model.addAttribute("commentCount",postService.commentCount(postID));
        return "/toshare/article :: commentList";
    }
    @PostMapping("/comments")
//    @RequiresRoles("user")
    public String saveComments(Comment comment,String author,String authorno){
//        System.out.println(comment.getUsername());
//        System.out.println(comment.getContent());
//        System.out.println(comment.getParent_id());
//        System.out.println(author);
//        System.out.println(authorno);
//        System.out.println(comment.getUsername());
//        System.out.println(comment.getUserno());
        if (comment.getUsername().equals(author)&&comment.getUserno().equals(authorno)){
            comment.setAuthor_comment(1);
        }else {
            comment.setAuthor_comment(0);
        }
        postService.saveComment(comment);
        return "redirect:/comments/"+comment.getPost_id();
    }
    @PostMapping("/search")
    public String search(String keyword,String limit,Model model){
        model.addAttribute("result",postService.findPostByKeyword(limit,keyword,1));
        model.addAttribute("keyword",keyword);
        model.addAttribute("search",limit);
        return "/toshare/search";
    }
    @ResponseBody
    @PostMapping("/searchPG")
    public PageBean<Post> searchPG(@RequestParam(value = "currentPage",required = false,defaultValue = PAGE_CODE_DEFAULT)String currentPage, String keyword, String limit){
        Integer pageCode=Integer.parseInt(currentPage);
        return postService.findPostByKeyword(limit,keyword,pageCode);
    }
//    @ResponseBody
//    @RequiresRoles("user")
//    @PostMapping("/ifLogin")
//    public String ifLogin(){
//        return "login";
//    }
}