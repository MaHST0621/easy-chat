package com.example.easyChat.server.service;

import com.example.easyChat.server.mapper.MessageMapper;
import com.example.easyChat.server.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    public Message getById(final Long messageId) {
        if ( null == messageId ) {
            System.out.println("messageId is empty!");
            return null;
        }
        return messageMapper.selectByPrimaryKey(messageId);
    }


    public Message add(final Message message) {
        if ( null == message ) {
            System.out.println("message is empty!");
        }
        messageMapper.insertUseGeneratedKeys(message);
        return message;
    }

    public void modify(final Message message) {
        if ( null == message ) {
            System.out.println("message is empty!");
        }
        messageMapper.updateByPrimaryKeySelective(message);
    }

    public List<Message> fetchHistory(final Long userId) {
        if ( null == userId ) {
            System.out.println("userId is empty!");
            return null;
        }
        Example example = new Example(Message.class);
        example.createCriteria()
                .andEqualTo(Message.FROM_ID, userId)
                .orEqualTo(Message.TO_ID, userId);
        return messageMapper.selectByExample(example);
    }
}
