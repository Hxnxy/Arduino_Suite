package com.hxnry.arduino.nav.widgets.debug;

import com.hxnry.arduino.events.ArduinoDataListener;
import com.hxnry.arduino.events.ArduinoEvent;
import com.hxnry.arduino.events.EventDispatcher;
import com.hxnry.arduino.nav.ArduinoNavPanel;
import com.hxnry.arduino.nav.widgets.containers.HorizontalWidget;
import com.hxnry.arduino.nav.widgets.wrappers.Widget;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DebugWidget extends Widget {

    private final JPanel contentPanel = new JPanel();
    private final JButton cutTreeButton = new JButton("Cut Tree");
    private final HorizontalWidget horizontalWidget;
    public final ArduinoNavPanel arduinoPanel;

    public DebugWidget(ArduinoNavPanel arduinoPanel) {

        this.arduinoPanel = arduinoPanel;

        this.cutTreeButton.addActionListener(a -> {

        });

        this.horizontalWidget = new HorizontalWidget(cutTreeButton);

        setOpaque(false);
        setLayout(new BorderLayout());

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(15, 5, 5, 5));
        add(contentPanel, BorderLayout.CENTER);

    }

}
