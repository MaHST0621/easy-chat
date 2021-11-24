package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.LoginReqAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;

public class LoginEvent implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("receive action:" + action);
        LoginReqAction reqAction = JSONObject.parseObject(action.getPayload(),LoginReqAction.class);
        System.out.println("receive login req action:" + reqAction);
        return null;
    }
}
