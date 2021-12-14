package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.FetchHistoryMessageReqAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;

/**
 * this is test
 */
public class FetchHistoryMessageEvent implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("received action: " + action);
        FetchHistoryMessageReqAction reqAction = JSONObject.parseObject(action.getPayload(),FetchHistoryMessageReqAction.class);
        System.out.println("received  req: " + reqAction);
        return null;
    }
}
