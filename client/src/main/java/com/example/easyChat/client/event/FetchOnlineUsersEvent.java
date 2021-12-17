package com.example.easyChat.client.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.FetchOnlineUsersRespAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;

public class FetchOnlineUsersEvent implements IEvent<Action, Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        FetchOnlineUsersRespAction respAction = JSONObject.parseObject(action.getPayload(),FetchOnlineUsersRespAction.class);
        System.out.println(respAction.getUsers());
        return null;
    }
}
