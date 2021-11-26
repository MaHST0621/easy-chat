package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class ReceiveMessageNotifyAction extends Action{
    public ReceiveMessageNotifyAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_RECEIVE_MESSAGE_NOTIFY.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private String mobile;

    private Long fromUserId;

    private Long messageId;

    private Byte messageType;

    private String message;
}
