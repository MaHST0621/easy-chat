package com.example.easyChat.server.dao;

import com.example.easyChat.server.model.Message;
import org.apache.ibatis.annotations.Mapper;

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
}
