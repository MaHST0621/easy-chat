package com.example.easyChat.server.event;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.FetchHistoryMessageReqAction;
import com.example.easyChat.common.action.FetchHistoryMessageRespAction;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.connection.ConnectionPool;
import com.example.easyChat.server.model.MessageRelation;
import com.example.easyChat.server.service.MessageService;
import com.example.easyChat.server.service.impl.MessageServiceImp;
import com.example.easyChat.server.util.SpringContextUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author m1186
 */
@Slf4j
public class FetchHistoryMessageEvent implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        MessageServiceImp messageService = SpringContextUtil.getBean(MessageServiceImp.class);
        FetchHistoryMessageReqAction reqAction = JSONObject.parseObject(action.getPayload(),FetchHistoryMessageReqAction.class);
        Long from_Id = ConnectionPool.getInstance().getUserIdByChannelId(channel.id().asLongText());
        List<String> messages = messageService.fetchHistory(reqAction.getToUserId(),from_Id);

        FetchHistoryMessageRespAction respAction = new FetchHistoryMessageRespAction();
        List<String> resMessage = new ArrayList<>(messages);
        respAction.setResult(true);
        respAction.setMessages(resMessage);
        respAction.setPayload(JSONObject.toJSONString(respAction));
        resMessage.clear();
        return respAction;
    }
}
