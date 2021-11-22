package com.example.easyChat.client.websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
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
    public WebSocketClient(final URI uri) {
        System.out.println(uri.toString());
        this.uri = uri;
        this.init();
    }

    public void connect(){
        try {
            channel = bootstrap.connect(uri.getHost(),uri.getPort()).sync().channel();
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
                        pipeline.addLast(new WebsocketHandler(getHandshaker(uri)));
                    }
                    private WebSocketClientHandshaker getHandshaker(final URI uri) {
                        return WebSocketClientHandshakerFactory.newHandshaker(uri,
                                WebSocketVersion.V13,null,false,null
                                );
                    }
                });
    }

    private class WebsocketHandler extends SimpleChannelInboundHandler<Object> {
        private WebSocketClientHandshaker handshaker;

        public WebsocketHandler (final WebSocketClientHandshaker handshaker) {
            this.handshaker = handshaker;
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
    }
}
