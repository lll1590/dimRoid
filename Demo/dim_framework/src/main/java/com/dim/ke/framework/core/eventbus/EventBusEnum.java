package com.dim.ke.framework.core.eventbus;

public enum EventBusEnum {

    StringEvent(100001, "普通字符串事件");

    private int type;
    private String msg;

    EventBusEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
