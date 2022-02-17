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
import com.example.easyChat.server.util.JWTUtil;
import com.example.easyChat.server.util.SpringContextUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
public class  SendMessageEvent implements IEvent<Action, Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        if (action == null) {
            log.info("发送Action不能为空");
        }
        if (channel == null) {
            log.info("发送Channel不能为空");
        }
        SendMessageReqAction reqAction = JSONObject.parseObject(action.getPayload(),SendMessageReqAction.class);
        SendMessageRespAction respAction = new SendMessageRespAction();
        //通过Channel获取from_user_id (校验用户，如果连接时启用了校验就不用这一步)
        //TODO: 添加登录校验 删除这一步
        if (!JWTUtil.checkToken(action.getToken())) {
            log.info("收到的token不存在{}",action.getToken());
            return respAction;
        }

        Long from_id = ConnectionPool.getInstance().getUserIdByChannel(channel.id().asLongText());
        if (from_id == null) {
            log.info("发送者ID为空");
            respAction.setPayload(JSONObject.toJSONString(action));
            return respAction;
        }

        //判断from_user_id 是否存在用户信息
        UserService userService = SpringContextUtil.getBean(UserService.class);
        User userFrom = userService.getUserById(from_id);
        log.info("用户信息：{}",userFrom);
        if (userFrom == null) {
            log.info("发送者信息不存在,id为{}",from_id);
            respAction.setPayload(JSONObject.toJSONString(action));
            return respAction;
        }

        //判断to_user_id 是否存在用户信息
        User userTo = userService.getUserById(reqAction.getToUserId());
        if (userFrom == null) {
            log.info("接收者信息不存在");
            respAction.setPayload(JSONObject.toJSONString(action));
            return respAction;
        }

        //将消息 插入到 数据库中
        Message message = new Message();
        message.setSenderId(from_id);
        message.setRecipientId(reqAction.getToUserId());
        message.setContent(reqAction.getMessage());
        message.setMsgType(Integer.valueOf(reqAction.getMessageType()));
        MessageService messageService = SpringContextUtil.getBean(MessageService.class);
        messageService.add(message);

        // Tips: 可选项。如果在线，就是接收，不在线就是离线消息
        // 找到接收方的链接对象
        // 发送消息
        List<Channel> channels = ConnectionPool.getInstance().getChannels(userTo.getUId());
        if ( !CollectionUtils.isEmpty(channels) ) {
            ReceiveMessageNotifyAction notifyAction = new ReceiveMessageNotifyAction();
            notifyAction.setFromUserId(from_id);
            notifyAction.setMessageId(message.getMId());
            notifyAction.setMessageType(message.getMsgType());
            notifyAction.setMessage(message.getContent());
            notifyAction.setMobile(userFrom.getMobile());
            notifyAction.setPayload(JSONObject.toJSONString(notifyAction));
            channels.stream().forEach(toChannel -> {
                if ( null == toChannel ) {
                    log.info("接收方Channel为空");
                    return ;
                }
                toChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(notifyAction)));
            });
        }
        // 返回发送结果
        respAction.setMessageId(message.getMId());
        respAction.setResult(true);
        respAction.setPayload(JSONObject.toJSONString(respAction));
        return respAction;
    }
}
