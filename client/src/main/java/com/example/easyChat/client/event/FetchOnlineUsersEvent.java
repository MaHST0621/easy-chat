package com.example.easyChat.client.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.FetchOnlineUsersRespAction;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchOnlineUsersEvent implements IEvent<Action, Action> {
    private Logger logger = LoggerFactory.getLogger(FetchOnlineUsersEvent.class);
    @Override
    public Action handle(Action action, Channel channel) {
        FetchOnlineUsersRespAction respAction = JSONObject.parseObject(action.getPayload(),FetchOnlineUsersRespAction.class);
        if (!respAction.isResult()) {
            logger.info("收到失败的响应{}",respAction);
        }
        logger.info("在线用户为:{}", JSON.toJSONString(respAction.getUsers()));
        return null;
    }
}
