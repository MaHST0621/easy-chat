package com.example.easyChat.client.Api;

import com.example.easyChat.common.action.Action;

public interface MessageService {
    Action SendMessage(Long to_userId, Long from_userId, String Message);
}
