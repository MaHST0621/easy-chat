package com.example.easyChat.client.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.ReceiveMessageNotifyAckAction;
import com.example.easyChat.common.action.ReceiveMessageNotifyAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;

public class RecieveMessageEvent implements IEvent<Action, Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("received  action:" + action);
        ReceiveMessageNotifyAction notifyAction = JSONObject.parseObject(action.getPayload(),ReceiveMessageNotifyAction.class);
        System.out.println("received  message from id { " + notifyAction.getFromUserId() + "} : " + notifyAction.getMessage());
        ReceiveMessageNotifyAckAction ackAction = new ReceiveMessageNotifyAckAction();
        ackAction.setMessageId(notifyAction.getMessageId());
        ackAction.setPayload(JSONObject.toJSONString(ackAction));
        return ackAction;
    }
}
