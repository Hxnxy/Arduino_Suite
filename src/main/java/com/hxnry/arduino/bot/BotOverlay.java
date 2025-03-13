package com.hxnry.arduino.bot;

import com.hxnry.arduino.ArduinoConfig;
import com.hxnry.arduino.ArduinoPlugin;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class BotOverlay extends Overlay {
    private final Client client;
    private final ArduinoConfig config;
    private final ArduinoPlugin plugin;

    private final PanelComponent panelComponent = new PanelComponent();

    Duration deltaTime = Duration.ZERO;
    Instant beginTime = Instant.now();

    String defaultDebug = "-/-";
    String defaultDebugInverse = "/-/";

    @Setter
    public String debug = defaultDebug;

    public Rectangle boundsLocal = new Rectangle();
    public Rectangle boundsGlobal = new Rectangle();
    @Inject
    BotOverlay(Client client, ArduinoConfig config, ArduinoPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        deltaTime = Duration.between(beginTime, Instant.now());

        if(deltaTime.getSeconds() > 1) {
            if(debug.equals(defaultDebug))
                debug = defaultDebugInverse;
            else
                debug = defaultDebug;
            beginTime = Instant.now();
        }

        if(boundsLocal != null) {
            graphics.setFont(FontManager.getRunescapeSmallFont());
            OverlayUtil.renderTextLocation(graphics, new net.runelite.api.Point(boundsLocal.x, boundsLocal.y), "Target", Color.RED);
            OverlayUtil.renderHoverableArea(graphics, boundsLocal, client.getMouseCanvasPosition(), new Color(255, 255, 255, 100), Color.WHITE, Color.PINK);
        }

        panelComponent.getChildren().clear();

        String title = "Arduino Suite";

        panelComponent.setPreferredSize(
                new Dimension(graphics.getFontMetrics().stringWidth(title) + 180, 0));

        panelComponent.getChildren().add(TitleComponent.builder()
                .text(title)
                .color(Color.GREEN)
                .build());

        int realX = MouseInfo.getPointerInfo().getLocation().x;
        int realY = MouseInfo.getPointerInfo().getLocation().y;

        int x = client.getMouseCanvasPosition().getX();
        int y = client.getMouseCanvasPosition().getY();

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Global Mouse: " + realX + ", " + realY)
                .leftFont(FontManager.getRunescapeSmallFont())
                .right("Local Mouse: " + x + ", " + y)
                .rightFont(FontManager.getRunescapeSmallFont())
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Target (Local)")
                .leftFont(FontManager.getRunescapeSmallFont())
                .right(boundsLocal != null ? boundsLocal.x + " , " + boundsLocal.y : "null")
                .rightColor(Color.GREEN)
                .rightFont(FontManager.getRunescapeSmallFont())
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Target (Global)")
                .leftFont(FontManager.getRunescapeSmallFont())
                .right(boundsLocal != null ? boundsGlobal.x + " , " + boundsGlobal.y : "null")
                .rightColor(Color.GREEN)
                .rightFont(FontManager.getRunescapeSmallFont())
                .build());

        client.getCanvas().getParent().getLocationOnScreen();

        Point canvasLocation = client.getCanvas().getParent().getLocationOnScreen();

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Canvas Location")
                .leftFont(FontManager.getRunescapeSmallFont())
                .leftFont(FontManager.getRunescapeSmallFont())
                .right(canvasLocation.x + " , " + canvasLocation.y)
                .rightColor(Color.GREEN)
                .rightFont(FontManager.getRunescapeSmallFont())
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Can Click")
                .leftFont(FontManager.getRunescapeSmallFont())
                .right("" + boundsLocal.contains(new Point(x, y)))
                .rightColor(Color.GREEN)
                .rightFont(FontManager.getRunescapeSmallFont())
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Debug")
                .leftFont(FontManager.getRunescapeSmallFont())
                .right(debug)
                .rightColor(Color.GREEN)
                .rightFont(FontManager.getRunescapeSmallFont())
                .build());

        return panelComponent.render(graphics);
    }
}
