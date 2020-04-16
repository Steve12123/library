package com.hkjxth.controller;

import com.hkjxth.bean.*;
import com.hkjxth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/userTalking")
public class UserTalkingController {

    @Autowired
    private UserService userService;

    private static Integer USERID;

    @RequestMapping("/createNewTalking")
    public String createNewTalking(HttpSession session){
        USERID= (Integer) session.getAttribute("userId");
        return "redirect:/createTalking.html";
    }

    @RequestMapping("/mineTalking")
    public String myTalking(HttpSession session){
        USERID= (Integer) session.getAttribute("userId");
        return "redirect:/mineTalking.html";
    }

    @RequestMapping("/selectTalking")
    @ResponseBody
    public JsonResult selectTalking(@RequestParam("key")String key){
        List<Talking> list=userService.selectTalkingByTalkingName(key);
        return JsonResult.success().add("list",list);
    }

    @RequestMapping("/showTalking")
    public String toTalkingId(@RequestParam("talkingId")Integer id,
                              RedirectAttributes attributes){
        attributes.addFlashAttribute("talkingId",id);
        return "redirect:/talkingInfo.html";
    }

    @RequestMapping("/getUserTalking")
    @ResponseBody
    public JsonResult getUserInfo(){
        List<Talking> talkingList=userService.selectTalkingByUserId(USERID);
        return JsonResult.success().add("talkingList",talkingList);
    }

    @RequestMapping("/getTalkingSubject")
    @ResponseBody
    public JsonResult getTalkingSubject(){
        List<TalkingSubject> list=userService.getTalkingSubject();
        return JsonResult.success().add("subjectList",list);
    }

    @RequestMapping("/saveTalking")
    @ResponseBody
    public JsonResult saveTalking(@RequestParam("title")String title,
                                  @RequestParam("photo")MultipartFile file,
                                  @RequestParam("subject")String subject,
                                  @RequestParam("textarea")String textarea){
        String date=UtilClass.getDate();
        String fileName=UtilClass.savePhoto(file,null);
        User user=userService.getUserById(USERID);
        userService.saveNewTalking(title,user.getUserId(),user.getUserName(),textarea,date,subject,fileName);
        return JsonResult.success().add("message","success");
    }

    @RequestMapping("/deleteTalkingById")
    @ResponseBody
    public JsonResult deleteTalkingById(@RequestParam("id")Integer id){
        Talking talking=userService.getTalkingById(id);
        String photoName=talking.getTalkingPhoto();
        userService.deleteTalkingById(id);
        UtilClass.deletePhoto(photoName);
        return JsonResult.success().add("message","success");
    }

    @RequestMapping("/showUser")
    public String showUser(@RequestParam("userId")Integer userId){
        return "redirect:/showUser.html?userId="+userId;
    }

    @RequestMapping("/getUserInfoToShow")
    @ResponseBody
    public JsonResult getUserInfoToShow(@RequestParam("userId")Integer userId){
        User thisUser=userService.getUserById(userId);
        if(thisUser==null||thisUser.equals("")){
            return JsonResult.fail().add("userInfo","fail");
        }
        thisUser.setUserPassword(null);
        thisUser.setUserRealname(null);
        String userPhoto=userService.getUserPhoto(thisUser.getUserId());
        thisUser.setUserPassword(userPhoto);
        return JsonResult.success().add("userInfo",thisUser);
    }
}
