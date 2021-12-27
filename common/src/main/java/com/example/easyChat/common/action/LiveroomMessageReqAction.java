package com.example.easyChat.common.action;

import lombok.Data;

import java.util.UUID;

/**
 * 发送弹幕请求
 */

@Data
public class LiveroomMessageReqAction extends Action{
    public LiveroomMessageReqAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_LIVEROOM_MESSAGE_REQ.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    /**
     *  直播间Id
     */
    private String LiverommId;


    /**
     * 发送者Id
     */
    private String fromId;


    /**
     * 发送的消息内容
     */
    private String message;
}
