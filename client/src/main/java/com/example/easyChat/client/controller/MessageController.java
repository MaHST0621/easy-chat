package com.example.easyChat.client.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.client.Api.MessageService;
import com.example.easyChat.client.ClientApplication;
import com.example.easyChat.client.websocket.WebSocketClient;
import com.example.easyChat.common.action.SendMessageReqAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @RequestMapping("/test")
    @ResponseBody
    public String SendMessage(@RequestBody HashMap<String,String> map) {
        System.out.println(map);
        SendMessageReqAction reqAction = JSONObject.parseObject(JSONObject.toJSONString(map),SendMessageReqAction.class);
        System.out.println(reqAction);
        WebSocketClient client = ClientApplication.getClient();
        return "success";
    }
}
