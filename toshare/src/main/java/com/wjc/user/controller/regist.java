package com.wjc.user.controller;

import com.wjc.user.pojo.User;
import com.wjc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class regist {
    @Autowired
    private UserService userService;


    @PostMapping("/fusername")
    public String ajaxValidateUsername(@RequestParam("username") String username, HttpServletResponse response) throws IOException {
        boolean b= userService.ajaxValidateUsername(username);
        response.getWriter().println(b);
        return null;
    }

    @PostMapping("/femail")
    public String ajaxValidateEmail(@RequestParam("email") String email, HttpServletResponse response) throws IOException {
        boolean b=userService.ajaxValidateEmail(email);
        response.getWriter().println(b);
        return null;
    }


    //后端数据验证
    @PostMapping("/registration")
    public String register(User user, Model model,HttpSession session)
    {

        Map<String,String> errmsg = validateRegist(user);
        if (errmsg.size()>0) {
            model.addAttribute("errmsg",errmsg);
            return "toshare/user/regist";
        }
        try{
            session.setAttribute("sessionUser", userService.regist(user));
            return "redirect:/main";
        }catch (Exception e){
            e.printStackTrace();
            errmsg.put("","Registration failed with unknown error.Please try again.");
            model.addAttribute("errmsg",errmsg);
            return "toshare/user/regist";
        }

    }

    private Map<String,String> validateRegist(User formUser) {
        Map<String,String> errors = new HashMap<>();

        String username = formUser.getUsername();
        if(username == null || username.trim().isEmpty()) {
            errors.put("username", "ユーザー名を入力してください。");
        } else if(username.length() < 4 || username.length() > 20) {
            errors.put("username", "4から20までのユーザー名を入力してください。");
        } else if(!userService.ajaxValidateUsername(username)) {
            errors.put("username", "ユーザー名はすでに存在します。");
        }


        String password = formUser.getPassword();
        if(password == null || password.trim().isEmpty()) {
            errors.put("password", "パスワードを入力してください。");
        } else if(password.length() < 6 || password.length() > 20) {
            errors.put("password", "6から20までのパスワードを入力してください。");
        }


        String confirmPwd = formUser.getConfirmPwd();
        if(confirmPwd == null || confirmPwd.trim().isEmpty()) {
            errors.put("confirmPwd", "確認パスワードを入力してください。");
        } else if(!confirmPwd.equals(password)) {
            errors.put("confirmPwd", "入力されたパスワードが一致ではありません。");
        }


        String email = formUser.getEmail();
        if(email == null || email.trim().isEmpty()) {
            errors.put("email", "メールアドレスを入力してください。");
        } else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            errors.put("email", "正しいメールアドレスの形式を入力してください。");
        } else if(!userService.ajaxValidateEmail(email)) {
            errors.put("email", "メールアドレスはすでに存在します。");
        }

        return errors;
    }
}
