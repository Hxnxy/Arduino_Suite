package com.hxnry.arduino.events;

import java.util.EventListener;

public interface DeviceSelectedListener extends EventListener {

    void onDeviceSelected(String device);

}
