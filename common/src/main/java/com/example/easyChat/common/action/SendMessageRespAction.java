package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class SendMessageRespAction extends Action{
    public SendMessageRespAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_SEND_MESSAGE_RESP.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private Long messageId;

    private Boolean result;

}
