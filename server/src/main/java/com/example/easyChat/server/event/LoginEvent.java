package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.LoginReqAction;
import com.example.easyChat.common.action.LoginRespAction;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.connection.ConnectionPool;
import com.example.easyChat.server.model.User;
import com.example.easyChat.server.service.UserService;
import com.example.easyChat.server.util.JWTUtil;
import com.example.easyChat.server.util.SpringContextUtil;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

public class LoginEvent implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        //TODO :sout 清除
        LoginReqAction reqAction = JSONObject.parseObject(action.getPayload(),LoginReqAction.class);

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
        String token = JWTUtil.create_Token(user);
        if (StringUtils.isEmpty(token)) {
            System.out.println("create token faild");
        }else {
            System.out.println("token:  " + token);
        }
        respAction.setToken(token);
        respAction.setPayload(JSONObject.toJSONString(reqAction));
        return respAction;
    }
}
