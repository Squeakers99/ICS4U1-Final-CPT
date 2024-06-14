package Panels;

import java.awt.*;
import javax.swing.*;

public class GameOver extends JPanel {

    //Imports all assets into the program
    Assets prograAssets = new Assets();

    //Creates JLabels, JButtons, and JTextFields for the panel
    public JLabel theMessage = new JLabel();

    public GameOver() {
        //Panel Formatting
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(null);

        //Message Formatting
        theMessage.setFont(prograAssets.fntHelvetica120);
        theMessage.setForeground(prograAssets.clrWhite);
        theMessage.setBounds(0, 50, 1280, 130);
        theMessage.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theMessage);
    }
}
