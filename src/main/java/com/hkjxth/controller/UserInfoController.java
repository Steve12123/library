package com.hkjxth.controller;

import com.hkjxth.bean.*;
import com.hkjxth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/userinfocontroller")
public class UserInfoController {

    @Autowired
    private UserService userService;

    /*设置全局用户id属性*/
    private static Integer userId;
    private static String oldName;
    private static String oldPhoto;
    @RequestMapping("/userinfo{id}")
    public String userInfo(@PathVariable("id")Integer id){
        userId=id;
        return "redirect:/userinfopage.html";
    }

    @RequestMapping("/mywaiting{id}")
    public String userwaiting(@PathVariable("id")Integer id){
        userId=id;
        return "redirect:/userwaiting.html";
    }

    @RequestMapping("/myinformation{id}")
    public String information(@PathVariable("id")Integer id){
        userId=id;
        return "redirect:/userinfomation.html";
    }


    @RequestMapping("/getUserInfo")
    @ResponseBody
    public JsonResult getUserInfo(){
        User user=userService.getUserById(userId);
        oldName=user.getUserName();
        return JsonResult.success().add("user",user);
    }

    @RequestMapping("/getUserPhoto")
    @ResponseBody
    public JsonResult getUserPhoto(){
        String photo=userService.getUserPhoto(userId);
        if(photo!=null&&!photo.equals("")){
            oldPhoto=photo;
        }
        return JsonResult.success().add("photo",photo);
    }

    @RequestMapping("/savePhoto")
    @ResponseBody
    public JsonResult savePhoto(@RequestParam("picture")MultipartFile file){
        String photoName=UtilClass.savePhoto(file,null);
        UtilClass.deletePhoto(oldPhoto);
        oldPhoto=photoName;
        userService.savePhotoByUserId(userId,photoName);
        return JsonResult.success().add("photo",photoName);
    }

    @RequestMapping("/changeUserInfo")
    @ResponseBody
    public JsonResult saveChange(@RequestParam("changeName")String changeName,
                                 @RequestParam("sex")String sex,
                                 @RequestParam("subject")String subject){
        if(changeName==null||changeName.equals("")){
            return JsonResult.fail().add("message","用户名非法");
        }else {
            if(!changeName.equals(oldName)){
                boolean result=userService.isNameHasRegister(changeName);
                if(!result){
                    return JsonResult.fail().add("message","此用户名已注册");
                }
            }
        }
        userService.updateUserInfo(userId,changeName,sex,subject);
        Message message=new Message(null,userId,"操作确认",UtilClass.getDateToDatabase(),UtilClass.getDate(),"个人信息修改成功。");
        userService.saveMessage(message);
        return JsonResult.success().add("message","success");
    }

    @RequestMapping("/checkPassword")
    @ResponseBody
    public JsonResult checkPassword(@RequestParam("password")String password){
        String oldPassword=userService.checkPassword(userId);
        if (oldPassword.equals(password)&&password!=null&&!password.equals("")){
            return JsonResult.success().add("message","success");
        }else{
            return JsonResult.fail().add("message","密码不匹配");
        }
    }

    @RequestMapping("/changeUserPassword")
    @ResponseBody
    public JsonResult changeUserPassword(@RequestParam("newPassword")String newPassword){
        userService.changeUserPassword(userId,newPassword);
        Message message=new Message(null,userId,"操作确认",UtilClass.getDateToDatabase(),UtilClass.getDate(),"登录密码已修改。");
        userService.saveMessage(message);
        return JsonResult.success().add("message","密码修改成功！");
    }

    @RequestMapping("/getUserBookList")
    @ResponseBody
    public JsonResult getUserBookList(){
        List<UserBookList> list=userService.getUserBookList(userId);
        return JsonResult.success().add("bookList",list);
    }

    @RequestMapping("/addBookTime")
    @ResponseBody
    public JsonResult addBookTime(@RequestParam("bookId")Integer bookId){
        userService.addBookTime(userId,bookId);
        Message message=new Message(null,userId,"延期操作",UtilClass.getDateToDatabase(),UtilClass.getDate(),"编号为"+bookId+"的书籍延期操作成功。");
        userService.saveMessage(message);
        return JsonResult.success().add("message","success");
    }

    @RequestMapping("/getAddBookDayTime")
    @ResponseBody
    public JsonResult getAddBookDayTime(@RequestParam("bookId")Integer bookId){
        List<UserBookList> list=userService.getUserBookList(userId);
        Integer lastDayCount=0;
        for (UserBookList userBook:list) {
            if(userBook.getBookId().equals(bookId)){
                lastDayCount=userBook.getAddBookTime();
            }
        }
        return JsonResult.success().add("message",lastDayCount);
    }

    @RequestMapping("/getLeaveBookList")
    @ResponseBody
    public JsonResult getLeaveBookList(){
        List<Message> list=new ArrayList<>();
        list=userService.getUserMessage(userId);
        return JsonResult.success().add("list",list);
    }

    @RequestMapping("/getUserMarkList")
    @ResponseBody
    public JsonResult getUserMarkList(){
        List<MarkList> markList=userService.getUserMarkList(userId);
        if (markList==null||markList.size()==0){
            return JsonResult.success().add("message","empty");
        }else{
            return JsonResult.success().add("markList",markList);
        }
    }

    @RequestMapping("/removeMark")
    @ResponseBody
    public JsonResult removeMark(@RequestParam("markId")Integer markId){
        userService.removeMarkByMarkId(markId);
        return JsonResult.success().add("message","success");
    }
}
