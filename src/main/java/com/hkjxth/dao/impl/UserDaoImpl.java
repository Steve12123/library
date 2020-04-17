package com.hkjxth.dao.impl;

import com.hkjxth.bean.*;
import com.hkjxth.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Integer registeredThenReturnUserBy(String username, String password) {
        Integer userId=null;
        try{
            userId=jdbcTemplate.queryForObject("select user_id from user where user_name=? and user_password=?",new Object[] {username,password},Integer.class);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return userId;
    }

    @Override
    public User findByIdAndName(Integer id, String password) {
        User user=null;
        try{
            user=(User) jdbcTemplate.queryForObject("select * from user where user_id=? and user_password=?",new Object[] {id,password},new BeanPropertyRowMapper<>(User.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return user;
    }

    @Override
    public List<BookSubject> selectFourSubject() {
        List<BookSubject> booklist=null;
        try{
            booklist=jdbcTemplate.query("select * from booksubject limit 0,4",new BeanPropertyRowMapper<>(BookSubject.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return booklist;
    }

    @Override
    public List<BookSubject> selectAllSubjects() {
        List<BookSubject> booklist;
        try{
            booklist=jdbcTemplate.query("select * from booksubject",new BeanPropertyRowMapper<>(BookSubject.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return booklist;
    }

    @Override
    public Active selectNewBoard() {
        HashOperations<String,String,Object> hashOperations=redisTemplate.opsForHash();
        Active active= (Active) hashOperations.get("active",1);
        return active;
    }

    @Override
    public List<Book> getDayBook() {
        List<Book> list=null;
        try{
            list=jdbcTemplate.query("select * from book ORDER BY RAND() LIMIT 4",new BeanPropertyRowMapper<>(Book.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    public boolean isNameHasRegister(String name) {
        try{
            jdbcTemplate.queryForObject("select * from user where user_name=?",new Object[]{name},new BeanPropertyRowMapper<>(User.class));
        }catch (EmptyResultDataAccessException e){
            return true;
        }
        return false;
    }

    @Override
    public void registerUser(User user) {
        jdbcTemplate.update("insert into user values(default,?,?,?,null,?)",new Object[]{user.getUserName(),user.getUserRealname(),user.getUserSex(),user.getUserPassword()});
    }

    @Override
    public List<Book> getNewUpdateBook() {
        List<Book> list=jdbcTemplate.query("select * from book ORDER BY book_id DESC LIMIT 0,5",new BeanPropertyRowMapper<>(Book.class));
        return list;
    }

    @Override
    public Integer getThisBookCount(Integer bookId) {
        Integer count=jdbcTemplate.queryForObject("select book_count from book where book_id=?",new Object[]{bookId},Integer.class);
        return count;
    }

    @Override
    @Transactional
    public Boolean bringBook(Integer userId, String bookName, Integer bookId, String date) {
        try{
            jdbcTemplate.queryForObject("select * from user_book_list_record where user_id=? and book_id=?",new Object[]{userId,bookId},new BeanPropertyRowMapper<>(UserBookList.class));
        }catch (EmptyResultDataAccessException e){
            jdbcTemplate.update("insert into user_book_list_record values(default,?,?,?,?,15,3)",new Object[]{userId,bookId,bookName,date});
            jdbcTemplate.update("update book set book_count=book_count-1 where book_id=?",new Object[]{bookId});
            return true;
        }
        return false;
    }

    @Override
    public User getUserById(Integer id) {
        User user;
        try{
            user=jdbcTemplate.queryForObject("select * from user where user_id=?",new Object[]{id},new BeanPropertyRowMapper<>(User.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return user;
    }

    @Override
    public String getPhoto(Integer id) {
        String photo;
        try{
            photo=jdbcTemplate.queryForObject("select user_photo from userphoto where user_id=?",new Object[]{id},String.class);
        }catch (EmptyResultDataAccessException e){
            return "empty";
        }
        return photo;
    }

    @Override
    public void savePhoto(Integer userId, String photoName) {
        jdbcTemplate.update("update userphoto set user_photo=? where user_id=?",new Object[]{photoName,userId});
    }

    @Override
    public void registerUserPhotoTable(Integer userId) {
        jdbcTemplate.update("insert into userphoto values(?,default)",new Object[]{userId});
    }

    @Override
    public void updateUserInfo(Integer userId, String changeName, String sex, String subject) {
        jdbcTemplate.update("update user set user_name=?, user_sex=?, user_subject=? where user_id=?",new Object[]{changeName,sex,subject,userId});
    }

    @Override
    public String checkPassword(Integer userId) {
        String result=jdbcTemplate.queryForObject("select user_password from user where user_id=?",new Object[]{userId},String.class);
        return result;
    }

    @Override
    public void changeUserPassword(Integer userId, String newPassword) {
        jdbcTemplate.update("update user set user_password=? where user_id=?",new Object[]{newPassword,userId});
    }

    @Override
    public List<UserBookList> getUserBookList(Integer userId) {
        List<UserBookList> list;
        try{
            list=jdbcTemplate.query("select * from user_book_list_record where user_id=?",new Object[]{userId},new BeanPropertyRowMapper<>(UserBookList.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    public Book getLibraryBookById(Integer bookId) {
        return jdbcTemplate.queryForObject("select * from book where book_id=?",new Object[]{bookId},new BeanPropertyRowMapper<>(Book.class));
    }

    @Override
    public void addBookTime(Integer userId, Integer bookId) {
        jdbcTemplate.update("update user_book_list_record set read_day=read_day+15, add_book_time=add_book_time-1 where user_id=? and book_id=?",new Object[]{userId,bookId});
    }

    @Override
    public List<UserBookList> getHotBookList() {
        List<UserBookList> set;
        try{
            set= jdbcTemplate.query("select u.book_id ,u.book_name ,count(*) count \n" +
                    "from user_book_list_record u \n" +
                    "GROUP BY u.book_name \n" +
                    "having count>0\n" +
                    "limit 5",new BeanPropertyRowMapper<>(UserBookList.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return set;
    }

    @Override
    public List<Talking> getTalkingList(Integer pageNum) {
        List<Talking> list;
        try{
            list=jdbcTemplate.query("select * from talking limit ?,5",new Object[]{pageNum},new BeanPropertyRowMapper<>(Talking.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    public List<Talking> selectTalkingByTalkingName(String key) {
        String keys="%"+key+"%";
        List<Talking> list;
        try{
            list=jdbcTemplate.query("select * from talking where talking_title like ?",new Object[]{keys},new BeanPropertyRowMapper<>(Talking.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    public List<Talking> selectTalkingByUserId(Integer userId) {
        List<Talking> list;
        try{
            list=jdbcTemplate.query("select * from talking where talking_master_id=?",new Object[]{userId},new BeanPropertyRowMapper<>(Talking.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    public List<TalkingSubject> getTalkingSubject() {
        List<TalkingSubject> list;
        try{
            list=jdbcTemplate.query("select * from talkingsubject",new BeanPropertyRowMapper<>(TalkingSubject.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    public void saveNewTalking(String title, Integer userId, String userName, String textarea, String date, String subject, String fileName) {
        jdbcTemplate.update("insert into talking values(default,?,?,?,?,?,?,?)",new Object[]{title,userId,userName,textarea,date,subject,fileName});
    }

    @Override
    public Talking getTalkingById(Integer id) {
        return jdbcTemplate.queryForObject("select * from talking where talking_id=?",new Object[]{id},new BeanPropertyRowMapper<>(Talking.class));
    }

    @Override
    public void deleteTalkingById(Integer id) {
        jdbcTemplate.update("delete from talking where talking_id=?",new Object[]{id});
    }

    @Override
    public List<Message> getUserMessage(Integer userId) {
        List<Message> list=new ArrayList<>();
        try{
            list=jdbcTemplate.query("select * from message where user_id=? order by message_id desc",new Object[]{userId},new BeanPropertyRowMapper<>(Message.class));
        }catch (EmptyResultDataAccessException e){
            return list;
        }
        return list;
    }

    @Override
    public void saveMessage(Message message) {
        jdbcTemplate.update("insert into message values(default,?,?,?,?,?)",new Object[]{message.getUserId(),message.getMessageTitle(),message.getMessageDate(),message.getMessageDatabase(),message.getMessageBody()});
    }

}
