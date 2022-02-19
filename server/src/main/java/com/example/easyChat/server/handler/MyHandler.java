package com.example.easyChat.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.ReceiveMessageNotifyAction;
import com.example.easyChat.server.connection.ConnectionPool;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description: 可靠机制处理器
 * @author：异想天开的咸鱼
 * @date: 2022/2/18
 */
@Slf4j
@Component
public class MyHandler {
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(50);


    /**
     * 发送消息
     * @param otherUid
     * @param msg
     */
    public void pushMsg(long otherUid, ReceiveMessageNotifyAction msg) {
        List<Channel> toChannels = ConnectionPool.getInstance().getChannels(otherUid);
        if(toChannels == null) {
            log.info("ID为{}的用户未登录",otherUid);
            return;
        }

        AtomicLong generator = null;
        for (Channel channel : toChannels) {
            generator = ConnectionPool.getInstance().getTidGeneritor(channel);
            Long tId = generator.get();
            msg.setMessageId(tId);
            msg.setPayload(JSONObject.toJSONString(msg));
            log.info("发送SEQ_ID为{},channel地址为{}",tId,channel.remoteAddress());
            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(msg))).addListener(future -> {
                if (future.isCancelled()) {
                    log.info("输入已经被成功取消");
                }else if (future.isSuccess()) {
                    log.info("消息已经被成功发送");
                    addMsgToAckBuffer(channel,JSONObject.toJSONString(msg),tId);
                }else {
                    log.info("插入过程中发生错误");
                }
            });
        }
        generator.incrementAndGet();
    }

    /**
     * 将推送的消息加入待ack列表
     *
     * @param channel
     * @param msgJson
     */
    public void addMsgToAckBuffer(Channel channel, String msgJson,Long tId) {
        ConnectionPool.getInstance().addNonAcks(channel,tId,msgJson);
        executorService.schedule(() -> {
            if (channel.isActive()) {
                checkAndResend(channel, msgJson,tId);
            }
        }, 5000, TimeUnit.MILLISECONDS);
    }

    /**
     * 检查并重推
     *
     * @param channel
     * @param msgJson
     */
    private void checkAndResend(Channel channel, String msgJson,Long tId) {
        long tid = tId;
        int tryTimes = 2;
        while (tryTimes > 0) {
            if (ConnectionPool.getInstance().checkContainsId(channel,tid) && tryTimes > 0) {
                channel.writeAndFlush(new TextWebSocketFrame(msgJson));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tryTimes--;
        }
    }
}
