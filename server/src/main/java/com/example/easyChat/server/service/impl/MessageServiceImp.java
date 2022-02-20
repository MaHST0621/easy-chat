package com.example.easyChat.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.server.Constants;
import com.example.easyChat.server.dao.MessageContactRepository;
import com.example.easyChat.server.dao.MessageContentRepository;
import com.example.easyChat.server.dao.MessageRelationRepository;
import com.example.easyChat.server.dao.OfflineMessageRepository;
import com.example.easyChat.server.model.Message;
import com.example.easyChat.server.model.MessageRelation;
import com.example.easyChat.server.model.OfflineMessage;
import com.example.easyChat.server.service.MessageService;
import com.example.easyChat.server.util.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MessageServiceImp implements MessageService {
    @Autowired
    private MessageRelationRepository messageRelationRepository;
    @Autowired
    private MessageContactRepository messageContactRepository;
    @Autowired
    private MessageContentRepository messageContentRepository;
    @Autowired
    private OfflineMessageRepository offlineMessageRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
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
     * @param messageStatus 是否要为消息进行离线存储
     * @return
     */
    @Transactional
    @Override
    public Long add(final Message message,final boolean messageStatus) {
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

        //判断是否要为该消息进行离线存储
        if(!messageStatus) {
            OfflineMessage offlineMessage = ConvertUtils.convertToOfflineMessage(message);
            offlineMessageRepository.insert(offlineMessage);
        }

        return messageId;
    }

    /**
     * 查找聊天记录
     * @param userId 查询方
     * @param otherId 对方
     * @return
     */
    @Override
    public List<String> fetchHistory(final Long userId,final Long otherId) {
        if ( null == userId || null == otherId) {
            log.info("查询用户ID为空");
            return null;
        }
        List<MessageRelation> res = messageRelationRepository.fetchHistory(userId,otherId);
        if (res == null) return new ArrayList<String>();
        List<Long> ids = new ArrayList<>();
        for (MessageRelation messageRelation : res) {
            ids.add(messageRelation.getMId());
        }
        List<Message> messages = messageContentRepository.getMessages(ids);
        List<String> res_messages = new ArrayList<>();
        for(Message mes : messages) {
            res_messages.add(mes.getContent());
        }
        return res_messages;
    }

    /**
     * @param message
     */
    @Override
    public void publicMessage(Message message) {
        stringRedisTemplate.convertAndSend(Constants.WEBSOCKET_MSG_TOPIC, JSONObject.toJSONString(message));
    }

    @Override
    public void checkOfflineMessage(Long userId) {
        if(userId == null) {
            log.info("传入的用户ID为空");
            return;
        }

        List<OfflineMessage> messageList = offlineMessageRepository.getMessages(userId);
        if(messageList.size() == 0) {
            log.info("没有可处理的离线消息");
            return;
        }

        for (OfflineMessage offlineMessage : messageList) {
            Message message = ConvertUtils.convertToMessage(offlineMessage);
            publicMessage(message);
        }
    }
}
