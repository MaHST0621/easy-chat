package com.example.easyChat.client.handler;

import com.alibaba.fastjson.JSON;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class WebsocketHandler extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(WebsocketHandler.class);
    private WebSocketClientHandshaker handshaker;
    private ChannelPromise channelPromise;
    private static String myToken;

    public static String getMyToken() {
        if(StringUtils.isEmpty(myToken)) {
            log.info("token还未初始化！");
            return null;
        }
        return myToken;
    }

    public static void setMyToken(String token) {
        myToken = token;
    }

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
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ctx.channel(),(FullHttpResponse) o);
                channelPromise.setSuccess();
                logger.info("握手成功");
            } catch (Exception e) {
                channelPromise.setFailure(e);
                e.printStackTrace();
            }
            return;
        }
        if (!(o instanceof TextWebSocketFrame)) {
            logger.info("接收到未知类型消息{}",o);
        }
        TextWebSocketFrame request = (TextWebSocketFrame) o;
        log.info("收到消息{}",o);
        Action action;
        action = JSONObject.parseObject(request.text(),Action.class);
        IEvent event = EventPool.getInstance().find(action.getAction());
        if (event == null) {
            logger.info("{}未找到对应的事件",action.getAction());
            return;
        }
        Action respAction = (Action) event.handle(action,ctx.channel());
        if ( null != respAction ) {
            logger.info("响应信息{}", JSON.toJSONString(respAction));
            ctx.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(respAction)));
        }
    }

    public ChannelPromise getMyPromise() {
        return channelPromise;
    }
}
