package com.hxnry.arduino.events;

import java.util.EventObject;

public abstract class ArduinoEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private static final Object source = new Object();
    public int mask = -1;
    private long time;

    public ArduinoEvent() {
        super(source);
        this.time = System.currentTimeMillis();
    }

    public abstract void dispatch(ArduinoDataListener eventListener);

    public long getTime() {
        return time;
    }
}
