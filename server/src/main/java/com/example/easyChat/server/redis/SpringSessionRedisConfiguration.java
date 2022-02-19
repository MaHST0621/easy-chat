package com.example.easyChat.server.redis;

import com.example.easyChat.server.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/18
 */
@Configuration
public class SpringSessionRedisConfiguration {

    @Autowired
    private NewMessageListener newMessageListener;


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建 RedisTemplate 对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //
        GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);
        // 设置 RedisConnection 工厂。😈 它就是实现多种 Java Redis 客户端接入的秘密工厂。感兴趣的胖友，可以自己去撸下。
        template.setConnectionFactory(factory);

        // 使用 String 序列化方式，序列化 KEY 。
        template.setKeySerializer(new StringRedisSerializer());

        // 使用 JSON 序列化方式（库是 Jackson ），序列化 VALUE 。
        template.setValueSerializer(genericToStringSerializer);

        template.setHashKeySerializer(genericToStringSerializer);
        template.setHashValueSerializer(genericToStringSerializer);

        template.afterPropertiesSet();


        return template;
    }

    /**
     * PUB/SUB 使用的 Bean ，需要时打开。
     * @param factory
     * @return
     */
    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory factory) {
        // 创建 RedisMessageListenerContainer 对象
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        // 设置 RedisConnection 工厂。😈 它就是实现多种 Java Redis 客户端接入的秘密工厂。感兴趣的胖友，可以自己去撸下。
        container.setConnectionFactory(factory);

        // 添加监听器
        container.addMessageListener(new MessageListenerAdapter(newMessageListener), new ChannelTopic(Constants.WEBSOCKET_MSG_TOPIC));
        return container;
    }


}
