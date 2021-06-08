package com.wjc.user.controller;

import com.wjc.pager.PageBean;
import com.wjc.post.mapper.PostMapper;
import com.wjc.post.pojo.Post;
import com.wjc.post.service.PostService;
import com.wjc.type.mapper.TypeMapper;
import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;
import com.wjc.type.service.TypeService;
import com.wjc.user.pojo.User;
import com.wjc.utils.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

import static com.wjc.utils.MarkdownUtils.markdownToHtml;
import static com.wjc.utils.MarkdownUtils.markdownToHtmlExtensions;


@Controller
@RequestMapping("/{username}")
public class PostController {
    @Autowired(required = false)
    TypeService typeService;
    @Autowired(required = false)
    PostService postService;
    @Autowired(required = false)
    PostMapper postMapper;
    @Autowired(required = false)
    TypeMapper typeMapper;
    @GetMapping("/post/new")
    public String post(Model model){
        List<Type_first> typeFirsts = typeService.find1stType();
        model.addAttribute("typeFirsts",typeFirsts);
//        model.addAttribute("editPost",new Post());
        return "/toshare/user/doPost";
    }
    @PostMapping("/postArticle")
    public String postArticle(@RequestParam(required = false) String pid,@PathVariable("username")String username, Post post, Type_first type1, Type_second type2,
                              RedirectAttributes attributes, Model model, HttpServletRequest request,String allowChat,String allowTop){
//        System.out.println(post.getState());
        Map<String,String> errmsg = validatePost(post);
        if (errmsg.size()>0) {
            model.addAttribute("errmsg",errmsg);
            return "toshare/user/doPost";
        }
        post.setType(typeMapper.find1stID(type1.getType_first_name()));
        post.setType_second_id(typeMapper.find2ndID(type2.getType_second_name()));
        post.setAuthor(username);
        post.setHtml(markdownToHtmlExtensions(post.getMarkdown()));
        User sessionUser = (User)request.getSession().getAttribute("sessionUser");
        post.setUserno(sessionUser.getUserno());

        post.setLikes(0);
        post.setFavorites(0);

        if (allowChat==null){
            post.setAllow_chat(0);
        } else {
            post.setAllow_chat(Integer.valueOf(allowChat));
        }
        if (allowTop==null){
            post.setAllow_top(0);
        } else {
            post.setAllow_top(Integer.valueOf(allowTop));
        }

        if (post.getState()==0){
            post.setPost_id(pid);
            if (postService.saveDraft(post)){
                attributes.addFlashAttribute("message","success");
                attributes.addFlashAttribute("message_tip","article is saved in drafts");
            }else {
                attributes.addFlashAttribute("message","fail to post");
                attributes.addFlashAttribute("message_tip","article is not saved in drafts");
            }
        }else {
            boolean savePost = postService.savePost(post);
                if (savePost){
                    attributes.addFlashAttribute("message","success");
                    attributes.addFlashAttribute("message_tip","article is posted and wait review");
                }else {
                    attributes.addFlashAttribute("message","fail to post");
                    attributes.addFlashAttribute("message_tip","article is not posted");
                }
        }

        return "redirect:/"+username+"/myPost";
    }
    @GetMapping("/edit/{pid}")
    public String editArticle(@PathVariable String pid,Model model,@RequestParam(required = false) String mode){
        Post editPost=postService.findPostByID(pid);
        editPost.setType_first_name(typeMapper.find1stTypeNameBy1stTypeID(editPost.getType()));
        editPost.setType_second_name(typeMapper.find2ndNameBy1stID(editPost.getType_second_id()));
//        editPost.setStatus(0);
//        editPost.setDraft(1);
//        postMapper.changeDraft(pid);

        model.addAttribute("editPost",editPost);
        List<Type_first> typeFirsts = typeService.find1stType();
        model.addAttribute("typeFirsts",typeFirsts);
        model.addAttribute("scan",mode);
        return "toshare/user/doPost";
    }
    @GetMapping("/delete/{pid}")
    public String deleteArticle(@PathVariable String pid,HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("sessionUser");
        Date date=new Date();

        postMapper.changeDeleted(pid,new Timestamp(date.getTime()));
//        System.out.println("delete");
        return "redirect:/"+user.getUsername()+"/myPost";
    }



    private Map<String,String> validatePost(Post formPost) {
        Map<String,String> errors = new HashMap<>();

        String title = formPost.getTitle();
        if(title == null || title.trim().isEmpty()) {
            errors.put("title", "タイトルを入力してください。");
        }

        String content = formPost.getMarkdown();
        if(content == null || content.trim().isEmpty()) {
            errors.put("content", "contentを入力してください。");
        }

        String description = formPost.getDescription();
        if(description == null || description.trim().isEmpty()) {
            errors.put("description", "descriptionを入力してください。");
        }
        return errors;
    }


}
