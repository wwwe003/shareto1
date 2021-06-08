package com.wjc.user.controller;

import com.wjc.user.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class PageController {

    @GetMapping("/registration")
    public String toRsgister(User user){
        return "toshare/user/regist";
    }//将一个user对象注入modelandview,便于回显信息

    @GetMapping("/login")
    public String toLogin(User user, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if (session.getAttribute("sessionUser")!=null){
            return "redirect:/main";
        }
//        else if (session.getAttribute("sessionAdmin")!=null){
////            Map<String, String> errors = new HashMap<>();
////            errors.put("logout","Please logout the admin account before login the user page.");
////            model.addAttribute("errmsg", errors);
//            return "/toshare/user/login";
//        }
        return "toshare/user/login";
    }

//    @GetMapping("/main")
//    public String toMain(){return "toshare/main";}

//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request){
//        request.getSession().removeAttribute("sessionUser");
//        return "redirect:/user/login";
//    }

    @GetMapping("/password")
    public String updatePwd(){
        return "toshare/user/updatePwd";
    }
}
