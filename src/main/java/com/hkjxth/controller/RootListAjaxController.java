package com.hkjxth.controller;

import com.hkjxth.bean.*;
import com.hkjxth.dao.RootDao;
import com.hkjxth.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/rootajaxcontroller")
public class RootListAjaxController {

    @Autowired
    private RootDao rootDao;

    @Autowired
    private UserDao userDao;

    /*定义当前页*/
    private static int PAGENUMBER=1;
    /*定义最大页*/
    private static int LASTPAGE;

    @RequestMapping("/gettableinfo")
    public JsonResult getTableInfo(){
        //获取图书总数
        Integer bookCounts=rootDao.getBookCounts();
        //获取所有书类
        List<BookSubject> bookSubject=rootDao.getAllBookSubject();
        Map<String, Integer> bookSbujectCount=new HashMap<>();
        for (int i=0;i<bookSubject.size();i++){
            int sBookCcount=rootDao.getBookCountsBySubject(bookSubject.get(i).getSubjectName());
            bookSbujectCount.put(bookSubject.get(i).getSubjectName(),sBookCcount);
        }
        return JsonResult.success().add("tableinfo",bookSbujectCount).add("bookCounts",bookCounts);
    }

    @RequestMapping("/selectbook")
    public JsonResult selectBook(@RequestParam(name = "subject",defaultValue = "")String subject,
                                 @RequestParam(name = "selectAuthor",defaultValue = "") String autherName,
                                 @RequestParam(name = "selectBookname",defaultValue = "") String bookname) throws UnsupportedEncodingException {
        subject=UtilClass.ajaxUTF8(subject);
        autherName=UtilClass.ajaxUTF8(autherName);
        bookname=UtilClass.ajaxUTF8(bookname);
        List<Book> list=rootDao.selectBooksBySomething(subject,autherName,bookname);
        return JsonResult.success().add("list",list);
    }

    @RequestMapping("/getBookById")
    public JsonResult getBookById(@RequestParam("bookId")Integer id){
        Book book=rootDao.getBookById(id);
        return JsonResult.success().add("book",book);
    }

    @RequestMapping("/selectbooksubject")
    public JsonResult selectSubject(){
        List<BookSubject> list=rootDao.getAllBookSubject();
        return JsonResult.success().add("subjectList",list);
    }

    @RequestMapping("/selectAllBooksWithLimit")
    public JsonResult selectAllBooksByLimit(@RequestParam(name = "pageNum",defaultValue = "1")Integer pageNum){
        /*获取图书总数*/
        Integer counts=rootDao.selectBookNumbers();
        /*设置每页显示图书数*/
        int pageSize=10;
        /*设置最大页数*/
        int lastPage;
        if(counts%pageSize==0){
            lastPage=counts/pageSize;
        }else{
            lastPage=counts/pageSize+1;
        }
        /*定义查找起始位置*/
        if(pageNum<=0){
            pageNum=1;
        }
        /*设置最大页不能超过指定值*/
        if(pageNum>lastPage){
            pageNum=lastPage;
        }
        if(counts<=pageSize){
            lastPage=pageNum=1;
        }
        int pageStart=(pageNum-1)*pageSize;
        List<Book> list=rootDao.selectAllBooksUseLimit(pageStart, pageSize);
        PAGENUMBER=pageNum;
        LASTPAGE=lastPage;
        return JsonResult.success().add("bookList",list);
    }

    @RequestMapping("/getPageNumber")
    public JsonResult getPageNumber(){
        return JsonResult.success().add("pageNumber",PAGENUMBER);
    }

    @RequestMapping("/getLastPage")
    public JsonResult getLastPage(){
        return JsonResult.success().add("lastPage",LASTPAGE);
    }

    @RequestMapping("/saveUpdate")
    public JsonResult saveUpdate(@RequestParam("bookId")Integer bookId,
                                 @RequestParam("updateName")String bookName,
                                 @RequestParam("updateAuthor")String bookAuthor,
                                 @RequestParam("updateCount")Integer bookCount,
                                 @RequestParam("subject")String bookSubject,
                                 @RequestParam("describe")String bookDescribe){
       rootDao.saveBookInfo(bookName,bookAuthor,bookCount,bookSubject,bookDescribe,bookId);
       return JsonResult.success();
    }
    @RequestMapping("/savePicture")
    public JsonResult savePicture(@RequestParam("bookId")Integer bookId,
                                  @RequestParam("pictureFile")MultipartFile file,
                                  @RequestParam("oldPicture")String oldPicture){
        String fileName=UtilClass.savePhoto(file,null);
        UtilClass.deletePhoto(oldPicture);
        rootDao.savePhoto(bookId,fileName);
        return JsonResult.success().add("pictureName",fileName);
    }

    @RequestMapping("/insertNewBook")
    public JsonResult insertNewBook(@RequestParam("name")String bookName,
                                    @RequestParam("author")String bookAuthor,
                                    @RequestParam("count")Integer bookCount,
                                    @RequestParam("subject")String bookSubject,
                                    @RequestParam("describe")String bookDescribe,
                                    @RequestParam("picture")MultipartFile file) throws SQLException {
        String fileName=UtilClass.savePhoto(file,null);
        Integer id=rootDao.insertNewBook(bookName,bookAuthor,bookCount,bookSubject,bookDescribe,fileName);
        return JsonResult.success().add("newBookId",id);
    }

    @RequestMapping("/deleteBook")
    public JsonResult deleteBook(@RequestParam("bookId")Integer bookId){
        Book book=rootDao.getBookById(bookId);
        UtilClass.deletePhoto(book.getBookPicture());
        rootDao.deleteBookById(bookId);
        return JsonResult.success().add("id",bookId);
    }

    @RequestMapping("/getIndexInformation")
    public JsonResult getIndexInformation(){
        /*注册用户数
        * 书籍总数
        * 每日访问量*/
        Integer userCounts=rootDao.getAllUsersCount();
        Integer bookCounts=rootDao.getBookCounts();
        List select=new ArrayList();
        select.add(userCounts);
        select.add(bookCounts);
        return JsonResult.success().add("indexList",select);
    }

    @RequestMapping("/getUserBookById")
    public JsonResult getUserBookById(@RequestParam("userId")Integer userId){
        List<UserBookList> list=rootDao.getUserBookListById(userId);
        return JsonResult.success().add("bookList",list);
    }

    @RequestMapping("/getUserTalkingById")
    public JsonResult getUserTalkingById(@RequestParam("userId")Integer userId){
        List<Talking> list=rootDao.getTalkingByUserId(userId);
        return JsonResult.success().add("talkingList",list);
    }

    @RequestMapping("/returnBookByIds")
    @Transactional
    public JsonResult returnBookByIds(@RequestParam("userId")Integer userId,
                                      @RequestParam("bookId")Integer bookId){
        Boolean result=rootDao.returnBookByUserIdAndBookId(userId,bookId);
        if(result){
            Message message=new Message(null,userId,"还书操作",UtilClass.getDateToDatabase(),UtilClass.getDate(),"编号为"+bookId+"的书籍已确认收回。");
            userDao.saveMessage(message);
            return JsonResult.success().add("message","success");
        }else{
            return JsonResult.fail().add("message","fail");
        }
    }

    @RequestMapping("/saveAdv")
    public void saveAdv(@RequestParam("advTitle")String advTitle,
                        @RequestParam("advBody")String advBody){
        String date=UtilClass.getDateToDatabase();
        rootDao.saveActiveToRedis(advTitle,date,advBody);
    }

    @RequestMapping("/saveActive")
    public void saveActive(@RequestParam("advTitle") String titleName,
                                 @RequestParam("advPicture") MultipartFile picture,
                                 @RequestParam("advBody") String body){
        String photoName=UtilClass.savePhoto(picture,null);
        String date=UtilClass.getDateToDatabase();
        rootDao.insertNewBoard(titleName,body,date,photoName);
    }

    @RequestMapping("/getNewAdvice")
    public JsonResult getNewAdvice(){
        Board board=rootDao.getBoardsByCount();
        return JsonResult.success().add("board",board);
    }

    @RequestMapping("/getAllBringBooksRecord")
    public JsonResult getAllBringBooksRecord(){
        List<Message> list=rootDao.getAllBringBooksRecord();
        return JsonResult.success().add("list",list);
    }

    @RequestMapping("/selectLeavingTimeList")
    public JsonResult selectLeavingTimeList(){
        List<UserBookList> bookList=rootDao.getLeavingTimeList();
        return JsonResult.success().add("bookList",bookList);
    }

    @GetMapping("/getUserInfo{userId}")
    public JsonResult getUserInfo(@PathVariable("userId")Integer userId){
        User user=userDao.getUserById(userId);
        user.setUserPassword(null);
        return JsonResult.success().add("user",user);
    }

    @RequestMapping("/sendMessage")
    public JsonResult sendMessage(@RequestParam("userId")Integer userId,@RequestParam("bookId")Integer bookId){
        rootDao.sendMessage(userId,"逾期通知",UtilClass.getDateToDatabase(),UtilClass.getDate(),"书籍编号为"+bookId+"的书籍已逾期，请尽快还书！");
        return JsonResult.success().add("message","success");
    }

    @RequestMapping("/selectRecords")
    public JsonResult selectRecords(@RequestParam(name = "selectTitle",defaultValue = "")String selectTitle,
                                    @RequestParam(name = "selectId",defaultValue = "") Integer selectId,
                                    @RequestParam(name = "selectDate",defaultValue = "") String selectDate) {
        System.out.println("title:"+selectTitle);
        System.out.println("selectId = " + selectId);
        System.out.println("selectDate = " + selectDate);
        List<Message> set=rootDao.selectMessageWithCondition(selectTitle,selectId,selectDate);
        System.out.println("set = " + set);
        if (set!=null){
            return JsonResult.success().add("messageList",set);
        }else{
            return JsonResult.success().add("message","fail");
        }


    }

}
