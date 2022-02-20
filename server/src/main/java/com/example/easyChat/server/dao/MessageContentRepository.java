package com.example.easyChat.server.dao;

import com.example.easyChat.server.model.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/17
 */
@Mapper
public interface MessageContentRepository {
    /**
     *
     * @param messageId
     * @return
     */
    Message getMesById(Long messageId);

    /**
     * 传入
     * @param message
     */
    Long insert(Message message);

    /**
     * 消息
     * @param ids 查询消息ID
     * @return
     */
    List<Message> getMessages(List<Long> ids);
}
