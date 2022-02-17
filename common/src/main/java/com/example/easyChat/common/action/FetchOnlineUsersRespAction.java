package com.example.easyChat.common.action;

import com.example.easyChat.common.vo.UserItem;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
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
    private List<UserItem> users;


    private boolean result;

}
