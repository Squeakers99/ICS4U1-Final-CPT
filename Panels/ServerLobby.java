package Panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * The ServerLobby class represents a JPanel that displays the server lobby interface.
 * It contains various JLabels, JButtons, and JTextFields for the panel.
 * The class also overrides the paintComponent method to customize the appearance of the panel.
 */
public class ServerLobby extends JPanel {

    //Imports all assets into the program
    Assets programAssets = new Assets();

    //Creates JLabels, JButtons, and JTextFields for the panel
    /** The title of the server lobby */
    public JLabel theServerTitle = new JLabel("Server Lobby");
    /** The chat field to chat with everyone in the lobby */
    public JTextField theChatField = new JTextField("Message Here");
    /** The IP address of the server */
    public JLabel theIPAdress = new JLabel();
    /** Area for the messages to display */
    public JTextArea theChatArea = new JTextArea();
    /** Button to join the red team */
    public JButton theRedButton = new JButton("Join Red");
    /** Button to join the black team */
    public JButton theBlackButton = new JButton("Join Black");
    /** Button to start the game */
    public JButton theStartButton = new JButton("Start Game");
    /** Button to join as a spectator */
    public JButton theSpectatorButton = new JButton("Join Spectator");
    /** Label to display the number of red players */
    public JLabel theRedPlayers = new JLabel("Red Players: 0");
    /** Label to display the number of black players */
    public JLabel theBlackPlayers = new JLabel("Black Players: 0");
    /** Label to display the number of spectator players */
    public JLabel theSpectatorPlayers = new JLabel("Spectators: 0");

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

    /**
     * Constructs a new ServerLobby panel.
     */
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

        //Red Players Number Formatting
        theRedPlayers.setFont(programAssets.fntHelvetica30);
        theRedPlayers.setForeground(programAssets.clrBlack);
        theRedPlayers.setBounds(960, 570, 320, 40);
        theRedPlayers.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theRedPlayers);

        //Black Players Number Formatting
        theBlackPlayers.setFont(programAssets.fntHelvetica30);
        theBlackPlayers.setForeground(programAssets.clrBlack);
        theBlackPlayers.setBounds(960, 610, 320, 40);
        theBlackPlayers.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theBlackPlayers);

        //Spectator Players Number Formatting
        theSpectatorPlayers.setFont(programAssets.fntHelvetica30);
        theSpectatorPlayers.setForeground(programAssets.clrBlack);
        theSpectatorPlayers.setBounds(960, 650, 320, 40);
        theSpectatorPlayers.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theSpectatorPlayers);
    }
}
