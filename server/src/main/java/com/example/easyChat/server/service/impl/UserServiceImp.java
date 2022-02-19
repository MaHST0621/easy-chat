package com.example.easyChat.server.service.impl;

import com.example.easyChat.server.dao.UserRepository;
import com.example.easyChat.server.model.User;
import com.example.easyChat.server.service.UserService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User find(final String mobile) {
        if (StringUtil.isNullOrEmpty(mobile)) {
            log.info("手机号为空");
            return null;
        }
        User user = userRepository.getUserBymobile(mobile);
        if (user == null) {
            log.info("该手机号未注册");
            return null;
        }

        return user;
    }

    @Override
    public User getUserById(Long fromUserId) {
        return userRepository.getUserById(fromUserId);
    }

    @Override
    public List<User> listUsers(List<Long> userIds) {
        List<User> res = userRepository.getUsers(userIds);
        return res;
    }
}
