package com.hxnry.arduino.nav.widgets.device;

import com.hxnry.arduino.events.DeviceSelectedListener;
import com.hxnry.arduino.events.EventDispatcher;
import com.hxnry.arduino.nav.widgets.wrappers.Widget;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.shadowlabel.JShadowedLabel;

import javax.swing.*;
import java.awt.*;

public class DeviceWidget extends Widget {

    private final JLabel header = new JShadowedLabel();
    public final JLabel device = new JShadowedLabel();

    public DeviceWidget() {
        init();
    }

    private void init() {

        EventDispatcher.getInstance().accept((DeviceSelectedListener) device -> setDevice(device, "#65E117"));

        header.setForeground(Color.WHITE);
        header.setHorizontalAlignment(SwingConstants.CENTER);

        device.setFont(FontManager.getRunescapeSmallFont());
        device.setHorizontalAlignment(SwingConstants.CENTER);
        device.setForeground(Color.GRAY);

        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        device.setAlignmentX(Component.LEFT_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(header);
        add(Box.createVerticalStrut(2));
        add(device);
        add(Box.createVerticalStrut(10));

        setContent("<html><body style = 'text-align:center'>" + "-Device-" + "</body></html>", setSelectDevice());

    }

    public String setSelectDevice() {
        return "<html><body style = 'text-align:center;color:#FE7072;'>" + "Please select your input device." + "</body></html>";
    };

    public void setContent(String title, String description)
    {
        header.setText(title);
        device.setText(description);
        setVisible(true);
    }
//#ecec00
//#65E117
    public void setDevice(String chosen, String color) {
        if(chosen.equals("empty")) {
            setSelectDevice();
        } else {
            device.setText("<html><body style = 'text-align:center;color:"+ color + ";'>" + chosen + "</body></html>");
        }
    }
}
