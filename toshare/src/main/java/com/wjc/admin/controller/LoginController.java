package com.wjc.admin.controller;

import com.wjc.admin.mapper.AdminMapper;
import com.wjc.admin.pojo.Admin;
import com.wjc.admin.service.AdminService;
import com.wjc.type.mapper.TypeMapper;
import com.wjc.type.pojo.Type_first;
import com.wjc.type.pojo.Type_second;
import com.wjc.type.service.TypeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private TypeService typeService;
    @Autowired(required = false)
    private TypeMapper typeMapper;
    @Autowired(required = false)
    private AdminMapper adminMapper;

    @GetMapping("/login")
    public String loginPage(HttpSession session,Model model) {
        if (session.getAttribute("sessionAdmin")!=null){
            return "redirect:/admin/dashboard";
        }
//        else if (session.getAttribute("sessionUser")!=null){
////            Map<String, String> errors = new HashMap<>();
////            errors.put("logout","Please logout the user account before login the admin page.");
////            model.addAttribute("errmsg", errors);
//            return "/toshare/user/login";
//        }
        return "/toshare/admin/adminLogin";
    }

    @PostMapping("/login")
    public String login(Admin admin, HttpSession session,
                        Map<String,Object> map, Model model, HttpServletResponse resp) throws UnsupportedEncodingException {

        Map<String, String> errors = validateLogin(admin);
        if (errors.size() > 0) {
            model.addAttribute("errmsgA", errors);
            return "/toshare/admin/adminLogin";
        }

        Subject subject= SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(admin.getAdminname(),admin.getPassword());

        try{
            subject.login(token);
            Admin sessionAdmin = adminMapper.findByAdminnameAndPassword(admin.getAdminname(), admin.getPassword());
            sessionAdmin.setPassword(null);
            session.setAttribute("sessionAdmin", sessionAdmin);
            return "redirect:/admin/dashboard";
        } catch (UnknownAccountException | IncorrectCredentialsException e){
            map.put("msg", "ユーザー名またはパスワードが正しくではありません。");
            model.addAttribute("admin", admin);
            return "/toshare/admin/adminLogin";
        }


//        Admin sessionAdmin = adminService.login(admin);
//        if (sessionAdmin == null) {
//            map.put("msg", "ユーザー名またはパスワードが正しくではありません。");
//            model.addAttribute("admin", admin);
//            return "/toshare/admin/adminLogin";
//        } else {
//            admin.setPassword(null);
//            session.setAttribute("sessionAdmin", sessionAdmin);
//            return "redirect:/admin/dashboard";
//        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("sessionAdmin");
        return "redirect:/admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        Map<Type_first, List<Type_second>> allTypeAndCount = adminService.findAllTypeAndCount();
        model.addAttribute("allType",allTypeAndCount);
//        Map<String, List<String>> allType = typeService.findAllType();
//        model.addAttribute("allType",allType);
        return "/toshare/admin/dashboard";
    }
    //@RequiresRoles("admin")
    @GetMapping("/type/addSupertype")
    public String addSupertype(Model model){
        model.addAttribute("supertypes",typeMapper.findType_first());
        return "/toshare/admin/type/addSupertype";
    }
    @PostMapping("/addSupertype")
    public String addSupertype(Type_first type_first, RedirectAttributes attributes,Model model,String adminname) {
        if (type_first.getType_first_name().length()==0 || type_first.getType_first_name().trim().isEmpty()){
            model.addAttribute("nullMsgSup","the name of type is blank.");
            return "/toshare/admin/type/addSupertype";
        }
        Boolean result= adminService.addSupertype(type_first,adminname);
        if (result){
            attributes.addFlashAttribute("message_0","success");
            attributes.addFlashAttribute("message_tip","added.");
            return "redirect:/admin/dashboard";
        }else {
            model.addAttribute("message_1","failed");
            model.addAttribute("message_tip","Duplicate supertype.");
            model.addAttribute("sup",type_first);
            Map<String, List<String>> allType = typeService.findAllType();
            model.addAttribute("allType",allType);
            return "/toshare/admin/type/addSupertype";
        }
    }
    @GetMapping("/type/addSubtype")
    public String addSubtype(@RequestParam(value = "typeName",required = false)String type1,Model model){
        model.addAttribute("supertypes",typeMapper.findType_first());
        model.addAttribute("subtypes",typeMapper.findType_second());
        model.addAttribute("type1ID",typeMapper.find1stID(type1));
        return "/toshare/admin/type/addSubtype";
    }
    @PostMapping("/addSubtype")
    public String addSubtype(Type_second type_second,String first_id, RedirectAttributes attributes, Model model,String adminname){
        if (isNumeric(first_id)){
            model.addAttribute("type1ID",Integer.valueOf(first_id));
        } else{
            model.addAttribute("nullMsgSub0","supertype is not selected.");
            model.addAttribute("supertypes",typeMapper.findType_first());
            System.out.println(first_id);
            return "/toshare/admin/type/addSubtype";
        }
        if (type_second.getType_second_name().length()==0 || type_second.getType_second_name().trim().isEmpty()){
            model.addAttribute("nullMsgSub","the name of type is blank.");
            model.addAttribute("supertypes",typeMapper.findType_first());
            model.addAttribute("subtypes",typeMapper.findType_second());
            return "/toshare/admin/type/addSubtype";
        }
        type_second.setT_first_id(Integer.valueOf(first_id));
        String superTypeName = typeMapper.find1stTypeNameBy1stTypeID(type_second.getT_first_id());
        Boolean result= adminService.addSubtype(type_second,adminname,superTypeName);
        if (result){
            attributes.addFlashAttribute("message_0","success");
            attributes.addFlashAttribute("message_tip","added.");
        }else {
        model.addAttribute("message_1","failed");
        model.addAttribute("message_tip","Duplicate subtype.");
        model.addAttribute("sub",type_second);
        model.addAttribute("supertypes",typeMapper.findType_first());
        model.addAttribute("subtypes",typeMapper.findType_second());
        return "/toshare/admin/type/addSubtype";
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/editType")
    public String editType(@RequestParam(required = false)String oldSuperName,String typeName, String currentTypeName,Integer supOrSubType,RedirectAttributes attributes,Model model,String adminname){
//        System.out.println(currentTypeName+currentTypeName.length());
//        System.out.println(typeName+typeName.length());
//        System.out.println(typeID);
//        System.out.println(typeName);
        if (typeName.length()==0 || typeName.trim().isEmpty()){
            model.addAttribute("nullNameMsg","the name of type is blank.");
            Map<Type_first, List<Type_second>> allTypeAndCount = adminService.findAllTypeAndCount();
            model.addAttribute("allType",allTypeAndCount);

            //Map<String, List<String>> allType = typeService.findAllType();
            //model.addAttribute("allType",allType);
            return "/toshare/admin/dashboard";
        }

        Boolean result= adminService.editType(oldSuperName,currentTypeName,typeName,supOrSubType,adminname);
        if (result){
            attributes.addFlashAttribute("message_0","success");
            attributes.addFlashAttribute("message_tip","edited.");
            return "redirect:/admin/dashboard";
        }else {
            model.addAttribute("message_1","failed");
            model.addAttribute("message_tip","Duplicate type.");
            Map<Type_first, List<Type_second>> allTypeAndCount = adminService.findAllTypeAndCount();
            model.addAttribute("allType",allTypeAndCount);
//            Map<String, List<String>> allType = typeService.findAllType();
//            model.addAttribute("allType",allType);
            return "/toshare/admin/dashboard";
        }
    }
    @PostMapping("/deleteType")
    public String deleteType(@RequestParam(required = false)String oldSuperName,String typeName, Integer supOrSubType,RedirectAttributes attributes,String adminname,Model model){
        Integer result= adminService.deleteType(oldSuperName,typeName,supOrSubType,adminname);
        if (result==0){
            attributes.addFlashAttribute("message_0","success");
            attributes.addFlashAttribute("message_tip","deleted");
            return "redirect:/admin/dashboard";
        }else if (result==1){
            model.addAttribute("message_1","failed");
            model.addAttribute("message_tip","This supertype still has subtype,please delete the subtype first.");
            Map<Type_first, List<Type_second>> allTypeAndCount = adminService.findAllTypeAndCount();
            model.addAttribute("allType",allTypeAndCount);
//            Map<String, List<String>> allType = typeService.findAllType();
//            model.addAttribute("allType",allType);
            return "/toshare/admin/dashboard";
        }else {
            model.addAttribute("message_1","failed");
            model.addAttribute("message_tip","Some articles which are under review or posted are belong to this type,please change the articles' type first.");
            Map<Type_first, List<Type_second>> allTypeAndCount = adminService.findAllTypeAndCount();
            model.addAttribute("allType",allTypeAndCount);
//            Map<String, List<String>> allType = typeService.findAllType();
//            model.addAttribute("allType",allType);
            return "/toshare/admin/dashboard";
        }
    }

    private Map<String,String> validateLogin(Admin admin) {
        Map<String,String> errors = new HashMap<>();
        String adminname = admin.getAdminname();

        if(adminname == null || adminname.trim().isEmpty()) {
            errors.put("adminname", "ユーザー名を入力してください。");
            System.out.println(adminname);
        }

        String password = admin.getPassword();
        if(password == null || password.trim().isEmpty()) {
            errors.put("password", "パスワードを入力してください。");
        }
        return errors;
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}

