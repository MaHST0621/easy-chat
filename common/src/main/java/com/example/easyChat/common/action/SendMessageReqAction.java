package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class SendMessageReqAction extends Action{
    public SendMessageReqAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_SEND_MESSAGE_REQ.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private Long toUserId;

    private Long fromUserId;

    private String messageType;

    private String message;
}
