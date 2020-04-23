package com.hkjxth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkList {
    private Integer markId;
    private Integer userId;
    private Integer bookId;
    private String bookName;
    private String startDate;
}
