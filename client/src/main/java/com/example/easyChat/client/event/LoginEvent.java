package com.example.easyChat.client.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.client.handler.WebsocketHandler;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.LoginRespAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginEvent implements IEvent<Action,Action>{
    /**
     * todo: 登录成功后想服务器发送最近时间戳，检查消息完整性
     * @param action
     * @param channel
     * @return
     */
    @Override
    public Action handle(Action action, Channel channel) {
        LoginRespAction respAction = JSONObject.parseObject(action.getPayload(),LoginRespAction.class);
        log.info("收到登录响应{},token为{}",respAction,action.getToken());
        WebsocketHandler.setMyToken(action.getToken());
        return null;
    }
}
