package com.hkjxth.dao;

import com.hkjxth.bean.*;

import java.util.List;


public interface UserDao {

    Integer registeredThenReturnUserBy(String username,String password);

    User findByIdAndName(Integer id, String password);

    List<BookSubject> selectFourSubject();

    List<BookSubject> selectAllSubjects();

    Active selectNewBoard();

    List<Book> getDayBook();

    boolean isNameHasRegister(String name);

    void registerUser(User user);

    List<Book> getNewUpdateBook();

    Integer getThisBookCount(Integer bookId);

    Boolean bringBook(Integer userId, String bookName, Integer bookId, String date);

    User getUserById(Integer id);

    String getPhoto(Integer id);

    void savePhoto(Integer userId, String photoName);

    void registerUserPhotoTable(Integer userId);

    void updateUserInfo(Integer userId, String changeName, String sex, String subject);

    String checkPassword(Integer userId);

    void changeUserPassword(Integer userId, String newPassword);

    List<UserBookList> getUserBookList(Integer userId);

    Book getLibraryBookById(Integer bookId);

    void addBookTime(Integer userId, Integer bookId);

    List<UserBookList> getHotBookList();

    List<Talking> getTalkingList(Integer pageNum);

    List<Talking> selectTalkingByTalkingName(String key);

    List<Talking> selectTalkingByUserId(Integer userId);

    List<TalkingSubject> getTalkingSubject();

    void saveNewTalking(String title, Integer userId, String userName, String textarea, String date, String subject, String fileName);

    Talking getTalkingById(Integer talkingId);

    void deleteTalkingById(Integer id);

    List<Message> getUserMessage(Integer userId);

    void saveMessage(Message message);
}
