package com.example.easyChat.server.event;

import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.Channel;

public class CreateLiveroomEvent implements IEvent<Action,Action> {
    @Override
    public Action handle(Action action, Channel channel) {
        return null;
    }
}
