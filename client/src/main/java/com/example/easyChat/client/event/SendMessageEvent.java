package com.example.easyChat.client.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.SendMessageRespAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendMessageEvent implements IEvent<Action, Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        log.info("发送消息成功响应{}",action);
        SendMessageRespAction respAction = JSONObject.parseObject(action.getPayload(),SendMessageRespAction.class);
        return null;
    }
}
