package com.hxnry.arduino.events;

import java.util.EventListener;

public interface ConnectListener extends EventListener {

    void connect(int serialPort, int baudRate);

    void disconnect();

}
