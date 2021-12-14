package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.LoginReqAction;
import com.example.easyChat.common.action.LoginRespAction;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.connection.ConnectionPool;
import com.example.easyChat.server.model.User;
import com.example.easyChat.server.service.UserService;
import com.example.easyChat.server.util.SpringContextUtil;
import io.netty.channel.Channel;

public class LoginEvent implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("receive action:" + action);
        LoginReqAction reqAction = JSONObject.parseObject(action.getPayload(),LoginReqAction.class);
        System.out.println("receive login req action:" + reqAction);

        UserService userService = SpringContextUtil.getBean(UserService.class);
        if (userService == null) {System.out.println("can not find userService!");}

        LoginRespAction respAction = new LoginRespAction();
        respAction.setResult(false);
        User user = userService.find(reqAction.getMobile(),reqAction.getPassword());
        if (user == null) {
            System.out.println("mobile: " + reqAction.getMobile() + "is not exist");
            respAction.setPayload(JSONObject.toJSONString(reqAction));
            return respAction;
        }
        System.out.println("login success by mobile: " + user.getMobile());
        ConnectionPool.getInstance().add(user.getId(),channel);
        respAction.setResult(true);
        respAction.setUserId(String.valueOf(user.getId()));
        respAction.setPayload(JSONObject.toJSONString(reqAction));
        return respAction;
    }
}
