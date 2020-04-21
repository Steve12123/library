package com.hkjxth.controller;

import com.hkjxth.bean.*;
import com.hkjxth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping("/replyTalking")
    @ResponseBody
    public JsonResult replyTalking(HttpSession session,
                               @RequestParam("talkingId")Integer talkingId,
                               @RequestParam("reportArea")String reportArea){
        USERID= (Integer) session.getAttribute("userId");
        String date=UtilClass.getDateToDatabase();
        String replyUserName=userService.getUserById(USERID).getUserName();
        userService.addReply(talkingId,USERID,replyUserName,reportArea,date);
        return JsonResult.success().add("message","success");
    }

    @RequestMapping("/selectTalking")
    @ResponseBody
    public JsonResult selectTalking(@RequestParam("key")String key){
        List<Talking> list=userService.selectTalkingByTalkingName(key);
        return JsonResult.success().add("list",list);
    }

    @RequestMapping("/getTalkingById")
    @ResponseBody
    public JsonResult getTalkingById(@RequestParam("talkingId")Integer talkingId){
        Talking talking=userService.getTalkingById(talkingId);
        return JsonResult.success().add("talking",talking);
    }

    @RequestMapping("/showTalking")
    public String toTalkingId(@RequestParam("talkingId")Integer talkingId){
        return "redirect:/talkingInfo.html?talkingId="+talkingId;
    }

    @RequestMapping("/getUserTalking")
    @ResponseBody
    public JsonResult getUserInfo(){
        List<Talking> talkingList=userService.selectTalkingByUserId(USERID);
        return JsonResult.success().add("talkingList",talkingList);
    }

    @RequestMapping("/getTalkingByUserId")
    @ResponseBody
    public JsonResult getUserTalking(@RequestParam("userId")Integer userId){
        List<Talking> talkingList=userService.selectTalkingByUserId(userId);
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
    @Transactional
    public JsonResult saveTalking(@RequestParam("title")String title,
                                  @RequestParam("photo")MultipartFile file,
                                  @RequestParam("subject")String subject,
                                  @RequestParam("textarea")String textarea){
        String date=UtilClass.getDate();
        String fileName=UtilClass.savePhoto(file,null);
        User user=userService.getUserById(USERID);
        Integer talkingId=userService.saveNewTalking(title,user.getUserId(),user.getUserName(),textarea,date,subject,fileName,UtilClass.getDateToLocal());
        userService.createTalkingReplyByTalkingId(talkingId);
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

    @RequestMapping("/getTalkingReply")
    @ResponseBody
    public JsonResult getTalkingReply(@RequestParam("talkingId")Integer talkingId){
        List<Reply> list=userService.getTalkingReply(talkingId);
        if (list==null){
            return JsonResult.success().add("message","empty");
        }else{
            return JsonResult.success().add("replyList",list);
        }
    }
}
