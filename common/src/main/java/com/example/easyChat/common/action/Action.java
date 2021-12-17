package com.example.easyChat.common.action;

import lombok.Data;

@Data
public class Action {
    private String action;
    private String actionType;
    private String requestId;

    /**
     * Jason格式
     */
    private String payload;


    /**
     * Token
     */

    private String token;
}
