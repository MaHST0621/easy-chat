package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class FetchHistoryMessageRespAction extends Action{
    public FetchHistoryMessageRespAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_FETCH_HISTORY_MESSAGE_RESP.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    //TODO

}
