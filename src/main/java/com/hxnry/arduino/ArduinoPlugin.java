package com.hxnry.arduino;

import com.google.inject.Provides;
import com.hxnry.arduino.bot.Bot;
import com.hxnry.arduino.events.ConnectEvent;
import com.hxnry.arduino.events.ConnectListener;
import com.hxnry.arduino.events.EventDispatcher;
import com.hxnry.arduino.hardware.AlertBox;
import com.hxnry.arduino.nav.ArduinoNavPanel;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

@Slf4j
@PluginDescriptor(
        name = "Arduino Suite"
)
public class ArduinoPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private ArduinoConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ArduinoOverlay overlay;

    @Inject
    private SpriteManager spriteManager;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private Bot bot;

    private ArduinoNavPanel panel;

    @Inject
    private RuneLite runeLite;

    private NavigationButton navButton;

    @Override
    protected void startUp() throws Exception
    {

        panel = new ArduinoNavPanel(this, config);

        bot.invoke(bot);

        overlayManager.add(bot.botOverlay);

        log.info("arduino suite has been switched on.");

        //TODO implement an icon
        final BufferedImage icon = null;
        if(icon == null) {
            return;
        }

        navButton = NavigationButton.builder()
                .tooltip("Arduino Suite")
                .icon(icon)
                .priority(5)
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navButton);

    }

    @Override
    protected void shutDown() throws Exception
    {

        bot.revoke(bot);

        overlayManager.remove(bot.botOverlay);

        log.info("arduino suite has been switched off.");
        EventDispatcher.getInstance().fire(new ConnectEvent() {
            @Override
            public void dispatch(ConnectListener eventListener) {
                eventListener.disconnect();
            }
        });

        clientToolbar.removeNavigation(navButton);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {

        if (event.getGroup().equals("arduino_suite"))
        {
            //SwingUtilities.invokeLater(panel::updateIgnoredRecords);
            switch (event.getKey()) {
                case "monitorSizeConfig":
                    AlertBox alertBox = new AlertBox(new Dimension(20, 20),
                            "Test", "Is this working?");
                    alertBox.display();
                    break;
                case "isFishingStarted":
                    alertBox = new AlertBox(new Dimension(20, 20),
                            "Test", "Is this working?");
                    alertBox.display();
                    break;
            }
        }
    }

    @Provides
    ArduinoConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ArduinoConfig.class);
    }
}
