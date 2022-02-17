package com.example.easyChat.server.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/17
 */
@Data
public class MessageVO {
    private Long mid;
    private String content;
    private Long ownerId;
    private Integer type;
    private Long otherId;
    private Date createTime;
    private String ownerIdAvatar;
    private String otherIdAvatar;
    private String ownerName;
    private String otherName;
}
