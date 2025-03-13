package com.hxnry.arduino.events;

import java.util.EventObject;

public abstract class ConnectEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private static final Object source = new Object();
    public int mask = -1;
    private long time;

    public ConnectEvent() {
        super(source);
        this.time = System.currentTimeMillis();
    }

    public abstract void dispatch(ConnectListener eventListener);

    public long getTime() {
        return time;
    }
}
