package com.example.easyChat.client.Api.Imp;

import com.example.easyChat.client.Api.LoginService;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.LoginReqAction;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImp implements LoginService {
    @Override
    public Action login(String mobile, String password) {
        LoginReqAction action = new LoginReqAction();
        action.setMobile(mobile);
        //TODO: MD5加密处理密码（明文传输密码有危险）
        action.setPassword(password);

        return action;
//        this.client.send(action, JSONObject.toJSONString(action));
    }
}
