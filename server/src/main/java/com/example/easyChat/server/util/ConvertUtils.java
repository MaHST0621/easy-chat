package com.example.easyChat.server.util;

import com.example.easyChat.server.model.Message;
import com.example.easyChat.server.model.OfflineMessage;

/**
 * @description:
 * @author：异想天开的咸鱼
 * @date: 2022/2/20
 */
public class ConvertUtils {

    public static OfflineMessage convertToOfflineMessage(Message message) {
        OfflineMessage res = new OfflineMessage();
        res.setMId(message.getMId());
        res.setContent(message.getContent());
        res.setMsgType(0);
        res.setRecipientId(message.getRecipientId());
        res.setSenderId(message.getSenderId());
        return res;
    }

    public static Message convertToMessage(OfflineMessage offlineMessage) {
        Message res = new Message();
        res.setMId(offlineMessage.getMId());
        res.setRecipientId(offlineMessage.getRecipientId());
        res.setMsgType(offlineMessage.getMsgType());
        res.setContent(offlineMessage.getContent());
        res.setSenderId(offlineMessage.getSenderId());
        return res;
    }
}
