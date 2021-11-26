package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.FetchOnlineUsersReqAction;
import com.example.easyChat.common.action.FetchOnlineUsersRespAction;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.common.vo.UserItem;
import com.example.easyChat.server.connection.ConnectionPool;
import com.example.easyChat.server.model.User;
import com.example.easyChat.server.service.UserService;
import com.example.easyChat.server.util.SpringContextUtil;
import io.netty.channel.Channel;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class FetchOnlineUsersEvent implements IEvent<Action, Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("receive action:" + action);
        FetchOnlineUsersReqAction reqAction = JSONObject.parseObject(action.getPayload(),FetchOnlineUsersReqAction.class);
        System.out.println("receive reqAction: " + reqAction);

        List<Long> userIds = ConnectionPool.getInstance().userIdList();
        UserService userService = SpringContextUtil.getBean(UserService.class);
        List<User> users = userService.listUsers(userIds);

        FetchOnlineUsersRespAction respAction = new FetchOnlineUsersRespAction();
        if ( !CollectionUtils.isEmpty(users)) {
            respAction.setUsers(users.stream().map(user -> {
                UserItem userItem = new UserItem();
                userItem.setId(user.getId());
                userItem.setMobile(user.getMobile());
                return userItem;
            }).collect(Collectors.toList()));
        }
        return respAction;
    }
}
