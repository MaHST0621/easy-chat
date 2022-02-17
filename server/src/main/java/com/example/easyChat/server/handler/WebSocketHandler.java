package com.example.easyChat.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.event.EventPool;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.connection.ConnectionPool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("新加入连接，IP为{}",ctx.channel().remoteAddress());
        ctx.writeAndFlush(new String("hello a friend"));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("{}IP已经断开连接",ctx.channel().remoteAddress());
        ConnectionPool.getInstance().removeByChannelId(ctx.channel().id().asLongText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        if (! (o instanceof TextWebSocketFrame)) {
            logger.info("接收到错误类型{}",o);
        }
        TextWebSocketFrame request = (TextWebSocketFrame) o;
        Action action;
        action = JSONObject.parseObject(request.text(),Action.class);
        IEvent<Action,Action> event = EventPool.getInstance().find(action.getAction());
        if (event == null) {
            logger.info("收到未定事件{}",action.getAction());
            return;
        }
        Action respAction = event.handle(action,ctx.channel());
        if ( null != respAction ) {
            logger.info("服务器响应{}", JSON.toJSONString(respAction));
            ctx.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(respAction)));
        }

    }
}
