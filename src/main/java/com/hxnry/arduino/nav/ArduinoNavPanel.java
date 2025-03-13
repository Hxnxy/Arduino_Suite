package com.hxnry.arduino.nav;

import com.hxnry.arduino.ArduinoConfig;
import com.hxnry.arduino.ArduinoPlugin;
import com.hxnry.arduino.events.ArduinoDataListener;
import com.hxnry.arduino.events.ConnectListener;
import com.hxnry.arduino.events.EventDispatcher;
import com.hxnry.arduino.hardware.Arduino;
import com.hxnry.arduino.nav.widgets.connection.ConnectionWidget;
import com.hxnry.arduino.nav.widgets.debug.DebugWidget;
import com.hxnry.arduino.nav.widgets.helper.InfoWidget;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
public class ArduinoNavPanel extends PluginPanel {

    final ArduinoPlugin plugin;
    public final ArduinoConfig config;
    protected Arduino arduino;
    final ConnectionWidget connectionPanel;
    final DebugWidget debugWidget;
    final InfoWidget infoPanel;
    final JLabel overallIcon = new JLabel();

    final ArduinoDataListener arduinoDataListener = new ArduinoDataListener() {
        @Override
        public void onSend(String data) {
            if(arduino != null) {
                System.out.println("Sending " + data + " to arduino.");
                arduino.serialWrite(data + "\n");
            } else {
                System.out.println("arduino is null");
            }
        }
    };

    @Inject
    public ArduinoNavPanel(ArduinoPlugin plugin, ArduinoConfig config) {

        //TODO MAKE SOUND SWAP PLUGIN FOR ING AME RUNESCAPE WITH TRAP REMIXES

        EventDispatcher.getInstance().setActive(true);
        EventDispatcher.getInstance().accept(new ConnectListener() {
            @Override
            public void connect(int serialPort, int baudRate) {
                if(arduino == null) {
                    arduino = new Arduino("COM" + serialPort, 9600);
                    System.out.println("attempting to connect to arduino on serial port " + serialPort + " with a baud rate of " + baudRate);
                    if(arduino.openConnection()) {
                        connectionPanel.devicePanel.setDevice(connectionPanel.deviceActionsPanel.device, "#65E117");
                        connectionPanel.deviceActionsPanel.scanButton.setEnabled(false);
                        connectionPanel.deviceActionsPanel.comboBox.setEnabled(false);
                        connectionPanel.deviceActionsPanel.connectButton.setText("Disconnect");
                        System.out.println("Arduino has been connected successfully.");
                        EventDispatcher.getInstance().accept(arduinoDataListener);
                    } else {
                        connectionPanel.deviceActionsPanel.scanButton.setEnabled(true);
                        connectionPanel.deviceActionsPanel.comboBox.setEnabled(true);
                        connectionPanel.deviceActionsPanel.connectButton.setText("Connect");
                        connectionPanel.devicePanel.setDevice("Error reaching arduino...", "#FE7072");
                    }
                } else {
                    System.out.println("attempting to reconnect to arduino on serial port " + serialPort + " with a baud rate of " + baudRate);
                    if(arduino.openConnection()) {
                        connectionPanel.devicePanel.setDevice(connectionPanel.deviceActionsPanel.device, "#65E117");
                        connectionPanel.deviceActionsPanel.scanButton.setEnabled(false);
                        connectionPanel.deviceActionsPanel.comboBox.setEnabled(false);
                        connectionPanel.deviceActionsPanel.connectButton.setText("Disconnect");
                        System.out.println("Arduino has been re-connected successfully.");
                        EventDispatcher.getInstance().accept(arduinoDataListener);
                    } else {
                        connectionPanel.deviceActionsPanel.scanButton.setEnabled(true);
                        connectionPanel.deviceActionsPanel.comboBox.setEnabled(true);
                        connectionPanel.deviceActionsPanel.connectButton.setText("Connect");
                        connectionPanel.devicePanel.setDevice("Error reaching arduino...", "#FE7072");
                    }
                }
            }

            @Override
            public void disconnect() {
                System.out.println("shutting down arduino...");
                connectionPanel.deviceActionsPanel.connectButton.setText("Connect");
                connectionPanel.deviceActionsPanel.connectButton.setEnabled(false);
                connectionPanel.deviceActionsPanel.scanButton.setEnabled(true);
                connectionPanel.deviceActionsPanel.comboBox.setEnabled(true);
                connectionPanel.devicePanel.device.setText(connectionPanel.devicePanel.setSelectDevice());
                EventDispatcher.getInstance().remove(arduinoDataListener);
                if(arduino != null)
                    arduino.closeConnection();
                arduino = null;
            }
        });


        this.plugin = plugin;
        this.config = config;
        this.connectionPanel = new ConnectionWidget(this);
        this.debugWidget = new DebugWidget(this);
        this.infoPanel = new InfoWidget();
        infoPanel.setContent("Arduino Suite", "Warning: This program is still in development.");
        setBorder(new EmptyBorder(6, 6, 6, 6));
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        add(connectionPanel);
        add(infoPanel);
        add(debugWidget);
    }

    void loadHeaderIcon(BufferedImage img) {
        overallIcon.setIcon(new ImageIcon(img));
    }
}
