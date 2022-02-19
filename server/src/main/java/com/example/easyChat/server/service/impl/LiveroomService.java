package com.example.easyChat.server.service.impl;

import com.example.easyChat.server.model.Liveroom;
import org.springframework.stereotype.Service;

@Service
public class LiveroomService {



    public Liveroom find(String liverommId) {
        Liveroom resLiveroom = null;
        if (liverommId == null || liverommId == "") {
            System.out.println("查询的直播间ID不能为空!");
            return resLiveroom;
        }

        return resLiveroom;
    }

    public void create(Liveroom liveroom) {
    }
}
