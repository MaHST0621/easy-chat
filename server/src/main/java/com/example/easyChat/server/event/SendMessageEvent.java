package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.ReceiveMessageNotifyAction;
import com.example.easyChat.common.action.SendMessageReqAction;
import com.example.easyChat.common.action.SendMessageRespAction;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.Constants;
import com.example.easyChat.server.connection.ConnectionPool;
import com.example.easyChat.server.model.Message;
import com.example.easyChat.server.model.User;
import com.example.easyChat.server.service.impl.MessageServiceImp;
import com.example.easyChat.server.service.impl.UserServiceImp;
import com.example.easyChat.server.util.JWTUtil;
import com.example.easyChat.server.util.SpringContextUtil;
import com.example.easyChat.server.vo.MessageVO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author m1186
 */
@Slf4j
public class  SendMessageEvent implements IEvent<Action, Action> {
    ExecutorService ownThreadPool = new ThreadPoolExecutor(2,4,20, TimeUnit.SECONDS,new LinkedBlockingQueue());
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

        if(!ConnectionPool.getInstance().getOnlineUsers().contains(from_id)) {
            log.info("未登录ID{}",from_id);
            respAction.setPayload(JSONObject.toJSONString(action));
            return respAction;
        }

        //判断from_user_id 是否存在用户信息
        UserServiceImp userService = SpringContextUtil.getBean(UserServiceImp.class);
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
        MessageServiceImp messageService = SpringContextUtil.getBean(MessageServiceImp.class);
        //异步落库
        FutureTask futureTask = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                messageService.add(message);
                return null;
            }
        });
        ownThreadPool.submit(futureTask);

        // Tips: 可选项。如果在线，就是接收，不在线就是离线消息
        // 将消息发布到redis
        messageService.publicMessage(message);

        // 返回发送结果
        respAction.setMessageId(message.getMId());
        respAction.setResult(true);
        respAction.setPayload(JSONObject.toJSONString(respAction));
        return respAction;
    }
}
