package com.example.easyChat.server.service;

import com.example.easyChat.server.dao.UserRepository;
import com.example.easyChat.server.model.User;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据mobile 验证用户是否注册
     * @param mobile
     * @param passwd
     * @return
     */
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


    public User getUserById(Long fromUserId) {
        return userRepository.getUserById(fromUserId);
    }


    public List<User> listUsers(List<Long> userIds) {
        List<User> res = userRepository.getUsers(userIds);
        return res;
    }
}
