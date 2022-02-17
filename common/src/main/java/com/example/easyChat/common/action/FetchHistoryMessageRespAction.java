package com.example.easyChat.common.action;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;


/**
 * @author m1186
 * @date 2022/02/17
 * 查询历史记录响应
 */
@Data
@ToString
public class FetchHistoryMessageRespAction extends Action{
    public FetchHistoryMessageRespAction() {
        this.setActionType("");
        this.setAction(ActionEnum.ACTION_FETCH_HISTORY_MESSAGE_RESP.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private boolean result;

    //TODO
}
