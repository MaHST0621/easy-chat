package com.example.easyChat.client.handler;

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
        System.out.println("from" + ctx.channel().remoteAddress() + " :" + request.text());
    }

    public ChannelPromise getMyPromise() {
        return channelPromise;
    }
}
