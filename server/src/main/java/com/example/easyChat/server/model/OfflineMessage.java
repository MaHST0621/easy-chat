package com.example.easyChat.server.model;

import lombok.Data;

import java.util.Date;

/**
 * @description: 离线消息存储
 * @author：异想天开的咸鱼
 * @date: 2022/2/20
 */
@Data
public class OfflineMessage {
    /**
     * 消息ID
     */
    private Long mId;

    /**
     * 发送用户ID
     */
    private Long senderId;

    /**
     * 接收用户ID
     */
    private Long recipientId;

    /**
     * 0:文本消息 1:表情包消息 2:语音消息 3:图片消息
     */
    private Integer msgType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;
}
