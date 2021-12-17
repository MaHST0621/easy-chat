package com.example.easyChat.server.websocket;

import com.alibaba.fastjson.JSONObject;
import com.example.easyChat.common.action.Action;
import com.example.easyChat.common.action.ActionEnum;
import com.example.easyChat.common.event.EventPool;
import com.example.easyChat.common.event.IEvent;
import com.example.easyChat.server.connection.ConnectionPool;
import com.example.easyChat.server.event.*;
import com.example.easyChat.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Setter;

public class WebSocketServer {
    //WebSocket链接路径
    @Setter
    private String contextPath;
    //服务端启动辅助类
    private ServerBootstrap bootstrap;
    //boss处理器组 用来接收链接请求
    private EventLoopGroup boss;
    //worker处理器组 用来处理相应事件
    private EventLoopGroup worker;

    public void start(final short port) {
        this.init();
        this.registeEvent();
        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("Websocket started.Listen on " + port + "!");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public WebSocketServer(final String Path) {
        this.contextPath = Path;
    }

    private void registeEvent() {
        //注册事件
        EventPool.getInstance().registe(ActionEnum.ACTION_LOGIN_REQ.getAction(), new LoginEvent());
        EventPool.getInstance().registe(ActionEnum.ACTION_FETCH_ONLINE_USER_REQ.getAction(), new FetchOnlineUsersEvent());
        EventPool.getInstance().registe(ActionEnum.ACTION_SEND_MESSAGE_REQ.getAction(), new SendMessageEvent());
        EventPool.getInstance().registe(ActionEnum.ACTION_RECEIVE_MESSAGE_Notify_ACK.getAction(), new RecieveMessageEvent());
        EventPool.getInstance().registe(ActionEnum.ACTION_FETCH_HISTORY_MESSAGE_REQ.getAction(), new FetchHistoryMessageEvent());
    }

    private void init() {
        bootstrap = new ServerBootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.option(ChannelOption.SO_BACKLOG,1024);
        bootstrap.option(ChannelOption.TCP_NODELAY,true);

        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup(5);

        bootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new ChunkedWriteHandler());
                        pipeline.addLast(new HttpObjectAggregator(64*1024));
                        pipeline.addLast(new WebSocketServerProtocolHandler(contextPath));
                        //添加自定义Handler
                        pipeline.addLast(new WebSocketHandler());
                    }
                });
    }

}
