package com.wjc.handler;

import com.wjc.admin.mapper.AdminMapper;
import com.wjc.exception.ArticleNotFoundException;
import com.wjc.user.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired(required = false)
    private AdminMapper adminMapper;
    @ExceptionHandler(ArticleNotFoundException.class)
    public ModelAndView ArticleException(HttpServletRequest request, Exception e){
        logger.error("Request : {}, Exception : {}" ,request.getRequestURL(),e);

        ModelAndView mv=new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);


        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        if (sessionUser!=null){
        int message_count = adminMapper.historyUnreadCount(sessionUser.getUsername());
        mv.addObject("message",message_count);}

        mv.setViewName("/toshare/error/404");
        return mv;
    }
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e){
        logger.error("Request : {}, Exception : {}" ,request.getRequestURL(),e);

        ModelAndView mv=new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);

        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        if (sessionUser!=null){
            int message_count = adminMapper.historyUnreadCount(sessionUser.getUsername());
            mv.addObject("message",message_count);}

        mv.setViewName("/toshare/error/500");
        return mv;
    }
}
