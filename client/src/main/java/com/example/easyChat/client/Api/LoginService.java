package com.example.easyChat.client.Api;

import com.example.easyChat.common.action.Action;

public interface LoginService {
    Action login(String mobile, String password);
}
