package com.example.easyChat.server.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/17
 */
@Data
public class MessageContactVO {
    private Long ownerUid;
    private String ownerAvatar;
    private String ownerName;
    private Long totalUnread;
    private List<ContactInfo> contactInfoList;

    @Data
    public class ContactInfo {
        private Long otherId;
        private String otherName;
        private String otherAvatar;
        private Long mId;
        private Integer type;
        private String content;
        private Long convUnread;
        private Date createTime;
    }
}
