package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class ReceiveMessageNotifyAckAction extends Action{
    public ReceiveMessageNotifyAckAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_RECEIVE_MESSAGE_Notify_ACK.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private String messageId;
}
