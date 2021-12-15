package com.example.easyChat.client.Api.Imp;


import com.example.easyChat.client.Api.FetchService;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.FetchHistoryMessageReqAction;
import com.example.easyChat.common.action.FetchOnlineUsersReqAction;
import org.springframework.stereotype.Service;

@Service
public class FetchServiceImp implements FetchService {
    @Override
    public Action fetchOnlineUsers(int page, int count) {
        FetchOnlineUsersReqAction action = new FetchOnlineUsersReqAction();
        action.setCount(page);
        action.setPage(page);
        return action;
    }

    @Override
    public Action fetchHistoryMessages(Long userId) {
        //TODO:获取聊天记录
        FetchHistoryMessageReqAction action = new FetchHistoryMessageReqAction();
        action.setToUserId(userId);
        return action;
    }
}
