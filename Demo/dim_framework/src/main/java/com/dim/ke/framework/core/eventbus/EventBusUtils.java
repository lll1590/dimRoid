package com.dim.ke.framework.core.eventbus;


import org.greenrobot.eventbus.EventBus;

public class EventBusUtils {
    //在要接收消息的页面注册
    public static void register(Object obj){
        if(!EventBus.getDefault().isRegistered(obj)) {
            EventBus.getDefault().register(obj);
        }
    }
    //解除注册
    public static void unregister(Object obj){
        if(EventBus.getDefault().isRegistered(obj)) {
            EventBus.getDefault().unregister(obj);
        }
    }

    //发送消息
    public static void post(Object obj){
        EventBus.getDefault().post(obj);
    }

    public static void postStick(Object obj){
        EventBus.getDefault().postSticky(obj);
    }
}
