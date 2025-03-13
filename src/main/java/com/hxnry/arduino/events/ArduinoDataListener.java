package com.hxnry.arduino.events;

import java.util.EventListener;

public interface ArduinoDataListener extends EventListener {

    void onSend(String device);

}
