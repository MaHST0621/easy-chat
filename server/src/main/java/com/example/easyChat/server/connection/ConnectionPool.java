package com.example.easyChat.server.connection;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ConnectionPool {
    private Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private static final ConnectionPool INSTANCE = new ConnectionPool();
    private ConnectionPool() {
        this.users = new ConcurrentHashMap<>();
        this.channels = new ConcurrentHashMap<>();
        this.userIds = new ConcurrentHashMap<>();
    }
    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    //一个用户可能有多条链接
    //case1:根据用户id获取链接对象的Set集合
    //userId => {channelId,channelId,channelId...}
    private ConcurrentHashMap<Long, HashSet<String>> users;

    //case2:根据channel的id去找到连接对象
    //channelId => {channel}
    private ConcurrentHashMap<String, Channel> channels;

    //channelId => userId
    private ConcurrentHashMap<String,Long> userIds;

    //liveroomId => channel
    private ConcurrentHashMap<String,Channel> liveroom_channels;

    //userId => liveroomId
    private ConcurrentHashMap<String,String> userId_liveroomId;

    //userIds => {userId,userId} 在线用户ID
    private List<Long> onlineUsers = new ArrayList<>();

    //消息SEQ
    public static final AttributeKey<AtomicLong> TID_GENERATOR = AttributeKey.valueOf("tid_generator");

    //未收ACK的包
    public static final AttributeKey<ConcurrentHashMap> NON_ACKED_MAP = AttributeKey.valueOf("non_acked_map");

    public void add(final Long userId, final Channel channel) {
        if (userId == null) {
            logger.info("连接池用户ID为空");
            return;
        }
        if (channel == null) {
            logger.info("连接池用户Channel为空");
            return;
        }
        HashSet<String> channelIds = users.get(userId);
        if (CollectionUtils.isEmpty(channelIds)) {
            channelIds = new HashSet<>();
        }
        channelIds.add(channel.id().asLongText());
        users.put(userId,channelIds);
        userIds.put(channel.id().asLongText(),userId);
        channels.put(channel.id().asLongText(),channel);
        onlineUsers.add(userId);
        channel.attr(TID_GENERATOR).set(new AtomicLong(0));
        channel.attr(NON_ACKED_MAP).set(new ConcurrentHashMap<Long,String>());
    }

    public void removeByChannelId(final String channelId) {
        if (channelId == null || channelId.isEmpty()) {
            logger.info("删除连接池ChannelID为空");
            return;
        }
        channels.remove(channelId);
        Long userId = userIds.get(channelId);
        if (null != userId) {
            users.remove(userId);
            userIds.remove(channelId);
            onlineUsers.remove(userId);
        }
    }

    public void removeByUserId(final Long userId) {
        if (userId == null) {
            logger.info("删除连接池用户ID为空");
            return;
        }
        HashSet<String> channelIds = users.get(userId);
        if ( !CollectionUtils.isEmpty(channelIds)) {
            users.remove(userId);
            channelIds.stream() .forEach(channelId -> {
                userIds.remove(channelId);
                channels.remove(channelId);
            });
        }
    }


    public Long getUserIdByChannel(String channelId) {
        Long userId = userIds.get(channelId);
        if (userId == null) {
            logger.info("该Channel_ID未绑定用户");
            return null;
        }
        return userId;
    }

    public List<Channel> getChannels(Long userId) {
        if ( null == userId ) {
            logger.info("查询Channel的用户ID为空");
            return null;
        }
        HashSet<String> channelIds = users.get(userId);
        if ( CollectionUtils.isEmpty(channelIds) ) {
            logger.info("{}用户未绑定Channel",userId);
            return null;
        }
        // TODO: 找不到对应的channelId的时候，会返回空
        return channelIds.stream().map(channelId -> channels.get(channelId)).collect(Collectors.toList());
    }


    public void addLiveroom(final String userId,final String liveroomId,final Channel channel) {
        if (userId == null || userId == "") {
            logger.info("连接池加入用户ID为空");
            return;
        }
        if (liveroomId == null || liveroomId == "") {
            logger.info("连接池加入直播间ID为空");
            return;
        }
        if (channel == null) {
            logger.info("连接池加入Channel为空");
            return;
        }
        liveroom_channels.put(liveroomId,channel);
        userId_liveroomId.put(userId,liveroomId);
    }

    public Channel getChannelByliveroomId(String liverommId) {
        Channel channel = null;
        if (liverommId == null || liverommId == "") {
            logger.info("连接池查找的Channel_ID为空");
            return channel;
        }

        channel = liveroom_channels.get(liverommId);
        return channel;
    }

    public List<Long> getOnlineUsers() {
        return new ArrayList<>(onlineUsers);
    }

    public ConcurrentHashMap getNonAckMap(final Channel channel) {
        return channel.attr(NON_ACKED_MAP).get();
    }

    public void addNonAcks(final Channel channel,final Long tId,final String msg) {
        channel.attr(NON_ACKED_MAP).get().put(tId,msg);
    }

    public boolean checkContainsId(final Channel channel,final Long tId) {
        return channel.attr(NON_ACKED_MAP).get().containsKey(tId);
    }

    public AtomicLong getTidGeneritor(final Channel channel) {
        return channel.attr(TID_GENERATOR).get();
    }

    public void checkAndDelAck(final Channel channel,Long tId) {
        if(!checkContainsId(channel,tId)) {
            logger.info("{}该ACK_SEQ不存在");
            return;
        }
        channel.attr(NON_ACKED_MAP).get().remove(tId);
    }
}
