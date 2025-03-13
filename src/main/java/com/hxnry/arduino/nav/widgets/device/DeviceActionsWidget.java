package com.hxnry.arduino.nav.widgets.device;

import com.hxnry.arduino.events.ConnectEvent;
import com.hxnry.arduino.events.ConnectListener;
import com.hxnry.arduino.events.EventDispatcher;
import com.hxnry.arduino.hardware.PrintDevices;
import com.hxnry.arduino.nav.widgets.connection.ConnectionWidget;
import com.hxnry.arduino.nav.widgets.containers.HorizontalWidget;
import com.hxnry.arduino.nav.widgets.wrappers.Widget;
import com.sun.jna.platform.win32.SetupApi;
import lombok.Setter;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeviceActionsWidget extends Widget {

    @Setter
    public ConnectionWidget context;

    public String[] devices = new String[0];

    public JLabel deviceNames = new JLabel();

    public JComboBox<String> comboBox = new JComboBox<>();

    public final JButton scanButton = new JButton("Scan");
    public final JButton connectButton = new JButton("Connect");

    private final HorizontalWidget actions = new HorizontalWidget(scanButton, Box.createHorizontalGlue(), connectButton);

    @Setter
    public String device = "";

    public DeviceActionsWidget() {
        init();
    }

    private void init() {
        this.deviceNames.setFont(FontManager.getRunescapeSmallFont());
        this.comboBox.setFocusable(false);
        this.connectButton.setEnabled(false);
        this.connectButton.setFocusPainted(false);
        this.scanButton.setFocusPainted(false);
        this.comboBox.setEnabled(false);
        this.comboBox.addActionListener(e -> {
            String chosen = (String) comboBox.getSelectedItem();
            if(chosen == null) return;
            device = chosen;
            context.devicePanel.setDevice(chosen, "#ecec00");
            connectButton.setEnabled(true);
        });
        this.connectButton.addActionListener(a -> EventDispatcher.getInstance().fire(new ConnectEvent() {
            @Override
            public void dispatch(ConnectListener eventListener) {
                if(connectButton.getText().equals("Connect")) {
                    String chosen = (String) comboBox.getSelectedItem();
                    if(chosen == null) return;
                    int start = chosen.indexOf("(COM") + 4;
                    int end = chosen.lastIndexOf(")");
                    String parse = chosen.substring(start, end);
                    int port = Integer.parseInt(parse);
                    eventListener.connect(port, context.arduinoPanel.config.baudRate());
                } else {
                    eventListener.disconnect();
                }
            }
        }));
        this.scanButton.addActionListener(a -> {
            PrintDevices pd = PrintDevices.getInstance();
            java.util.List<SetupApi.SP_DEVINFO_DATA.ByReference> deviceDevInfoDataReferences = pd.getAllDevInfoDataReferences();
            List<PrintDevices.DeviceInformation> infoObjects = pd.getAllDevInfoForDataFound(deviceDevInfoDataReferences);
            String[] devices = infoObjects.stream()
                    .filter(d -> d.getFriendlyName().contains("(COM"))
                    .filter(device -> device.getFriendlyName().startsWith("Arduino"))
                    .map(PrintDevices.DeviceInformation::getFriendlyName)
                    .toArray(String[]::new);
            if(devices.length > 0) {
                comboBox.removeAllItems();
                for (String device : devices) {
                    comboBox.addItem(device);
                }
                comboBox.setEnabled(true);
            } else {
                comboBox.removeAllItems();
                comboBox.addItem("No Devices Detected.");
                connectButton.setEnabled(false);
            }
        });
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        scanButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        connectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(actions);
        add(Box.createVerticalStrut(4));
        add(comboBox);
    }

    @Override
    public int position() {
        return 0;
    }
}
