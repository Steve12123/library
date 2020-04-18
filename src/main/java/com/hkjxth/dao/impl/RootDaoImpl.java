package com.hkjxth.dao.impl;

import com.hkjxth.bean.*;
import com.hkjxth.dao.RootDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


@Repository
public class RootDaoImpl implements RootDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Root rootLogin(Integer id, String password) {
        Root root=null;
        try{
            root=jdbcTemplate.queryForObject("select * from root where root_id=? and root_password=?", new Object[]{id,password},new BeanPropertyRowMapper<>(Root.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return root;
    }

    @Override
    public int getBookCounts() {
        Integer list=null;
        try{
            list=jdbcTemplate.queryForObject("select sum(book_count) from book",Integer.class);
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
        return list;
    }

    @Override
    public int getBookCountsBySubject(String bookSubject) {
        int counts;
        try{
            counts=jdbcTemplate.queryForObject("select SUM(book_count) from book where book_subject=?",new Object[]{bookSubject},Integer.class);
        }catch (NullPointerException e){
            //当书库里没有该类型图书时
            return 0;
        }
        return counts;
    }

    @Override
    public List<BookSubject> getAllBookSubject() {
        List<BookSubject> list=null;
        try{
            list= jdbcTemplate.query("select * from booksubject order by subject_id",new BeanPropertyRowMapper<>(BookSubject.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    public List<Book> selectBooksBySomething(String subject, String author, String bookname) {
        if((subject==null||subject.equals(""))&&(author==null||author.equals(""))&&(bookname==null||bookname.equals(""))){
            return null;
        }
        List<Book> list=new ArrayList<>();
        try{
            if(subject!=null&&!subject.equals("")){
                List<Book> list1=jdbcTemplate.query("select * from book where book_subject=?",new Object[]{subject},new BeanPropertyRowMapper<>(Book.class));
                for(int i=0;i<list1.size();i++){
                    if (list1.get(i).getBookAuthor().contains(author)||author==null){
                        if (list1.get(i).getBookName().contains(bookname)||bookname==null){
                            list.add(list1.get(i));
                        }
                    }
                }
            }else{
                if (author!=null&&!author.equals("")){
                    List<Book> list1=jdbcTemplate.query("select * from book where book_author like ?",new Object[]{"%"+author+"%"},new BeanPropertyRowMapper<>(Book.class));
                    for(int i=0;i<list1.size();i++){
                        if (list1.get(i).getBookName().contains(bookname)||bookname==null){
                            list.add(list1.get(i));
                        }
                    }
                }else{
                    List<Book> list1=jdbcTemplate.query("select * from book where book_name like ?",new Object[]{"%"+bookname+"%"},new BeanPropertyRowMapper<>(Book.class));
                    list=list1;
                }
            }
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    public List<Book> selectAllBooksUseLimit(Integer titlePage,Integer pageSize) {
        List<Book> list=null;
        try{
            list=jdbcTemplate.query("select * from book limit ? , ?",new Object[]{titlePage,pageSize},new BeanPropertyRowMapper<>(Book.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    public Integer selectBookNumbers() {
        Integer count=0;
        try{
            count=jdbcTemplate.queryForObject("select count(*) from book",Integer.class);
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
        return count;
    }

    @Override
    public Book getBookById(int id) {
        Book book=null;
        try{
            book=jdbcTemplate.queryForObject("select * from book where book_id=?",new Object[]{id},new BeanPropertyRowMapper<>(Book.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return book;
    }

    @Override
    public void savePhoto(Integer bookId,String fileName) {
        jdbcTemplate.update("update book set book_picture=? where book_id=?",new Object[]{fileName,bookId});
    }

    @Override
    public void saveBookInfo(String bookName, String bookAuthor, Integer bookCount, String bookSubject, String bookDescribe, Integer bookId) {
        jdbcTemplate.update("update book set book_name=?, book_author=?, book_count=?, book_subject=?, book_describe=? where book_id=?",new Object[]{bookName,bookAuthor,bookCount,bookSubject,bookDescribe,bookId});
    }

    @Override
    public Integer insertNewBook(String bookName, String bookAuthor, Integer bookCount, String bookSubject, String bookDescribe,String fileName) throws SQLException {
        Integer id=0;
        Connection conn=null;
        try {
            conn=jdbcTemplate.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.setAutoCommit(false);
            jdbcTemplate.update("insert into book values(default,?,?,?,?,?,?)",new Object[]{bookName,bookAuthor,bookCount,bookSubject,fileName,bookDescribe});
            id=jdbcTemplate.queryForObject("select book_id from book where book_name=? and book_author=? and book_count=? and book_subject=?",new Object[]{bookName,bookAuthor,bookCount,bookSubject},Integer.class);
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            conn.rollback();
        }
        return id;
    }

    @Override
    public void deleteBookById(Integer id) {
        jdbcTemplate.update("delete from book where book_id=?",new Object[]{id});
    }

    @Override
    public Integer getAllUsersCount() {
        Integer count=jdbcTemplate.queryForObject("select count(*) from user",Integer.class);
        return count;
    }

    @Override
    public List<UserBookList> getUserBookListById(Integer userId) {
        List<UserBookList> list;
        try{
            list=jdbcTemplate.query("select * from user_book_list_record where user_id=?",new Object[]{userId},new BeanPropertyRowMapper<>(UserBookList.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    @Transactional
    public Boolean returnBookByUserIdAndBookId(Integer userId, Integer bookId) {
        try{
            jdbcTemplate.queryForObject("select * from user_book_list_record where user_id=? and book_id=?",new Object[]{userId,bookId},new BeanPropertyRowMapper<>(UserBookList.class));
        }catch (EmptyResultDataAccessException e){
            return false;
        }
        try{
            jdbcTemplate.update("delete from user_book_list_record where user_id=? and book_id=?",new Object[]{userId,bookId});
            jdbcTemplate.update("update book set book_count=book_count+1 where book_id=?",new Object[]{bookId});
        }catch (DuplicateKeyException e){
            return false;
        }
        return true;
    }

    @Override
    public void insertNewBoard(String advTitle, String advBody, String date, String photoName) {
        jdbcTemplate.update("insert into board values (default,?,?,?,?)",new Object[]{advTitle,advBody,date,photoName});
    }

    @Override
    public void saveActiveToRedis(String titleName, String data, String body) {
        redisTemplate.opsForHash().put("active",1,new Active(titleName,data,body));
    }

    @Override
    public Board getBoardsByCount() {
        Board board;
        try{
            board=jdbcTemplate.queryForObject("select * from board order by board_id desc limit 1",new BeanPropertyRowMapper<>(Board.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return board;
    }

    @Override
    public List<Talking> getTalkingByUserId(Integer userId) {
        List<Talking> list;
        try{
            list=jdbcTemplate.query("select * from talking where talking_master_id=?",new Object[]{userId},new BeanPropertyRowMapper<>(Talking.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return list;
    }

    @Override
    public List<Message> getAllBringBooksRecord() {
        List<Message> list=new ArrayList<>();
        try{
            list=jdbcTemplate.query("select * from message order by message_id desc",new BeanPropertyRowMapper<>(Message.class));
        }catch (EmptyResultDataAccessException e){
            return list;
        }
        return list;
    }

    @Override
    public List<UserBookList> getLeavingTimeList() {
        List<UserBookList> list=new ArrayList<>();
        try{
            list=jdbcTemplate.query("select * from user_book_list_record where read_day<0",new BeanPropertyRowMapper<>(UserBookList.class));
        }catch (EmptyResultDataAccessException e){
            return list;
        }
        return list;
    }

    @Override
    public void sendMessage(Integer userId, String title, String date, String datebaseTime, String body) {
        jdbcTemplate.update("insert into message values(default,?,?,?,?,?)",new Object[]{userId,title,date,datebaseTime,body});
    }

    @Override
    public List<Message> selectMessageWithCondition(String selectTitle, Integer selectId, String selectDate) {
        List<Message> list=new ArrayList<>();
        if ((selectTitle==null||selectTitle.equals(""))&&(selectId==null)&&(selectDate==null||selectDate.length()==0)){
            return null;
        }else{
            /*当标题条件不为空时*/
            if (selectTitle!=null&&!selectTitle.equals("")){
                List<Message> list1;
                try{
                    list1=jdbcTemplate.query("select * from message where message_title=?",new Object[]{selectTitle},new BeanPropertyRowMapper<>(Message.class));
                }catch (EmptyResultDataAccessException e){
                    return null;
                }
                for (Message message:list1){
                    Integer userId=message.getUserId();
                    String date=message.getMessageDatabase();
                    if ((selectId==null||selectId.equals(userId))&&(selectDate.equals("")||date.equals(selectDate))){
                         list.add(message);
                     }
                }
            }else{
                /*当标题条件为空时*/
                if (selectId!=null&&!selectId.equals("")){
                    List<Message> list2;
                    try{
                        list2=jdbcTemplate.query("select * from message where user_id=?",new Object[]{selectId},new BeanPropertyRowMapper<>(Message.class));
                    }catch (EmptyResultDataAccessException e){
                        return null;
                    }
                    for (Message message:list2){
                        String date=message.getMessageDatabase();
                        if (date.equals("")||date.contains(selectDate)){
                            list.add(message);
                        }
                    }
                }else{
                    /*当前两个条件都为空时*/
                    List<Message> list3;
                    try{
                        list3=jdbcTemplate.query("select * from message where message_database=?",new Object[]{selectDate},new BeanPropertyRowMapper<>(Message.class));
                    }catch (EmptyResultDataAccessException e){
                        return null;
                    }
                    list.addAll(list3);
                }
            }
        }
        return list;
    }

    @Override
    public List<User> getAllUser(Integer pageNum) {
        List<User> list=new ArrayList<>();
        try{
            list=jdbcTemplate.query("select * from user limit ?,10",new Object[]{pageNum},new BeanPropertyRowMapper<>(User.class));
        }catch (EmptyResultDataAccessException e){
             return list;
        }
        return list;
    }


}
