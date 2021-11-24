package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class FetchOnlineUsersRespAction extends Action{
    public FetchOnlineUsersRespAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_FETCH_ONLINE_USER_RESP.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    //TODO： 格式结合数据库表
}
