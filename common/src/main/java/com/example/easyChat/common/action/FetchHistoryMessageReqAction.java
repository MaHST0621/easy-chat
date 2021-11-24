package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class FetchHistoryMessageReqAction extends Action{
    public FetchHistoryMessageReqAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_FETCH_HISTORY_MESSAGE_REQ.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private Long toUserId;
}
