package com.example.easyChat.server.connection;

import io.netty.channel.Channel;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ConnectionPool {
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

    public void add(final Long userId, final Channel channel) {
        if (userId == null) {
            System.out.println("userId is Empty!");
            return;
        }
        if (channel == null) {
            System.out.println("channel is Empty!!");
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
    }

    public void removeByChannelId(final String channelId) {
        if (channelId == null || channelId.isEmpty()) {
            System.out.println("channelId is Empty!");
            return;
        }
        channels.remove(channelId);
        Long userId = userIds.get(channelId);
        if (null != userId) {
            users.remove(userId);
            userIds.remove(channelId);
        }
    }

    public void removeByUserId(final Long userId) {
        if (userId == null) {
            System.out.println("userId is Empty!");
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

    public List<Long> userIdList() {
        List<Long> ids = new ArrayList<>();
        Enumeration<Long> enumeration = users.keys();
        while (enumeration.hasMoreElements()) {
            Long id = enumeration.nextElement();
            ids.add(id);
        }
        return ids;
    }

    public Long getUserIdByChannel(String channelId) {
        Long userId = userIds.get(channelId);
        if (userId == null) {
            System.out.println("can not find userId by channel: " + channelId);
            return null;
        }
        return userId;
    }

    public List<Channel> getChannels(Long userId) {
        if ( null == userId ) {
            System.out.println("userId is empty!");
            return null;
        }
        HashSet<String> channelIds = users.get(userId);
        if ( CollectionUtils.isEmpty(channelIds) ) {
            System.out.println("can not find channelIds with userId: " + userId);
            return null;
        }
        // TODO: 找不到对应的channelId的时候，会返回空
        return channelIds.stream().map(channelId -> channels.get(channelId)).collect(Collectors.toList());
    }
}
