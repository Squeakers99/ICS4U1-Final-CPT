package Panels;

import java.awt.*;
import javax.swing.*;

public class JoinIP extends JPanel {
    //Imports all assets into the program
    Assets programAssets = new Assets();

    //Creates JLabels, JButtons, and JTextFields for the panel
    private JLabel theTitle = new JLabel("Join With IP");
    private JLabel theIPTitle = new JLabel("IP Adress:");

    public JTextField theIPField = new JTextField();
    public JButton theJoinButton = new JButton("Join Server");
    public JLabel theErrorMessage = new JLabel();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(programAssets.imgBackground, 0, 0, null);
    }

    public JoinIP() {
        //Panel Formatting
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(null);

        //Title Formatting
        theTitle.setFont(programAssets.fntHelvetica120);
        theTitle.setForeground(programAssets.clrWhite);
        theTitle.setBounds(0, 30, 1280, 150);
        theTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theTitle);

        //IP Title Formatting
        theIPTitle.setFont(programAssets.fntHelvetica50);
        theIPTitle.setForeground(programAssets.clrWhite);
        theIPTitle.setBounds(0, 275, 1280, 50);
        theIPTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theIPTitle);

        //IP Field Formatting
        theIPField.setFont(programAssets.fntHelvetica30);
        theIPField.setForeground(programAssets.clrBlack);
        theIPField.setBounds(380, 350, 520, 50);
        theIPField.setBorder(null);
        theIPField.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theIPField);

        //Join Button Formatting
        theJoinButton.setFont(programAssets.fntHelvetica30);
        theJoinButton.setForeground(programAssets.clrBlack);
        theJoinButton.setBounds(440, 500, 400, 50);
        theJoinButton.setBorder(null);
        theJoinButton.setHorizontalAlignment(SwingConstants.CENTER);
        theJoinButton.setBackground(programAssets.clrWhite);
        this.add(theJoinButton);

        //Error Message Formatting
        theErrorMessage.setFont(programAssets.fntHelvetica50);
        theErrorMessage.setForeground(programAssets.clrWhite);
        theErrorMessage.setBounds(0, 610, 1280, 50);
        theErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theErrorMessage);
    }
}
