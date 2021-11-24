package com.example.easyChat.common.action;

import lombok.Getter;

public enum ActionEnum {
    ACTION_LOGIN_REQ("login_req","登录请求"),
    ACTION_LOGIN_RESP("login_resp","登录响应"),
    ACTION_FETCH_ONLINE_USER_REQ("fetch_online_users_req","获取在线用户列表请求"),
    ACTION_FETCH_ONLINE_USER_RESP("fetch_online_users_resp_resp","获取现在用户列表响应"),
    ACTION_SEND_MESSAGE_REQ("send_message_req","发送消息请求"),
    ACTION_SEND_MESSAGE_RESP("send_message_reSP","发送消息响应"),
    ACTION_RECEIVE_MESSAGE_NOTIFY("receive_message_notify","接收消息推送"),
    ACTION_RECEIVE_MESSAGE_Notify_ACK("receive_message_notify_ack","接收消息推送确认"),
    ACTION_FETCH_HISTORY_MESSAGE_REQ("fetch_history_message_req","获取历史消息请求"),
    ACTION_FETCH_HISTORY_MESSAGE_RESP("fetch_history_message_resp","获取历史消息响应")
    ;

    @Getter
    private String action;
    @Getter
    private String desc;

    ActionEnum(final String action,final String desc) {
        this.action = action;
        this.desc = desc;
    }
}
