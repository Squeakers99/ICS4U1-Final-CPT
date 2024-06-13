package Panels;

import java.awt.*;
import javax.swing.*;

/**
 * The ClientWaiting class represents a JPanel that displays a waiting screen for the client,
 * indicating that it is waiting for the host to start the game.
 */
public class ClientWaiting extends JPanel {
    //Imports all assets into the program
    Assets programAssets = new Assets();

    //labels, buttons, and text fields for panel
    private final JLabel theTitle1 = new JLabel("Waiting for Host");
    private final JLabel theTitle2 = new JLabel("to Start Game");

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(programAssets.imgBackground, 0, 0, null);
    }

    /**
     * Constructs a new ClientWaiting panel.
     * Sets the preferred size, layout, and adds the necessary components to the panel.
     */
    public ClientWaiting() {
        // Panel Formatting
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(null);

        // Title Formatting
        theTitle1.setFont(programAssets.fntHelvetica120);
        theTitle1.setForeground(programAssets.clrWhite);
        theTitle1.setBounds(0, 150, 1280, 150);
        theTitle1.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theTitle1);

        // Title Formatting
        theTitle2.setFont(programAssets.fntHelvetica120);
        theTitle2.setForeground(programAssets.clrWhite);
        theTitle2.setBounds(0, 300, 1280, 150);
        theTitle2.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theTitle2);
    }
}
