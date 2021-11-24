package com.example.easyChat.common.event;

import io.netty.channel.Channel;

public interface IEvent<T,R> {
    /**
     * 处理事件业务
     * @param action
     * @param channel
     * @return
     */
    R handle (final T action, final Channel channel);
}
