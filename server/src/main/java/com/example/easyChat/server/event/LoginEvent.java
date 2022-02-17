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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class LoginEvent implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        LoginReqAction reqAction = JSONObject.parseObject(action.getPayload(),LoginReqAction.class);
        UserService userService = SpringContextUtil.getBean(UserService.class);
        if (userService == null) {
            log.info("userService未初始化");
        }
        LoginRespAction respAction = new LoginRespAction();
        respAction.setResult(false);

        //验证登录用户额真实性
        User user = userService.find(reqAction.getMobile());
        if (user == null) {
            log.info("手机号为{}用户不存在",reqAction.getMobile());
            respAction.setPayload(JSONObject.toJSONString(reqAction));
            return respAction;
        }

        //验证请求中是否包含密码并验证
        if (StringUtils.isEmpty(reqAction.getPassword())) {
            log.info("请求中不包含登录密码");
            respAction.setPayload(JSONObject.toJSONString(reqAction));
            return respAction;
        }else if (!reqAction.getPassword().equals(user.getPassWord())) {
            log.info("用户输入的密码错误{}",reqAction.getPassword());
            respAction.setPayload(JSONObject.toJSONString(reqAction));
            return respAction;
        }
        log.info("手机号为:{}的用户成功登录",reqAction.getMobile());


        ConnectionPool.getInstance().add(user.getUId(),channel);
        respAction.setResult(true);
        respAction.setUserId(String.valueOf(user.getUId()));
        String token = JWTUtil.create_Token(user);
        if (StringUtils.isEmpty(token)) {
            log.info("创建token失败");
        }
        respAction.setToken(token);
        respAction.setPayload(JSONObject.toJSONString(respAction));
        return respAction;
    }
}
