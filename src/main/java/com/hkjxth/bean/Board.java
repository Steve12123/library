package com.hkjxth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 公告属性
 */
public class Board {
    private int boardId;
    private String boardTitle;
    private String boardBody;
    private String boardDate;
    private String boardActpicture;
}
