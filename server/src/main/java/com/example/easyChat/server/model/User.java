package com.example.easyChat.server.model;

import lombok.Data;

@Data
public class User {
    /**
     * 用户ID
     */
    private Long uId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 用户头像
     */
    private String avatar;
}