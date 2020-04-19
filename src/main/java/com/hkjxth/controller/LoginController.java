package com.hkjxth.controller;



import com.hkjxth.bean.JsonResult;
import com.hkjxth.bean.Root;
import com.hkjxth.bean.User;
import com.hkjxth.bean.UtilClass;
import com.hkjxth.service.RootService;
import com.hkjxth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;


@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private RootService rootService;

    private static Integer USERID=null;
    private static String USERNAME=null;

    @RequestMapping("/login")
    public String login(@RequestParam(name = "username", defaultValue = "0") String username,
                        @RequestParam("password") String password,
                        @RequestParam("checkbox") String isUser,
                        Model model,
                        HttpSession session){
        Integer userId;
        try{
            userId=Integer.valueOf(username);
        }catch (NumberFormatException e){
            String errIgnoire="用户名非法";
            model.addAttribute("loginErrInfoToUser",errIgnoire);
            return "index";
        }
        if(isUser.equals("user")){
            User user=userService.selectUserByIdAndName(userId,password);
            if(user==null){
                String errInfo="用户名或密码错误";
                model.addAttribute("loginErrInfoToUser",errInfo);
                return "index";
            }else{
                if (rootService.isUserLocked(userId)){
                    String errInfo="此用户已被管理员冻结,暂时无法登录！";
                    model.addAttribute("loginErrInfoToUser",errInfo);
                    return "index";
                }else{
                    //向session域中存入值
                    session.setAttribute("loginUser",user.getUserRealname());
                    session.setAttribute("userId",user.getUserId());
                    USERID=user.getUserId();
                    USERNAME=user.getUserRealname();
                    return "redirect:/mainuser.html";
                }
            }
        }else{
            Root root=rootService.rootLogin(userId,password);
            if(root==null){
                String errInfo="用户名或密码错误";
                model.addAttribute("loginErrInfoToUser",errInfo);
                return "index";
            }else{
                //管理员账号验证成功 进入主页面
                session.setAttribute("loginUser",root.getRootName());
                return "redirect:/mainroot.html";
            }
        }
    }

    @RequestMapping("/register")
    @ResponseBody
    public JsonResult register(@RequestParam("registerName")String registerName,
                               @RequestParam("registerRealName")String realName,
                               @RequestParam("sex")String sex,
                               @RequestParam("password")String password,
                               @RequestParam("password1")String password1) throws UnsupportedEncodingException {
        registerName= UtilClass.ajaxUTF8(registerName);
        realName=UtilClass.ajaxUTF8(realName);
        if(registerName==null||registerName.equals("")){
            return JsonResult.fail().add("errInfo","用户名非法");
        }else {
            if(registerName.length()>=11){
                return JsonResult.fail().add("errInfo","用户名长度不能超过11位");
            }
            boolean result=userService.isNameHasRegister(registerName);
            if(!result){
                return JsonResult.fail().add("errInfo","此用户名已注册");
            }
        }
        if(realName==null||realName.equals("")){
            return JsonResult.fail().add("errInfo","真实姓名非法");
        }
        if(password==null||password.equals("")||password1==null||password1.equals("")){
            return JsonResult.fail().add("errInfo","输入的密码非法");
        }
        if (!password.equals(password1)){
            return JsonResult.fail().add("errInfo","两次输入的密码不一致");
        }
        userService.registerUser(new User(0,registerName,realName,sex,null,password));
        Integer userId=userService.selectIdByUsernameAndPassword(registerName,password);
        userService.registerUserPhotoTable(userId);
        return JsonResult.success().add("successInfo","注册成功,登录账号为："+userId);
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("loginUser");
        session.removeAttribute("userId");
        return "redirect:/index.html";
    }
}
