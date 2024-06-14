/*
 * Checkers: The Game
 * Created by Jayred Robles, Kelvin Wang, and Soheil Rajabali
 * V 1.0
 */

package Panels;

import java.awt.*;
import javax.swing.*;

/**
 * The MainScreen class represents the main panel of the application.
 * It extends the JPanel class and contains various components such as labels, buttons, and text fields.
 */
public class MainScreen extends JPanel {

    //Imports all assets into the program
    Assets programAssets = new Assets();

    //labels, buttons, and text fields for panel
    private final JLabel theMainTitle = new JLabel("CHECKERS");
    private final JLabel theNameTitle = new JLabel("Enter Name:");

    /**The field to enter the player's name */
    public JTextField theNameField = new JTextField();
    /**The button to host a game */
    public JButton theHostButton = new JButton("Host Game");
    /**The button to join a game */
    public JButton theJoinButton = new JButton("Join Game");
    /**The button to access the help screen */
    public JButton theHelpButton = new JButton("Help");

    /**
     * Paints the background image on the panel.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(programAssets.imgBackground, 0, 0, null);
    }

    /**
     * Constructs a new MainScreen object.
     * Initializes and formats all the components of the panel.
     */
    public MainScreen() {
        //Panel Formatting
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(null);

        //Main Title Formatting
        theMainTitle.setFont(programAssets.fntHelvetica120);
        theMainTitle.setForeground(programAssets.clrWhite);
        theMainTitle.setBounds(0, 50, 1280, 130);
        theMainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theMainTitle);

        //Name Title Formatting
        theNameTitle.setFont(programAssets.fntHelvetica50);
        theNameTitle.setForeground(programAssets.clrWhite);
        theNameTitle.setBounds(0, 195, 1280, 130);
        theNameTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theNameTitle);

        //Name Field Formatting
        theNameField.setFont(programAssets.fntHelvetica30);
        theNameField.setForeground(programAssets.clrBlack);
        theNameField.setBounds(380, 315, 520, 50);
        theNameField.setBorder(null);
        theNameField.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theNameField);

        //Host Button Formatting
        theHostButton.setFont(programAssets.fntHelvetica30);
        theHostButton.setForeground(programAssets.clrBlack);
        theHostButton.setBounds(380, 480, 520, 50);
        theHostButton.setBorder(null);
        theHostButton.setHorizontalAlignment(SwingConstants.CENTER);
        theHostButton.setBackground(programAssets.clrWhite);
        this.add(theHostButton);

        //Join Button Formatting
        theJoinButton.setFont(programAssets.fntHelvetica30);
        theJoinButton.setForeground(programAssets.clrBlack);
        theJoinButton.setBounds(380, 550, 520, 50);
        theJoinButton.setBorder(null);
        theJoinButton.setHorizontalAlignment(SwingConstants.CENTER);
        theJoinButton.setBackground(programAssets.clrWhite);
        this.add(theJoinButton);

        //Help Button Formatting
        theHelpButton.setFont(programAssets.fntHelvetica30);
        theHelpButton.setForeground(programAssets.clrBlack);
        theHelpButton.setBounds(380, 620, 520, 50);
        theHelpButton.setBorder(null);
        theHelpButton.setHorizontalAlignment(SwingConstants.CENTER);
        theHelpButton.setBackground(programAssets.clrWhite);
        this.add(theHelpButton);
    }
}
