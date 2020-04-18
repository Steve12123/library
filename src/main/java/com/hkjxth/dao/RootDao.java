package com.hkjxth.dao;

import com.hkjxth.bean.*;

import java.sql.SQLException;
import java.util.List;

public interface RootDao {

    Root rootLogin(Integer id, String password);

    //数据库中book_count的总数
    int getBookCounts();

    int getBookCountsBySubject(String bookSubject);

    List<BookSubject> getAllBookSubject();

    List<Book> selectBooksBySomething(String subject, String author, String bookname);

    List<Book> selectAllBooksUseLimit(Integer titlePage,Integer pageSize);
    //数据库中所有书籍的总数（不重复计算）
    Integer selectBookNumbers();

    Book getBookById(int id);

    void savePhoto(Integer bookId,String fileName);

    void saveBookInfo(String bookName,String bookAuthor,Integer bookCount,String bookSubject,String bookDescribe,Integer bookId);

    Integer insertNewBook(String bookName,String bookAuthor,Integer bookCount,String bookSubject,String bookDescribe,String fileName) throws SQLException;

    void deleteBookById(Integer id);

    Integer getAllUsersCount();

    List<UserBookList> getUserBookListById(Integer userId);

    Boolean returnBookByUserIdAndBookId(Integer userId, Integer bookId);

    void insertNewBoard(String advTitle, String advBody, String date, String photoName);

    void saveActiveToRedis(String titleName, String photoName, String body);

    Board getBoardsByCount();

    List<Talking> getTalkingByUserId(Integer userId);

    List<Message> getAllBringBooksRecord();

    List<UserBookList> getLeavingTimeList();

    void sendMessage(Integer userId, String title, String date, String datebaseTime, String body);

    List<Message> selectMessageWithCondition(String selectTitle, Integer selectId, String selectDate);

    List<User> getAllUser(Integer pageNum);
}
