package com.example.easyChat.common.action;

import lombok.Data;

import java.util.UUID;


/**
 * 创建直播间请求
 */
@Data
public class LiveroomCreateReqAction extends Action{
    public LiveroomCreateReqAction(){
        this.setActionType("");
        this.setRequestId(UUID.randomUUID().toString());
        this.setAction(ActionEnum.ACTION_LIVEROOM_CREATE_REQ.getAction());
    }

    /**
     * 直播间简介
     */
    private String description;


    /**
     * 直播间拥有者
     */
    private String userId;

}
