package com.hxnry.arduino.nav.widgets.helper;

import com.hxnry.arduino.nav.widgets.wrappers.Widget;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.shadowlabel.JShadowedLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InfoWidget extends Widget {

    private final JLabel noResultsTitle = new JShadowedLabel();
    private final JLabel noResultsDescription = new JShadowedLabel();

    public InfoWidget() {
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 0, 10));
        setLayout(new BorderLayout());

        noResultsTitle.setForeground(Color.WHITE);
        noResultsTitle.setHorizontalAlignment(SwingConstants.CENTER);

        noResultsDescription.setFont(FontManager.getRunescapeSmallFont());
        noResultsDescription.setForeground(Color.GRAY);
        noResultsDescription.setHorizontalAlignment(SwingConstants.CENTER);

        add(noResultsTitle, BorderLayout.NORTH);
        add(noResultsDescription, BorderLayout.CENTER);

        setVisible(false);
    }

    /**
     * Changes the content of the panel to the given parameters.
     * The description has to be wrapped in html so that its text can be wrapped.
     */
    public void setContent(String title, String description)
    {
        noResultsTitle.setText("<html><body style = 'text-align:center'><b>" + title + "</b></body></html>");
        noResultsDescription.setText("<html><body style = 'text-align:center'>" + description + "</body></html>");
        setVisible(true);
    }

}
