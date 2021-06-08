package com.wjc.action.controller;

import com.wjc.action.pojo.AjaxResponse;
import com.wjc.action.service.ActionService;
import com.wjc.post.service.PostService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@Controller

public class changeLike {

    @Autowired(required = false)
    ActionService actionService;
    @Autowired(required = false)
    PostService postService;
    @PostMapping("/like")
    @ResponseBody
    @RequiresRoles("user")
    public AjaxResponse likePost(@RequestParam("like_postid") String like_postid, @RequestParam("like_uid") String like_uid, @RequestParam("update_time") String update_time) throws IOException {

        Timestamp up_time=Timestamp.valueOf(update_time);
        boolean likeState = actionService.updateLike(like_postid, like_uid, up_time);
        Integer likeNum = postService.findLikeNumByPid(like_postid);

        AjaxResponse ajaxResponse=new AjaxResponse();
        ajaxResponse.setCurrentState(likeState);
        ajaxResponse.setLikeNum(likeNum);
        return ajaxResponse;
    }
}
