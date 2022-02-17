package com.example.easyChat.server.model;

import lombok.Data;

import java.util.Date;

/**
 * @description: 最近联系人
 * @author：异想天开的咸鱼
 * @date: 2022/2/17
 */
@Data
public class UserRelation {
    /**
     * 消息拥有者1
     */
    private Long ownerId;

    /**
     * 消息拥有者2
     */
    private Long otherId;

    /**
     * 消息ID
     */
    private Long mId;

    /**
     * 消息拥有者1身份
     */
    private Integer type;

    /**
     * 消息创建时间
     */
    private Date createTime;
}
