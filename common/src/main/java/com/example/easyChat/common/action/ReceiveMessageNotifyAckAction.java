package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;


/**
 * @author m1186
 * @date 2022/02/17
 * 接收信息并成功处理响应
 */
@Data
@ToString
public class ReceiveMessageNotifyAckAction extends Action{
    public ReceiveMessageNotifyAckAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_RECEIVE_MESSAGE_Notify_ACK.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private Long messageId;
}
