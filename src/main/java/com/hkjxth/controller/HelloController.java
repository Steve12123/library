package com.hkjxth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class HelloController {

    @RequestMapping("/go")
    public String success(Map<String,Object> map){
        map.put("hello","nihao");
        return "success";
    }

    /*@RequestMapping("/register")
    public String register(HttpServletRequest request){
        request.getSession().setAttribute("loginUser","register");
        *//*设置当前session有效期为5分钟*//*
        request.getSession().setMaxInactiveInterval(5);
        return "redirect:/register.html";
    }*/

}
