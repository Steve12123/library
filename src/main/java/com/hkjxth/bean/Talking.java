package com.hkjxth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Talking {
    private Integer talkingId;
    private String talkingTitle;
    private Integer talkingMasterId;
    private String talkingMasterName;
    private String talkingBody;
    private String talkingCreateDate;
    private String talkingSubject;
    private String talkingPhoto;
}
