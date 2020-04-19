package com.hkjxth.service;

import com.hkjxth.bean.Book;
import com.hkjxth.bean.BookSubject;
import com.hkjxth.bean.Root;
import com.hkjxth.dao.RootDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RootService {

    @Autowired
    private RootDao rootDao;

    public Root rootLogin(Integer id,String password){
        return rootDao.rootLogin(id,password);
    }

    public Integer getBookCounts(){
        return rootDao.getBookCounts();
    }

    public List<BookSubject> getAllBookSubjectName(){
        return rootDao.getAllBookSubject();
    }

    public Integer getBookCountsBySubjectName(String subjectName){
        return rootDao.getBookCountsBySubject(subjectName);
    }

    public List<Book> selectBooksBySomethings(String subject, String authorName, String bookName){
        return rootDao.selectBooksBySomething(subject,authorName,bookName);
    }

    public List<Book> selectAllBooksUseLimit(Integer titlePage,Integer pageSize){
        return rootDao.selectAllBooksUseLimit(titlePage,pageSize);
    }

    public Book getBookById(int bookId) {
        return rootDao.getBookById(bookId);
    }

    public Boolean isUserLocked(Integer userId) {
        return rootDao.isUserLocked(userId);
    }
}
