package com.example.easyChat.server.dao;

import com.example.easyChat.server.model.OfflineMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/20
 */
@Mapper
public interface OfflineMessageRepository {
    /**
     * 离线消息落库
     * @param offlineMessage
     */
    void insert(OfflineMessage offlineMessage);


    /**
     * 获取离线消息
     * @param userId
     * @return
     */
    List<OfflineMessage> getMessages(Long userId);
}
