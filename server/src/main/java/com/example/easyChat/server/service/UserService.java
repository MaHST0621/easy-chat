package com.example.easyChat.server.service;

import com.example.easyChat.server.mapper.UserMapper;
import com.example.easyChat.server.model.User;
import com.sun.istack.internal.NotNull;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 根据mobile 和 passwd  查询用户
     * @param mobile
     * @param passwd
     * @return
     */
    public User find(final String mobile, final String passwd) {
        if (StringUtil.isNullOrEmpty(mobile)) {
            System.out.println("mobile is empty!");
            return null;
        }
        if (StringUtil.isNullOrEmpty(passwd)) {
            System.out.println("passwd is empty!");
            return null;
        }

        Example example = new Example(User.class);
        example.createCriteria()
                .andEqualTo(User.MOBILE,mobile)
                .andEqualTo(User.PASSWD,passwd);
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            System.out.println("user not be find by mobile: " + mobile);
            return null;
        }
        if (users.size() != 1) {
            System.out.println("user duplicated with mobile: " + mobile + "and passwd:" + passwd);
            return null;
        }

        return users.get(0);
    }
}
