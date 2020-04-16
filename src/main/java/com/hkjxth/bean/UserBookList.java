package com.hkjxth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBookList {
    private Integer messageId;
    private Integer userId;
    private Integer bookId;
    private String bookName;
    private String beginDay;
    private Integer readDay;
    private Integer addBookTime;
}
