package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.ReceiveMessageNotifyAckAction;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.connection.ConnectionPool;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/18
 */
@Slf4j
public class RecieveAckMessage implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        ReceiveMessageNotifyAckAction res = JSONObject.parseObject(action.getPayload(),ReceiveMessageNotifyAckAction.class);
        log.info("Id为{}的消息ACK已收到",res.getMessageId());
        ConnectionPool.getInstance().checkAndDelAck(channel,res.getMessageId());
        return null;
    }
}
