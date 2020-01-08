package com.dim.ke.framework.core.eventbus;

import java.io.Serializable;

public class BaseEvent<T> implements Serializable {
    private EventBusEnum eventType;
    private T data;
}
