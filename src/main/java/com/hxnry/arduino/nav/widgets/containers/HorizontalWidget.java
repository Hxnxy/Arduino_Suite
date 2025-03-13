package com.hxnry.arduino.nav.widgets.containers;

import javax.swing.*;
import java.awt.*;

public class HorizontalWidget extends JPanel {

    public HorizontalWidget(Component... components) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        for(Component component : components) {
            add(component);
            add(Box.createHorizontalStrut(3));
        }
    }

}
