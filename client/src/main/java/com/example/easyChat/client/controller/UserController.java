package com.example.easyChat.client.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.client.Api.LoginService;
import com.example.easyChat.client.ClientApplication;
import com.example.easyChat.client.websocket.WebSocketClient;
import com.example.easyChat.common.action.Action;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    LoginService loginService;

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam("mobile") String mobile,@RequestParam("password") String password) {
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            return "登录参数不能为空";
        }
        Action action = loginService.login(mobile,password);
        WebSocketClient client = ClientApplication.getClient();
        client.send(action, JSONObject.toJSONString(action));
        return "success";
    }
}
