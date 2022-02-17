package com.example.easyChat.common.action;

import lombok.Data;

@Data
public class Action {
    /**
     * 动作类型
     */
    public String action;
    /**
     * 动作类型
     */
    public String actionType;
    /**
     * 请求ID
     */
    public String requestId;
    /**
     * Token
     */
    public String token;
    /**
     * Jason格式
     */
    private String payload;
}
