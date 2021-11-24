package com.example.easyChat.client.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.LoginRespAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;

public class LoginEvent implements IEvent<Action,Action>{
    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("receive action:" + action);
        LoginRespAction respAction = JSONObject.parseObject(action.getPayload(),LoginRespAction.class);
        System.out.println("received login respAction:" + respAction);
        return null;
    }
}
