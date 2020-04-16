package com.hkjxth.service;

import com.hkjxth.bean.*;
import com.hkjxth.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;


    public User selectUserByIdAndName(Integer id, String name){
        return userDao.findByIdAndName(id,name);
    }

    public List<BookSubject> selectFourSubject(){
        return userDao.selectFourSubject();
    }

    public List<BookSubject> selectAllSubjects(){
        return userDao.selectAllSubjects();
    }

    public Active selectNewBoard(){
        return userDao.selectNewBoard();
    }

    public List<Book> getDayBook(){
        return userDao.getDayBook();
    }

    public boolean isNameHasRegister(String name){
        return userDao.isNameHasRegister(name);
    }

    public void registerUser(User user){
        userDao.registerUser(user);
    }

    public Integer selectIdByUsernameAndPassword(String username,String password){
        return userDao.registeredThenReturnUserBy(username,password);
    }

    public List<Book> getNewUpdateBook() {
        return userDao.getNewUpdateBook();
    }

    public Integer getBookCount(Integer bookId) {
        return userDao.getThisBookCount(bookId);
    }

    public Boolean bringBook(Integer userId, String bookName, Integer bookId, String date) {
        return userDao.bringBook(userId,bookName,bookId,date);
    }

    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    public String getUserPhoto(Integer id) {
        return userDao.getPhoto(id);
    }

    public void savePhotoByUserId(Integer userId, String photoName) {
        userDao.savePhoto(userId,photoName);
    }

    public void registerUserPhotoTable(Integer userId) {
        userDao.registerUserPhotoTable(userId);
    }

    public void updateUserInfo(Integer userId, String changeName, String sex, String subject) {
        userDao.updateUserInfo(userId,changeName,sex,subject);
    }

    public String checkPassword(Integer userId) {
        return userDao.checkPassword(userId);
    }

    public void changeUserPassword(Integer userId, String newPassword) {
        userDao.changeUserPassword(userId,newPassword);
    }

    public List<UserBookList> getUserBookList(Integer userId) {
        return userDao.getUserBookList(userId);
    }

    public Book getLibraryBookById(Integer bookId) {
        return userDao.getLibraryBookById(bookId);
    }

    public void addBookTime(Integer userId, Integer bookId) {
        userDao.addBookTime(userId,bookId);
    }

    public List<UserBookList> getHotBookList() {
        return userDao.getHotBookList();
    }

    public List<Talking> getTalkingList(Integer pageNum) {
        return userDao.getTalkingList(pageNum);
    }

    public List<Talking> selectTalkingByTalkingName(String key) {
        return userDao.selectTalkingByTalkingName(key);
    }

    public List<Talking> selectTalkingByUserId(Integer userId) {
        return userDao.selectTalkingByUserId(userId);
    }

    public List<TalkingSubject> getTalkingSubject() {
        return userDao.getTalkingSubject();
    }

    public void saveNewTalking(String title, Integer userId, String userName, String textarea, String date, String subject, String fileName) {
        userDao.saveNewTalking(title,userId,userName,textarea,date,subject,fileName);
    }

    public Talking getTalkingById(Integer id) {
        return userDao.getTalkingById(id);
    }

    public void deleteTalkingById(Integer id) {
        userDao.deleteTalkingById(id);
    }

    public List<Message> getUserMessage(Integer userId) {
        return userDao.getUserMessage(userId);
    }

    public void saveMessage(Message message) {
        userDao.saveMessage(message);
    }
}
