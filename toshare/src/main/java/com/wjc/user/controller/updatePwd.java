package com.wjc.user.controller;

import com.wjc.user.pojo.User;
import com.wjc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class updatePwd {
    @Autowired
    private UserService userService;

    @PostMapping("/password")
    public String update(HttpServletRequest req, User user, Map<String,Object> map) {
        User sessionUser = (User) req.getSession().getAttribute("sessionUser");
        String oldPwd=user.getPassword();
        String newPwd=user.getNewPwd();
        //System.out.println(sessionUser.getUid());
        //old password validation
        boolean b = userService.validateOldPwd(sessionUser.getUid(), oldPwd);
        if (!b){
            map.put("failMsg","現在のパスワードが間違っています。");
        }else if (newPwd == null || newPwd.trim().isEmpty()) {
            map.put("failMsg", "新しいパスワードを入力してください。");
        }else if(newPwd.length() < 6 || newPwd.length() > 20) {
            map.put("failMsg", "6から20までのパスワードを入力してください。");
        }else {
            userService.updatePassword(sessionUser.getUid(), newPwd);
            map.put("successMsg", "パスワードを変更されました。");
        }
//        System.out.println(sessionUser.getPassword());
        return "toshare/user/updatePwd";
    }
}
