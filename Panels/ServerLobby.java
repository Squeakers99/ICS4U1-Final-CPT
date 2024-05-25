package Panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class ServerLobby extends JPanel {

    //Imports all assets into the program
    Assets programAssets = new Assets();

    //Creates JLabels, JButtons, and JTextFields for the panel
    public JLabel theServerTitle = new JLabel("Server Lobby");
    public JTextField theChatField = new JTextField("Message Here");
    public JLabel theIPAdress = new JLabel();
    public JTextArea theChatArea = new JTextArea();
    public JButton theRedButton = new JButton("Join Red");
    public JButton theBlackButton = new JButton("Join Black");
    public JButton theStartButton = new JButton("Start Game");
    public JButton theSpectatorButton = new JButton("Join Spectator");

    private final JLabel theChatTitle = new JLabel("Lobby Chat");
    private final JLabel theIPTitle = new JLabel("Server IP Address:");
    private final JScrollPane theChatScroll = new JScrollPane(theChatArea);

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(programAssets.imgBackground, 0, 0, null);
        g.setColor(programAssets.clrGray);
        g.fillRect(960, 0, 320, 720);
    }

    public ServerLobby() {
        //Panel Formatting
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(null);

        //Server Title Formatting
        theServerTitle.setFont(programAssets.fntHelvetica100);
        theServerTitle.setForeground(programAssets.clrWhite);
        theServerTitle.setBounds(0, 30, 960, 150);
        theServerTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theServerTitle);

        //Chat Title Formatting
        theChatTitle.setFont(programAssets.fntHelvetica30);
        theChatTitle.setForeground(programAssets.clrBlack);
        theChatTitle.setBounds(960, 20, 320, 40);
        theChatTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theChatTitle);

        //Chat Area Formatting
        theChatArea.setFont(programAssets.fntHelvetica12);
        theChatArea.setEditable(false);
        theChatArea.setLineWrap(true);
        theChatArea.setWrapStyleWord(true);
        theChatArea.setBorder(null);
        theChatArea.setForeground(programAssets.clrBlack);
        theChatScroll.setBounds(980, 80, 280, 350);
        theChatScroll.setBorder(null);
        this.add(theChatScroll);

        //Chat Field Formatting
        theChatField.setFont(programAssets.fntHelvetica12);
        theChatField.setForeground(programAssets.clrBlack);
        theChatField.setBorder(new LineBorder(programAssets.clrBlack, 2));
        theChatField.setBounds(980, 440, 280, 40);
        this.add(theChatField);

        //IP Title Formatting
        theIPTitle.setFont(programAssets.fntHelvetica30);
        theIPTitle.setForeground(programAssets.clrBlack);
        theIPTitle.setBounds(960, 490, 320, 40);
        theIPTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theIPTitle);

        //IP Address Formatting
        theIPAdress.setFont(programAssets.fntHelvetica30);
        theIPAdress.setForeground(programAssets.clrBlack);
        theIPAdress.setBounds(960, 530, 320, 40);
        theIPAdress.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theIPAdress);

        //Red Button Formatting
        theRedButton.setFont(programAssets.fntHelvetica30);
        theRedButton.setForeground(programAssets.clrBlack);
        theRedButton.setBounds(140, 285, 240, 240);
        theRedButton.setBackground(programAssets.clrCrimson);
        theRedButton.setBorder(null);
        this.add(theRedButton);

        //Black Button Formatting
        theBlackButton.setFont(programAssets.fntHelvetica30);
        theBlackButton.setForeground(programAssets.clrCrimson);
        theBlackButton.setBounds(580, 285, 240, 240);
        theBlackButton.setBackground(programAssets.clrBlack);
        theBlackButton.setBorder(null);
        this.add(theBlackButton);

        //Start Button Formatting
        theStartButton.setBounds(390, 190, 180, 180);
        theStartButton.setFont(programAssets.fntHelvetica20);
        theStartButton.setForeground(programAssets.clrBlack);
        theStartButton.setBackground(programAssets.clrGray);
        this.add(theStartButton);

        //Spectator Button Formatting
        theSpectatorButton.setBounds(390, 380, 180, 180);
        theSpectatorButton.setFont(programAssets.fntHelvetica20);
        theSpectatorButton.setForeground(programAssets.clrBlack);
        theSpectatorButton.setBackground(programAssets.clrGray);
        this.add(theSpectatorButton);
    }
}
