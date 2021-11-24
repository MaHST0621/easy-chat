package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class FetchOnlineUsersReqAction extends Action{
    public FetchOnlineUsersReqAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_FETCH_ONLINE_USER_REQ.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private int page;

    private int count;
}
