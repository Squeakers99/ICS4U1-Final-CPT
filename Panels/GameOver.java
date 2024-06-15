/*
 * Checkers: The Game
 * Created by Jayred Robles, Kelvin Wang, and Soheil Rajabali
 * V 1.0
 */

package Panels;

import java.awt.*;
import javax.swing.*;

/**
 * The GameOver class represents a JPanel that displays the game over screen.
 * It extends the JPanel class and contains JLabels, JButtons, and JTextFields for the panel.
 */
public class GameOver extends JPanel {

    //Imports all assets into the program
    Assets programAssets = new Assets();

    //Creates JLabels, JButtons, and JTextFields for the panel
    /** The message for the screen (who won) */
    public JLabel theMessage = new JLabel();

    @Override
    /**
     * Paints the background image of the panel.
     * @param g The Graphics object to paint with.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(programAssets.imgBackground, 0, 0, null);
    }

    /**
     * Constructs a new GameOver panel.
     * Sets the preferred size of the panel to 1280x720 and the layout to null.
     * Initializes and configures the message label.
     */
    public GameOver() {
        //Panel Formatting
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(null);

        //Message Formatting
        theMessage.setFont(programAssets.fntHelvetica120);
        theMessage.setForeground(programAssets.clrWhite);
        theMessage.setBounds(0, 50, 1280, 130);
        theMessage.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theMessage);
    }
}
