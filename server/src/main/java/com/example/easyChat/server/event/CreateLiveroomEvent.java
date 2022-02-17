package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.LiveroomCreateReqAction;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.model.Liveroom;
import com.example.easyChat.server.service.LiveroomService;
import com.example.easyChat.server.util.SpringContextUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CreateLiveroomEvent implements IEvent<Action,Action> {
    private Logger logger = LoggerFactory.getLogger(CreateLiveroomEvent.class);
    @Override
    public Action handle(Action action, Channel channel) {
        if (channel == null) {
            logger.info("{}事件传入的Channel不能为空",action.getAction());
            return null;
        }
        if (action == null) {
            logger.info("{}事件传入的action不能为空",action.getAction());
            return null;
        }
        LiveroomCreateReqAction reqAction = JSONObject.parseObject(action.getPayload(),LiveroomCreateReqAction.class);
        LiveroomService liveroomService = SpringContextUtil.getBean(LiveroomService.class);
        Liveroom liveroom = new Liveroom();
        liveroom.setCreatedate(new Date());
        liveroom.setDescription(reqAction.getDescription());
        liveroom.setUserid(Long.valueOf(reqAction.getUserId()));
        liveroomService.create(liveroom);
        logger.info("{}直播间创建成功",liveroom.getId());
        return null;
    }
}
