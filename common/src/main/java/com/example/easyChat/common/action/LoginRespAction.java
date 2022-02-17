package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

/**
 * @author m1186
 * @date 2022/02/17
 * 登录响应
 */
@Data
@ToString
public class LoginRespAction extends Action{
    public LoginRespAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_LOGIN_RESP.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private Boolean result;

    private String userId;
}
