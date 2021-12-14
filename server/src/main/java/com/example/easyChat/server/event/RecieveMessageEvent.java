package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.ReceiveMessageNotifyAckAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;

public class RecieveMessageEvent implements IEvent<Action, ReceiveMessageNotifyAckAction> {
    @Override
    public ReceiveMessageNotifyAckAction handle(Action action, Channel channel) {
        System.out.println("received  action:" + action);
        ReceiveMessageNotifyAckAction ackAction = JSONObject.parseObject(action.getPayload(),ReceiveMessageNotifyAckAction.class);
        System.out.println("received  message ack: " + ackAction);
        return null;
    }
}
