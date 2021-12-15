package com.example.easyChat.client.Api.Imp;


import com.example.easyChat.client.Api.MessageService;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.SendMessageReqAction;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImp implements MessageService {
    @Override
    public Action SendMessage(Long to_userId, Long from_userId, String Message) {
        //TODO:消息发送逻辑
        SendMessageReqAction action = new SendMessageReqAction();
        action.setToUserId(to_userId);
        action.setMessageType((byte) 0);
        action.setMessage(Message);
        action.setFromUserId(from_userId);
        return action;
    }
}
