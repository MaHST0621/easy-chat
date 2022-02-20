package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.FetchOnlineUsersReqAction;
import com.example.easyChat.common.action.FetchOnlineUsersRespAction;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.common.vo.UserItem;
import com.example.easyChat.server.connection.ConnectionPool;
import com.example.easyChat.server.model.User;
import com.example.easyChat.server.service.impl.UserServiceImp;
import com.example.easyChat.server.util.JWTUtil;
import com.example.easyChat.server.util.SpringContextUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FetchOnlineUsersEvent implements IEvent<Action, Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        FetchOnlineUsersReqAction reqAction = JSONObject.parseObject(action.getPayload(),FetchOnlineUsersReqAction.class);
        FetchOnlineUsersRespAction respAction = new FetchOnlineUsersRespAction();
        if (!JWTUtil.checkToken(action.getToken())) {
            log.info("传入的token为:",reqAction.getToken());
            return respAction;
        }


        List<Long> userIds = ConnectionPool.getInstance().getOnlineUsers();
        UserServiceImp userService = SpringContextUtil.getBean(UserServiceImp.class);
        List<User> users = userService.listUsers(userIds);


        if ( !CollectionUtils.isEmpty(users)) {
            respAction.setUsers(users.stream().map(user -> {
                UserItem userItem = new UserItem();
                userItem.setId(user.getUId());
                userItem.setMobile(user.getMobile());
                return userItem;
            }).collect(Collectors.toList()));
        }
        respAction.setResult(true);
        respAction.setPayload(JSONObject.toJSONString(respAction));
        return respAction;
    }
}
