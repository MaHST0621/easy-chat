package com.example.easyChat.client.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.ReceiveMessageNotifyAckAction;
import com.example.easyChat.common.action.ReceiveMessageNotifyAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;

public class RecieveMessageEvent implements IEvent<Action, ReceiveMessageNotifyAckAction> {
    @Override
    public ReceiveMessageNotifyAckAction handle(Action action, Channel channel) {
        System.out.println("received  action:" + action);
        ReceiveMessageNotifyAction notifyAction = JSONObject.parseObject(action.getPayload(),ReceiveMessageNotifyAction.class);
        System.out.println("received  message:" + action);
        ReceiveMessageNotifyAckAction ackAction = new ReceiveMessageNotifyAckAction();
        ackAction.setMessageId(notifyAction.getMessageId());
        ackAction.setPayload(JSONObject.toJSONString(ackAction));
        return ackAction;
    }
}
