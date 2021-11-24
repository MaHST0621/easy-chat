package com.example.easyChat.client.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.FetchHistoryMessageRespAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;

public class FetchHistoryMessageEvent implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("received action: " + action);
        FetchHistoryMessageRespAction respAction = JSONObject.parseObject(action.getPayload(),FetchHistoryMessageRespAction.class);
        System.out.println("received  resp: " + respAction);
        return null;
    }
}
