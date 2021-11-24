package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class LoginReqAction extends Action{
    public LoginReqAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_LOGIN_REQ.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private String mobile;

    private String password;
}
