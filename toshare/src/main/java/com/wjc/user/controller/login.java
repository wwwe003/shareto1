package com.wjc.user.controller;

import com.wjc.user.mapper.UserMapper;
import com.wjc.user.pojo.User;
import com.wjc.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class login {
    @Autowired(required = false)
    UserMapper userMapper;
    @PostMapping("/login")
    public String userLogin(User user, Model model, HttpServletResponse resp, HttpServletRequest request,HashMap<String,String> map) throws UnsupportedEncodingException {
        HttpSession session = request.getSession();
        /*
         * user not null validation
         */
        Map<String, String> errors = validateLogin(user);
        if (errors.size() > 0) {
            model.addAttribute("errmsg", errors);
            return "/toshare/user/login";
        }

        Subject subject= SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(user.getUsername(),user.getPassword());

        try{
            subject.login(token);
            //System.out.println(token.getPassword());
            //session.setAttribute("sessionUser", userMapper.findByUsernameAndPassword(user.getUsername(),user.getPassword()));
            session.setAttribute("sessionUser",userMapper.findUserByName(user.getUsername()));
                String loginname = user.getUsername();
                loginname = URLEncoder.encode(loginname, "utf-8");
                Cookie cookie = new Cookie("loginname", loginname);
                cookie.setMaxAge(60 * 60 * 24 * 10);
                resp.addCookie(cookie);
            return "redirect:/main";
        }catch (UnknownAccountException | IncorrectCredentialsException e){
            map.put("msg", "ユーザー名またはパスワードが正しくではありません。");
            model.addAttribute("user", user);
            return "/toshare/user/login";
        }

//        User loginuser = userService.login(user);
//        if (loginuser == null) {
//            map.put("msg", "ユーザー名またはパスワードが正しくではありません。");
//            model.addAttribute("user", user);
//            return "/toshare/user/login";
//        } else {
//            HttpSession session = request.getSession(true);
//            User sessionUser = userService.login(loginuser);
//            session.setAttribute("sessionUser", sessionUser);
//                // 获取用户名保存到cookie中
//                String loginname = user.getUsername();
//                loginname = URLEncoder.encode(loginname, "utf-8");
//                Cookie cookie = new Cookie("loginname", loginname);
//                cookie.setMaxAge(60 * 60 * 24 * 10);
//                resp.addCookie(cookie);
////                System.out.println(login.getNo());
//
//                return "redirect:/main";
//        }
    }
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request){
//        request.getSession().removeAttribute("sessionUser");
//        return "redirect:/user/login";
//    }
    @GetMapping("/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "redirect:/user/login";
    }
    private Map<String,String> validateLogin(User user) {
        Map<String,String> errors = new HashMap<>();
        String username = user.getUsername();

        if(username == null || username.trim().isEmpty()) {
            errors.put("username", "ユーザー名を入力してください。");
        }
        /*
         * 2. password validation
         */

        String password = user.getPassword();
        if(password == null || password.trim().isEmpty()) {
            errors.put("password", "パスワードを入力してください。");
        }
        return errors;
    }

}
