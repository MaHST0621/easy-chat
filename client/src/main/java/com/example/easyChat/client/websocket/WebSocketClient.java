package com.example.easyChat.client.websocket;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.client.handler.WebsocketHandler;
import com.example.easyChat.common.action.Action;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;

public class WebSocketClient {
    private URI uri;

    private Bootstrap bootstrap;
    private EventLoopGroup group;

    private ChannelPromise channelPromise;
    private Channel channel;

    private WebsocketHandler myHandler;
    public WebSocketClient(final URI uri) {
        System.out.println(uri.toString());
        this.uri = uri;
        this.init();
    }

    public void connect(){
        try {
            channel = bootstrap.connect(uri.getHost(),uri.getPort()).sync().channel();
            channelPromise = myHandler.getMyPromise();
            channelPromise.sync();
            System.out.println("Connected success! and handShake complete!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.option(ChannelOption.TCP_NODELAY,true);

        group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(64*1024));
                        pipeline.addLast(myHandler = new WebsocketHandler(getHandshaker(uri),channelPromise));
                    }
                    private WebSocketClientHandshaker getHandshaker(final URI uri) {
                        return WebSocketClientHandshakerFactory.newHandshaker(uri,
                                WebSocketVersion.V13,null,false,null
                                );
                    }
                });
    }

    public void send(Action action,String payload) {
        if (action == null) {
            System.out.println("empty action");
            return;
        }

        if (payload == null || payload.isEmpty()) {
            System.out.println("empty payload");
            return;
        }
        action.setPayload(payload);
        channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(action)));
    }
}
