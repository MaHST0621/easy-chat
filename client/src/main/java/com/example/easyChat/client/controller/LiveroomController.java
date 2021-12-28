package com.example.easyChat.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.client.ClientApplication;
import com.example.easyChat.client.websocket.WebSocketClient;
import com.example.easyChat.common.action.LiveroomCreateReqAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/liveroom")
public class LiveroomController {

    @PostMapping("/create")
    @ResponseBody
    public void create(@RequestParam("userId") String userId,@RequestParam("description") String description) {
        LiveroomCreateReqAction reqAction = new LiveroomCreateReqAction();
        reqAction.setDescription(description);
        reqAction.setUserId(userId);
        reqAction.setPayload(JSONObject.toJSONString(reqAction));
        WebSocketClient client = ClientApplication.getClient();
        client.send(reqAction,reqAction.getPayload());
    }
}
