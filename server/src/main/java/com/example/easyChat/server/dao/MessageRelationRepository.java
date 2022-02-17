package com.example.easyChat.server.dao;

import com.example.easyChat.server.model.MessageRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/17
 */
@Mapper
public interface MessageRelationRepository {
    /**
     * 获取与某用户的聊天记录
     * @param userId
     * @param otherId
     * @return 聊天记录
     */
    List<MessageRelation> fetchHistory(Long userId, Long otherId);


    /**
     * 消息记录落库
     * @param messageRelation
     */
    void insert(MessageRelation messageRelation);
}
