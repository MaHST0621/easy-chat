package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.*;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.connection.ConnectionPool;
import com.example.easyChat.server.model.Liveroom;
import com.example.easyChat.server.model.Message;
import com.example.easyChat.server.model.User;
import com.example.easyChat.server.service.LiveroomService;
import com.example.easyChat.server.service.MessageService;
import com.example.easyChat.server.service.UserService;
import com.example.easyChat.server.util.JWTUtil;
import com.example.easyChat.server.util.SpringContextUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


public class MessageLiveroomEvent implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        if (action == null) {
            System.out.println("action can not be null");
        }
        if (channel == null) {
            System.out.println("channel can not be null");
        }
        //todo 前端编写实体类
        action.setPayload(JSONObject.toJSONString(action));

        //todo 删除sout语句
        System.out.println(action);
        System.out.println(action.getPayload());
        LiveroomMessageReqAction reqAction = JSONObject.parseObject(action.getPayload(),LiveroomMessageReqAction.class);
        //通过Channel获取from_user_id (校验用户，如果连接时启用了校验就不用这一步)
        //TODO: 添加登录校验 删除这一步
//        if (!JWTUtil.checkToken(reqAction.getToken())) {
//            System.out.println("token is not exist");
//            return null;
//        }

        //判断是否是已经连接的用户
//        Long from_id = ConnectionPool.getInstance().getUserIdByChannel(channel.id().asLongText());
//        if (from_id == null) {
//            System.out.println("please log in");
//            return null;
//        }


        //判断from_user_id 是否存在用户信息
        UserService userService = SpringContextUtil.getBean(UserService.class);
        System.out.println(reqAction);
        System.out.println(reqAction.getFromId());
        Long userId = Long.valueOf(reqAction.getFromId());
        System.out.println(userId);
        User userFrom = userService.getUserById(userId);
        if (userFrom == null) {
            System.out.println("没有该用户的信息");
            return null;
        }

        //判断直播间ID存不存在
        LiveroomService liveroomService = SpringContextUtil.getBean(LiveroomService.class);
        Liveroom liveroom = liveroomService.find(reqAction.getLiverommId());
        if (liveroom == null) {
            System.out.println("iD 为 " + reqAction.getLiverommId() + " 的直播间不存在");
            return null;
        }

        //判断直播间ID是否上线
        Channel toChannel = ConnectionPool.getInstance().getChannelByliveroomId(reqAction.getLiverommId());
        if (toChannel == null) {
            System.out.println("该直播间的channel不存在");
            return null;
        }

        //将消息 插入到 数据库中
        Message message = new Message();
        message.setFromId(Long.valueOf(reqAction.getFromId()));
        message.setToId(Long.valueOf(reqAction.getLiverommId()));
        message.setContent(reqAction.getMessage());
        message.setType(Byte.valueOf(reqAction.getActionType()));
        message.setSendTimestamp(System.currentTimeMillis());
        message.setStatus((byte) 0);
        message.setRecvTimestamp(0L);
        MessageService messageService = SpringContextUtil.getBean(MessageService.class);
        messageService.add(message);

        // 发送消息
        toChannel.writeAndFlush(new TextWebSocketFrame(reqAction.getMessage()));

        return null;
    }
}
