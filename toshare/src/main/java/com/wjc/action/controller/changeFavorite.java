package com.wjc.action.controller;

import com.wjc.action.pojo.AjaxResponse;
import com.wjc.action.service.ActionService;
import com.wjc.post.service.PostService;
import com.wjc.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;

@Controller

public class changeFavorite {

    @Autowired(required = false)
    ActionService actionService;
    @Autowired(required = false)
    PostService postService;
    @PostMapping("/favorite")
    @ResponseBody
    @RequiresRoles("user")
    public AjaxResponse favoritePost(@RequestParam("favorite_postid") String favorite_postid, @RequestParam("favorite_uid") String favorite_uid, @RequestParam("update_time") String update_time){

        Timestamp up_time=Timestamp.valueOf(update_time);

        //先更新状态，再获得favoriteNum
        boolean state = actionService.updateFavorite(favorite_postid, favorite_uid, up_time);
        Integer favoriteNum = postService.findFavoriteNumByPid(favorite_postid);
        AjaxResponse ajaxResponse=new AjaxResponse();
        ajaxResponse.setCurrentState(state);
        ajaxResponse.setFavoriteNum(favoriteNum);

        return ajaxResponse;
    }
}