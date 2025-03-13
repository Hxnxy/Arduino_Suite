package com.hxnry.arduino.nav.widgets.connection;

import com.hxnry.arduino.nav.ArduinoNavPanel;
import com.hxnry.arduino.nav.widgets.device.DeviceActionsWidget;
import com.hxnry.arduino.nav.widgets.device.DeviceWidget;
import com.hxnry.arduino.nav.widgets.wrappers.Widget;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConnectionWidget extends Widget {

    private final JPanel contentPanel = new JPanel();
    public final ArduinoNavPanel arduinoPanel;
    public final DeviceActionsWidget deviceActionsPanel = new DeviceActionsWidget();
    public final DeviceWidget devicePanel = new DeviceWidget();

    public ConnectionWidget(ArduinoNavPanel arduinoPanel) {

        this.arduinoPanel = arduinoPanel;

        setOpaque(false);
        setLayout(new BorderLayout());

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(15, 5, 5, 5));
        add(contentPanel, BorderLayout.CENTER);

        deviceActionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        deviceActionsPanel.setContext(this);

        contentPanel.add(devicePanel);
        contentPanel.add(deviceActionsPanel);
    }

    @Override
    public int position() {
        return 0;
    }
}
