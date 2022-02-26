package com.example.easyChat.server.servicetest;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.server.dao.MessageRelationRepository;
import com.example.easyChat.server.model.MessageRelation;
import com.example.easyChat.server.util.SensitiveFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class MessageTest {
    @Autowired
    MessageRelationRepository relationRepository;

    @Autowired
    SensitiveFilter sensitiveFilter;

    @Test
    public void get() {
        List<MessageRelation> messageRelations = relationRepository.fetchHistory(2L,1L);
        for (MessageRelation messageRelation : messageRelations) {
            log.info("{}", JSONObject.toJSONString(messageRelation));
        }
    }


    @Test
    public void mytest() {
        System.out.println("start test!");
        String testWord = "我是你爹，你是个傻逼吧？ fuck your self bitch!";
        String res = sensitiveFilter.filter(testWord);
        System.out.println(res);
    }
}
