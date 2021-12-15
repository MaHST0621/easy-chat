package com.example.easyChat.client.Api;

import com.example.easyChat.common.action.Action;

public interface FetchService {
    Action fetchOnlineUsers(int page, int count);
    Action fetchHistoryMessages(Long userId);
}
