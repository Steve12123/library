package com.hkjxth.controller;

import com.hkjxth.bean.UtilClass;
import com.hkjxth.service.RootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/rootcontroller")
public class RootController {

    @Autowired
    private RootService rootService;

    @RequestMapping("/getphoto")
    public String getPhoto(@RequestParam("image")MultipartFile file, Model model){
        String fileName=UtilClass.savePhoto(file,null);
        return "redirect:/mainroot.html";
    }

    @RequestMapping("/checkUser")
    public String checkUser(@RequestParam("userId")Integer userId){
        return "redirect:/showUserByRooter.html?userId="+userId;
    }





}
