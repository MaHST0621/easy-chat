package com.example.easyChat.server.dao;

import com.example.easyChat.server.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/17
 */
@Mapper
public interface UserRepository {
    User getUserBymobile(String mobile);

    User getUserById(Long uId);

    List<User> getUsers(List<Long> userIds);
}
