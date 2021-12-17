package com.example.easyChat.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.event.EventPool;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.connection.ConnectionPool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connect from address:" + ctx.channel().remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Removed connection from address:" + ctx.channel().remoteAddress());
        ConnectionPool.getInstance().removeByChannelId(ctx.channel().id().asLongText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        if (! (o instanceof TextWebSocketFrame)) {
            System.out.println("received a wrong type message:" + o);
        }
        TextWebSocketFrame request = (TextWebSocketFrame) o;
        Action action;
        action = JSONObject.parseObject(request.text(),Action.class);

        IEvent<Action,Action> event = EventPool.getInstance().find(action.getAction());
        if (event == null) {
            System.out.println("this action not exist! key : " + action.getAction());
            return;
        }
        Action respAction = event.handle(action,ctx.channel());
        if ( null != respAction ) {
            System.out.println("server create resp action: " + respAction);
            ctx.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(respAction)));
        }

    }
}
