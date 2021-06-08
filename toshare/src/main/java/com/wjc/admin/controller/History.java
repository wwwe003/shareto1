package com.wjc.admin.controller;

import com.wjc.admin.service.AdminService;
import com.wjc.pager.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class History {
    @Autowired
    AdminService adminService;
    @GetMapping("/history")
    public String history_article(@RequestParam(defaultValue = "1",required = false)String currentPage, Model model){
        Integer pageCode=Integer.parseInt(currentPage);
        PageBean<String> historyMessages = adminService.history_article(pageCode);
        model.addAttribute("historyMessages",historyMessages);
        return "/toshare/admin/history";
    }
    @GetMapping("/history_type")
    public String history_type(@RequestParam(defaultValue = "1",required = false)String currentPage,Model model){
        Integer pageCode=Integer.parseInt(currentPage);
        PageBean<String> typeHistoryMessages = adminService.history_type(pageCode);
        model.addAttribute("typeHistoryMessages",typeHistoryMessages);
        return "/toshare/admin/history_type";
    }
}
