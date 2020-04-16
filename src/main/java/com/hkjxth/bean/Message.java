package com.hkjxth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Integer messageId;
    private Integer userId;
    private String messageTitle;
    private String messageDate;
    private String messageDatabase;
    private String messageBody;
}
