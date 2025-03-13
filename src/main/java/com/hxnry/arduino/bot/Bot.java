package com.hxnry.arduino.bot;

import com.hxnry.arduino.ArduinoConfig;
import com.hxnry.arduino.ArduinoPlugin;
import com.hxnry.arduino.concurrency.Task;
import com.hxnry.arduino.events.ArduinoDataListener;
import com.hxnry.arduino.events.ArduinoEvent;
import com.hxnry.arduino.events.EventDispatcher;
import com.hxnry.arduino.internal.AbstractBot;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

import javax.inject.Inject;
import java.awt.*;
import java.util.Optional;

public class Bot extends AbstractBot {

    private Client client;

    private ArduinoConfig config;

    private ArduinoPlugin plugin;

    private Task task = new Task() {

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public void execute() {

            if(!client.getCanvas().hasFocus()) {
                return;
            }

            Widget inventory = client.getWidget(WidgetInfo.INVENTORY);

            if(inventory == null) return;

            Optional<WidgetItem> item = inventory.getWidgetItems().stream().findFirst();

            if(item.isPresent()) {
                moveTo(item.get().getCanvasBounds());
            }
        }
    };

    @Inject
    public BotOverlay botOverlay;

    @Inject
    private Bot(Client client, ArduinoConfig config, ArduinoPlugin arduinoPlugin) {
        this.client = client;
        this.config = config;
        this.plugin = arduinoPlugin;
    }

    public int getHorizontalMouseTo(Point point) {
        PointerInfo mouseInfo = MouseInfo.getPointerInfo();
        Point mouse = mouseInfo.getLocation();
        return point.x - mouse.x;
    }

    public int getVerticalMouseTo(Point point) {
        PointerInfo mouseInfo = MouseInfo.getPointerInfo();
        Point mouse = mouseInfo.getLocation();
        return mouse.y - point.y;
    }

    public Point getRealMouseLocation(Rectangle monitorBounds) {
        PointerInfo mouseInfo = MouseInfo.getPointerInfo();
        Point mouse = mouseInfo.getLocation();
        int xOffset = mouse.x - monitorBounds.x;
        int yOffset = mouse.y - monitorBounds.y;
        return new Point(xOffset, yOffset);
    }

    public Point getAbsBoundsRect(Rectangle bounds, Rectangle rectangle) {
        int x = bounds.x + rectangle.x;
        int y = bounds.y + rectangle.y;
        return new Point(x, y);
    }

    public Rectangle getAbsBoundsRectangle(Rectangle bounds, Rectangle rectangle) {
        int x = bounds.x + rectangle.x;
        int y = bounds.y + rectangle.y;
        return new Rectangle(x, y, rectangle.width, rectangle.height);
    }

    ArduinoEvent sendDataEvent(String data) {
        return new ArduinoEvent() {
            @Override
            public void dispatch(ArduinoDataListener eventListener) {
                eventListener.onSend(data);
            }
        };
    }

    public boolean moveTo(Rectangle target) {


        Point canvasLocationOnScreen = client.getCanvas().getParent().getLocationOnScreen();
        Dimension monitor = config.monitorSizeConfig();
        Rectangle destination = new Rectangle(canvasLocationOnScreen.x + target.x, canvasLocationOnScreen.y + target.y, target.width, target.height);

        Rectangle targetBounds = getAbsBoundsRectangle(new Rectangle(0, 0, monitor.width, monitor.height), destination);
        Point targetSpot = getAbsBoundsRect(new Rectangle(0, 0, monitor.width, monitor.height), destination);

        Point mouseLocation = new Point(client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY());

        botOverlay.boundsLocal = target;
        botOverlay.boundsGlobal = destination;

        if(!destination.contains(mouseLocation)) {
            int xDistance = getHorizontalMouseTo(targetSpot);
            if(xDistance > 5 || xDistance < -5) {
                if(xDistance > 65 || xDistance < -65) {
                    if(xDistance < 0) {
                        botOverlay.setDebug("Moving left -65");
                        EventDispatcher.getInstance().fire(sendDataEvent("x" + -65));
                    } else {
                        botOverlay.setDebug("Moving right 65");
                        EventDispatcher.getInstance().fire(sendDataEvent("x" + 65));
                    }
                } else {
                    int xMove = 0;
                    if(xDistance < 0) {
                        xMove = Math.abs(xDistance) / 2;
                    }
                    botOverlay.setDebug("Moving X " + xMove);
                    sendDataEvent("x" + xMove);
                }
            } else {
                int yDistance = getVerticalMouseTo(targetSpot);
                if(yDistance > 65 || yDistance < -65) {
                    if(yDistance < 0) {
                        botOverlay.setDebug("Moving Y " + (65 / 3));
                        EventDispatcher.getInstance().fire(sendDataEvent("y" + (65 / 3)));
                    } else {
                        botOverlay.setDebug("Moving Y " + (-65 / 3));
                        EventDispatcher.getInstance().fire(sendDataEvent("y" + (-65 / 3)));
                    }
                } else {
                    int yMove = 0;
                    if(yDistance < 0) {
                        yMove = Math.abs(yDistance) / 2;
                    }
                    botOverlay.setDebug("Moving Y " + yMove);
                    EventDispatcher.getInstance().fire(sendDataEvent("y" + yMove));
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public int loop() {

        if(!client.getGameState().equals(GameState.LOGGED_IN)) {
            return 200;
        }

        task.run();
        return 500;
    }
}
