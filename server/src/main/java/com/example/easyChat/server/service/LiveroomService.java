package com.example.easyChat.server.service;

import com.example.easyChat.server.mapper.LiveroomMapper;
import com.example.easyChat.server.model.Liveroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LiveroomService {

    @Autowired
    LiveroomMapper liveroomMapper;

    public Liveroom find(String liverommId) {
        Liveroom resLiveroom = null;
        if (liverommId == null || liverommId == "") {
            System.out.println("查询的直播间ID不能为空!");
            return resLiveroom;
        }
        resLiveroom = liveroomMapper.selectByPrimaryKey(liverommId);
        return resLiveroom;
    }
}
