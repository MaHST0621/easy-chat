package com.example.easyChat.server.service;

import com.example.easyChat.server.model.Message;
import com.example.easyChat.server.model.MessageRelation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/18
 */
public interface MessageService {
    /**
     * 获取消息
     * @param messageId
     * @return
     */
    Message getById(Long messageId);

    /**
     * 将消息落库
     * @param message
     * @return
     */
    @Transactional
    Long add(Message message,boolean messageStatus);

    /**
     * 查找聊天记录
     * @param userId
     * @param otherId
     * @return
     */
    List<String> fetchHistory(Long userId, Long otherId);


    /**
     * 将消息发布到redis
     * @param message
     */
    void publicMessage(Message message);

    /**
     * 获取是否有离线消息
     * @param userId
     */
    void checkOfflineMessage(Long userId);
}
