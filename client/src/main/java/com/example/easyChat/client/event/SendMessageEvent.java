package com.example.easyChat.client.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.SendMessageRespAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;

public class SendMessageEvent implements IEvent<Action, Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("send message resp: " + action);
        SendMessageRespAction respAction = JSONObject.parseObject(action.getPayload(),SendMessageRespAction.class);
        return null;
    }
}
