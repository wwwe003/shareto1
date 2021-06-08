package com.wjc.user.controller;

import com.wjc.action.mapper.ActionMapper;
import com.wjc.action.pojo.Follow;
import com.wjc.admin.mapper.AdminMapper;
import com.wjc.pager.PageBean;
import com.wjc.post.pojo.Post;
import com.wjc.post.service.PostService;
import com.wjc.user.mapper.UserMapper;
import com.wjc.user.pojo.User;
import com.wjc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class myPage {
    @Autowired(required = false)
    UserService userService;
    @Autowired(required = false)
    UserMapper userMapper;
    @Autowired(required = false)
    PostService postService;
    @Autowired(required = false)
    ActionMapper actionMapper;
    @Autowired(required = false)
    private AdminMapper adminMapper;

    @GetMapping("{no}")
    public String personPage(@PathVariable String no, HttpServletRequest request, Model model){
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        if (sessionUser!=null){
            if (sessionUser.getUserno().equals(no)) {
                Integer followernum = sessionUser.getFollowernum();
                Integer totallikes = postService.findTotallikes(sessionUser.getUsername());
                String followerNum=followerChange(followernum);
                String totalLikes=followerChange(totallikes);
                model.addAttribute("pageUsername", sessionUser.getUsername());
                model.addAttribute("followerNum", followerNum);
                model.addAttribute("totalLikes", totalLikes);
            } else {
                String uid = userMapper.findUidByUserNo(no);
                User user = userMapper.findUserByUid(uid);
                String pageUsername = user.getUsername();
                model.addAttribute("pageUsername", pageUsername);

                Integer followernum =user.getFollowernum();
                String followerNum=followerChange(followernum);
                Integer totallikes = postService.findTotallikes(user.getUsername());
                String totalLikes=followerChange(totallikes);
                model.addAttribute("followerNum", followerNum);
                model.addAttribute("totalLikes", totalLikes);
                Follow follow = actionMapper.ifFollow(uid, sessionUser.getUid());
                if (follow == null) {
                    model.addAttribute("followState", 0);
                } else {
                    model.addAttribute("followState", 1);
                }
            }
            int message_count = adminMapper.historyUnreadCount(sessionUser.getUsername());
            model.addAttribute("message",message_count);
        }else {
            String uid = userMapper.findUidByUserNo(no);
            User user = userMapper.findUserByUid(uid);
            String pageUsername = user.getUsername();
            model.addAttribute("pageUsername", pageUsername);

            Integer followernum =user.getFollowernum();
            String followerNum=followerChange(followernum);
            Integer totallikes = postService.findTotallikes(user.getUsername());
            String totalLikes=followerChange(totallikes);
            model.addAttribute("followerNum", followerNum);
            model.addAttribute("totalLikes", totalLikes);

            model.addAttribute("followState", 0);

        }

        return "/toshare/user/myPage";
    }
    public String followerChange(Integer followernum){
        if (followernum>=1000) {
            double followerNum=followernum.doubleValue();
            followerNum = (Math.floor(followerNum / 100) / 10);
            return followerNum +"k";
        }else {
            return Integer.toString(followernum);
        }
    }
    @ResponseBody
    @PostMapping("/totallikes")
    public PageBean<Post> showTotallikes(String currentPage, String userno){
        int pageCode=Integer.parseInt(currentPage);
        return postService.findTotallikesPost(userno, pageCode);
    }
    @ResponseBody
    @PostMapping("/totallikesLow")
    public PageBean<Post> showTotallikesLow(String currentPage, String userno){
        int pageCode=Integer.parseInt(currentPage);

        return postService.findTotallikesPostLow(userno, pageCode);
    }
    @ResponseBody
    @PostMapping("/timeline")
    public PageBean<Post> showTimeline(String currentPage, String userno){
        int pageCode=Integer.parseInt(currentPage);
        return postService.findTimeLinePost(userno, pageCode);
    }
    @ResponseBody
    @PostMapping("/dynamic")
    public PageBean<Post> showDynamic(String currentPage, String userno){
            int pageCode=Integer.parseInt(currentPage);
            return postService.findDynamicPost(userno, pageCode);
    }
    @ResponseBody
    @PostMapping("/favorite")
    public PageBean<Post> showFavorite(String currentPage, String userno, HttpServletRequest request){
        User sessionUser = (User)request.getSession().getAttribute("sessionUser");
        int pageCode=Integer.parseInt(currentPage);
        //String sessionUseruid=sessionUser.getUid();

        if (sessionUser!=null) {
            String sessionUseruid = sessionUser.getUid();
            return postService.findFavoritePost(userno, pageCode, sessionUseruid);
        }else {
            return postService.findFavoritePost(userno, pageCode, null);
        }
        //return postService.findFavoritePost(userno, pageCode, sessionUseruid);
    }
    @ResponseBody
    @PostMapping("/follower")
    public PageBean<User> showFollower(String currentPage,String userno,HttpServletRequest request){
        User sessionUser = (User)request.getSession().getAttribute("sessionUser");
        int pageCode=Integer.parseInt(currentPage);

        if (sessionUser!=null) {
            String sessionUseruid = sessionUser.getUid();
            return userService.findFollowerUser(userno, pageCode, sessionUseruid);
        }else {
            return userService.findFollowerUser(userno, pageCode, null);
        }
    }
    @ResponseBody
    @PostMapping("/following")
    public PageBean<User> showFollow(String currentPage,String userno,HttpServletRequest request){
        User sessionUser = (User)request.getSession().getAttribute("sessionUser");
        int pageCode=Integer.parseInt(currentPage);

        if (sessionUser!=null) {
            String sessionUseruid = sessionUser.getUid();
            return userService.findFollowingUser(userno, pageCode, sessionUseruid);
        }else {
            return userService.findFollowingUser(userno, pageCode, null);
        }
    }
}
