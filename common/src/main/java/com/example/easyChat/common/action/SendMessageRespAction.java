package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;


/**
 * @author m1186
 * @date 2022/02/17
 * 发送请求响应
 */
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
