package com.example.easyChat.common.event;

import java.util.concurrent.ConcurrentHashMap;

public class EventPool {
    private static final EventPool INSTANCE = new EventPool();
    private EventPool() {
        events = new ConcurrentHashMap<>();
    }
    public static EventPool getInstance() {return INSTANCE;}

    private ConcurrentHashMap<String,IEvent> events;

    /**
     * 注册事件
     * @param key
     * @param handler
     */
    public void registe(final String key,final IEvent handler) {
        if (key == null || key.isEmpty()) {
            System.out.println("key is empty!");
            return;
        }
        if (handler == null) {
            System.out.println("handler is empty");
            return;
        }
        events.put(key,handler);
    }

    public IEvent find(final String key) {
        if (key == null || key.isEmpty()) {
            System.out.println("key is empty!");
            return null;
        }
        return events.get(key);
    }
}
