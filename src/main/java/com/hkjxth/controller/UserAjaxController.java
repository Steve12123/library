package com.hkjxth.controller;

import com.hkjxth.bean.*;
import com.hkjxth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 *  专门用于处理personlist主界面的ajax请求
 */
@RestController
@RequestMapping("/ajaxcontroller")
public class UserAjaxController {
    @Autowired
    private UserService userService;

    @RequestMapping("/selectFourSubject")
    public JsonResult selectFourSubject(){
        List<BookSubject> list=userService.selectFourSubject();
        return JsonResult.success().add("subjectList",list);
    }


    @RequestMapping("/getNewBoard")
    public JsonResult selectNewBoard(){
        Active active=userService.selectNewBoard();
        return JsonResult.success().add("active",active);
    }

    @RequestMapping("/getDayBook")
    public JsonResult getDayBook(HttpSession session){
        Integer userId= (Integer) session.getAttribute("userId");
        String userSubject=userService.getUserById(userId).getUserSubject();
        List<Book> list;
        if (userSubject==null||userSubject.equals("")){
            list=userService.getDayBook(null);
        }else{
            list=userService.getDayBook(userSubject);
        }
        return JsonResult.success().add("daybooklist",list);
    }

    @RequestMapping("/getHotBookList")
    public JsonResult getHotBookList(){
        List<UserBookList> set=userService.getHotBookList();
        return JsonResult.success().add("set",set);
    }

    @RequestMapping("/getNewUpdateBook")
    public JsonResult getNewUpdateBook(){
        List<Book> list=userService.getNewUpdateBook();
        return JsonResult.success().add("list",list);
    }

    @RequestMapping("/getNowBookCount")
    public JsonResult getNowBookCount(@RequestParam("bookId")Integer bookId){
        Integer bookCount=userService.getBookCount(bookId);
        if(bookCount==0){
            return JsonResult.fail().add("message","n");
        }else{
            return JsonResult.success().add("message","y");
        }
    }

    @RequestMapping("/bringbook")
    @Transactional
    public JsonResult bringBook(@RequestParam("userId")Integer userId,
                                @RequestParam("bookId")Integer bookId){
        String date=UtilClass.getDate();
        Book book=userService.getLibraryBookById(bookId);
        Boolean result=userService.bringBook(userId,book.getBookName(),bookId,date);
        if(result){
            Message message=new Message(null,userId,"借书通知",UtilClass.getDateToDatabase(),UtilClass.getDate(),"书籍《"+book.getBookName()+"》借书操作成功，请尽快前往图书馆取书！");
            userService.saveMessage(message);
            return JsonResult.success().add("message","success");
        }else{
            return JsonResult.fail().add("message","fail");
        }
    }

    @RequestMapping("/getTalkingList")
    public JsonResult getTalkingList(){
        List<Talking> talkingList=userService.getTalkingList(0);
        return JsonResult.success().add("talkingList",talkingList);
    }

    @GetMapping("/listPage{page}")
    public JsonResult listPage(@PathVariable("page")Integer pageNum){
        /*每次显示五条数据*/
        List<Talking> talkingList=userService.getTalkingList((pageNum-1)*5);
        return JsonResult.success().add("talkingList",talkingList);
    }

    @RequestMapping("/getUserPhoto")
    public JsonResult getUserPhoto(@RequestParam("userId")Integer userId){
        String photo=userService.getUserPhoto(userId);
        return JsonResult.success().add("photo",photo);
    }

    @RequestMapping("/markBook")
    public JsonResult markBook(@RequestParam("userId")Integer userId,
                               @RequestParam("bookId")Integer bookId){
        String result=userService.isUserMarkThisBook(userId,bookId);
        if (result.equals("no")){
            Book book=userService.getBookInfoById(bookId);
            userService.saveMarkInfo(userId,bookId,book.getBookName(),UtilClass.getDateToDatabase());
            return JsonResult.success().add("message","success");
        }else{
            return JsonResult.fail().add("message","fail");
        }
    }
}
