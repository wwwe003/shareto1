package com.wjc.action.controller;

import com.wjc.action.pojo.AjaxResponse;
import com.wjc.action.service.ActionService;
import com.wjc.user.mapper.UserMapper;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@Controller

public class changeFollow {

    @Autowired(required = false)
    ActionService actionService;
    @Autowired(required = false)
    UserMapper userMapper;

    @PostMapping("/follow")
    @ResponseBody
    @RequiresRoles("user")
    public AjaxResponse followAuthor(@RequestParam("author") String author, @RequestParam("follower_uid") String follower_uid,@RequestParam("update_time") String update_time){

        Timestamp up_time=Timestamp.valueOf(update_time);
        boolean followState = actionService.updateFollow(author, follower_uid, up_time);
        Integer followerNum = userMapper.findFollowNumByAuthor(author);

        AjaxResponse ajaxResponse=new AjaxResponse();
        ajaxResponse.setCurrentState(followState);
        ajaxResponse.setFollowerNum(followerNum);
        return ajaxResponse;
    }
}
