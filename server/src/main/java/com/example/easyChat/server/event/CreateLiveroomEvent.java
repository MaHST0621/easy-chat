package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.LiveroomCreateReqAction;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.model.Liveroom;
import com.example.easyChat.server.service.LiveroomService;
import com.example.easyChat.server.util.SpringContextUtil;
import io.netty.channel.Channel;

import java.util.Date;

public class CreateLiveroomEvent implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        if (channel == null) {
            System.out.println("传入的Channel不能为空");
            return null;
        }
        if (action == null) {
            System.out.println("传入的action不能为空");
            return null;
        }
        LiveroomCreateReqAction reqAction = JSONObject.parseObject(action.getPayload(),LiveroomCreateReqAction.class);
        LiveroomService liveroomService = SpringContextUtil.getBean(LiveroomService.class);
        Liveroom liveroom = new Liveroom();
        liveroom.setCreatedate(new Date());
        liveroom.setDescription(reqAction.getDescription());
        liveroom.setUserid(Long.valueOf(reqAction.getUserId()));
        liveroomService.create(liveroom);
        System.out.println(liveroom.getId() + " 直播间创建成功");
        return null;
    }
}
