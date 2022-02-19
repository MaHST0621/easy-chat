package com.example.easyChat.client.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.ReceiveMessageNotifyAckAction;
import com.example.easyChat.common.action.ReceiveMessageNotifyAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RecieveMessageEvent implements IEvent<Action, Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        ReceiveMessageNotifyAction notifyAction = JSONObject.parseObject(action.getPayload(),ReceiveMessageNotifyAction.class);
        log.info("{}:{}",notifyAction.getFromUserId(),notifyAction.getMessage());


        ReceiveMessageNotifyAckAction ackAction = new ReceiveMessageNotifyAckAction();
        ackAction.setMessageId(notifyAction.getMessageId());
        ackAction.setPayload(JSONObject.toJSONString(ackAction));
        return ackAction;
    }
}
