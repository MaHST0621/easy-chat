package com.example.easyChat.server.service;

import com.example.easyChat.server.model.User;

import java.util.List;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/18
 */
public interface UserService {
    /**
     * 根据mobile 验证用户是否注册
     * @param mobile
     * @return
     */
    User find(String mobile);

    /**
     * 根据Id获取用户
     * @param fromUserId
     * @return
     */
    User getUserById(Long fromUserId);

    /**
     * 根据ID集合获取用户
     * @param userIds
     * @return
     */
    List<User> listUsers(List<Long> userIds);
}
