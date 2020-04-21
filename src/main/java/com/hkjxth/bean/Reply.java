package com.hkjxth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
    private Integer replyId;
    private Integer replyUserId;
    private String replyUserName;
    private String replyBody;
    private String replyDate;
}
