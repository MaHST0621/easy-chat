package com.example.easyChat.server.service;

import com.example.easyChat.server.dao.MessageContactRepository;
import com.example.easyChat.server.dao.MessageContentRepository;
import com.example.easyChat.server.dao.MessageRelationRepository;
import com.example.easyChat.server.model.Message;
import com.example.easyChat.server.model.MessageRelation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private MessageRelationRepository messageRelationRepository;
    @Autowired
    private MessageContactRepository messageContactRepository;
    @Autowired
    private MessageContentRepository messageContentRepository;

    public Message getById(final Long messageId) {
        if ( null == messageId ) {
            log.info("消息ID为空");
            return null;
        }
        return messageContentRepository.getMesById(messageId);
    }

    /**
     * 给消息落库
     * @param message
     * @return
     */
    @Transactional
    public Message add(final Message message) {
        if ( null == message ) {
            log.info("传入的消息实体为空");
        }
        //消息落库
        messageContentRepository.insert(message);
        Long messageId = message.getMId();
        log.info("要插入的ID为{}",messageId);

        //发送方消息落库
        MessageRelation messageRelation_sender = new MessageRelation();
        messageRelation_sender.setMId(messageId);
        messageRelation_sender.setOwnerId(message.getSenderId());
        messageRelation_sender.setOtherId(message.getRecipientId());
        //0:为发送方 1:接收方
        messageRelation_sender.setType(0);
        messageRelationRepository.insert(messageRelation_sender);

        //发送方消息落库
        MessageRelation messageRelation_rec = new MessageRelation();
        messageRelation_rec.setMId(messageId);
        messageRelation_rec.setOwnerId(message.getRecipientId());
        messageRelation_rec.setOtherId(message.getSenderId());
        messageRelation_rec.setType(1);
        messageRelationRepository.insert(messageRelation_rec);

        return message;
    }


    public List<MessageRelation> fetchHistory(final Long userId,final Long otherId) {
        if ( null == userId || null == otherId) {
            log.info("查询用户ID为空");
            return null;
        }
        List<MessageRelation> res = messageRelationRepository.fetchHistory(userId,otherId);
        return res;
    }
}
