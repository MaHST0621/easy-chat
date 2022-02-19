package com.example.easyChat.server.redis;


import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.ReceiveMessageNotifyAction;
import com.example.easyChat.server.handler.MyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/18
 */
@Component
public class NewMessageListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(NewMessageListener.class);

    @Autowired
    private MyHandler myHandler;

    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    private static final RedisSerializer<String> valueSerializer = new GenericToStringSerializer(Object.class);


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = stringRedisSerializer.deserialize(message.getChannel());
        String jsonMsg = valueSerializer.deserialize(message.getBody());
        logger.info("Message Received --> pattern: {}，topic:{}，message: {}", new String(pattern), topic, jsonMsg);
        JSONObject msgJson = JSONObject.parseObject(jsonMsg);
        long otherUid = msgJson.getLong("recipientId");
        ReceiveMessageNotifyAction notifyAction = new ReceiveMessageNotifyAction();
        notifyAction.setFromUserId(msgJson.getLong("senderId"));
        notifyAction.setMessageType(4);
        notifyAction.setMessage(msgJson.getString("content"));
        notifyAction.setPayload(JSONObject.toJSONString(notifyAction));

        myHandler.pushMsg(otherUid,notifyAction);
    }
}
