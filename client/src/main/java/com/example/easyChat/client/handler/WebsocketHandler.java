package com.example.easyChat.client.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.event.EventPool;
import com.example.easyChat.common.event.IEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;

public class WebsocketHandler extends SimpleChannelInboundHandler<Object> {
    private WebSocketClientHandshaker handshaker;
    private ChannelPromise channelPromise;

    public WebsocketHandler (final WebSocketClientHandshaker handshaker, ChannelPromise channelPromise) {
        this.handshaker = handshaker;
        this.channelPromise = channelPromise;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelPromise  = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.handshaker.handshake(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        System.out.println("receive data:" + o + "from address:" + ctx.channel().remoteAddress());
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ctx.channel(),(FullHttpResponse) o);
                channelPromise.setSuccess();
                System.out.println("handshake success!");
            } catch (Exception e) {
                channelPromise.setFailure(e);
                e.printStackTrace();
            }
            return;
        }
        if (!(o instanceof TextWebSocketFrame)) {
            System.out.println("no received text data:" + o);
        }
        TextWebSocketFrame request = (TextWebSocketFrame) o;
        Action action;
        action = JSONObject.parseObject(request.text(),Action.class);
        IEvent event = EventPool.getInstance().find(action.getAction());
        if (event == null) {
            System.out.println("this action not exist! key : " + action.getAction());
            return;
        }
        Action respAction = (Action) event.handle(action,ctx.channel());
        if ( null != respAction ) {
            System.out.println("resp action: " + action);
            ctx.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(respAction)));
        }
    }

    public ChannelPromise getMyPromise() {
        return channelPromise;
    }
}
