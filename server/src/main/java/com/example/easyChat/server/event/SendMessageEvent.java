package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.ReceiveMessageNotifyAction;
import com.example.easyChat.common.action.SendMessageReqAction;
import com.example.easyChat.common.action.SendMessageRespAction;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.connection.ConnectionPool;
import com.example.easyChat.server.model.Message;
import com.example.easyChat.server.model.User;
import com.example.easyChat.server.service.MessageService;
import com.example.easyChat.server.service.UserService;
import com.example.easyChat.server.util.SpringContextUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class  SendMessageEvent implements IEvent<Action, Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("send message resp: " + action);
        SendMessageReqAction reqAction = JSONObject.parseObject(action.getPayload(),SendMessageReqAction.class);
        System.out.println("receive req: " + reqAction);

        SendMessageRespAction respAction = new SendMessageRespAction();
        respAction.setResult(false);
        //通过Channel获取from_user_id (校验用户，如果连接时启用了校验就不用这一步)
        //TODO: 添加登录校验 删除这一步
        Long from_id = ConnectionPool.getInstance().getUserIdByChannel(channel.id().asLongText());
        if (from_id == null) {
            System.out.println("can not find userId by channel: " + channel.id().asLongText());
            respAction.setPayload(JSONObject.toJSONString(action));
            return respAction;
        }

        //判断from_user_id 是否存在用户信息
        UserService userService = SpringContextUtil.getBean(UserService.class);
        User userFrom = userService.getUserById(reqAction.getFromUserId());
        if (userFrom == null) {
            System.out.println("can not find user_from by id : " + reqAction.getFromUserId());
            respAction.setPayload(JSONObject.toJSONString(action));
            return respAction;
        }

        //判断to_user_id 是否存在用户信息
        User userTo = userService.getUserById(reqAction.getToUserId());
        if (userFrom == null) {
            System.out.println("can not find user_to by id : " + reqAction.getToUserId());
            respAction.setPayload(JSONObject.toJSONString(action));
            return respAction;
        }

        //将消息 插入到 数据库中
        Message message = new Message();
        message.setFromId(reqAction.getFromUserId());
        message.setToId(reqAction.getToUserId());
        message.setContent(reqAction.getMessage());
        message.setType(reqAction.getMessageType());
        message.setSendTimestamp(System.currentTimeMillis());
        message.setStatus((byte) 0);
        message.setRecvTimestamp(0L);
        MessageService messageService = SpringContextUtil.getBean(MessageService.class);
        messageService.add(message);

        // Tips: 可选项。如果在线，就是接收，不在线就是离线消息
        // 找到接收方的链接对象
        // 发送消息
        List<Channel> channels = ConnectionPool.getInstance().getChannels(userTo.getId());
        if ( !CollectionUtils.isEmpty(channels) ) {
            ReceiveMessageNotifyAction notifyAction = new ReceiveMessageNotifyAction();
            notifyAction.setFromUserId(from_id);
            notifyAction.setMessageId(message.getId());
            notifyAction.setMessageType(message.getType());
            notifyAction.setMessage(message.getContent());
            notifyAction.setMobile(userFrom.getMobile());
            notifyAction.setPayload(JSONObject.toJSONString(notifyAction));
            channels.stream().forEach(toChannel -> {
                if ( null == toChannel ) {
                    System.out.println("get null toChannel");
                    return ;
                }
                toChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(notifyAction)));
            });
        }
        // 返回发送结果
        respAction.setMessageId(message.getId());
        respAction.setResult(true);
        respAction.setPayload(JSONObject.toJSONString(respAction));
        return respAction;
    }
}
