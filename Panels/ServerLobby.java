package Panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class ServerLobby extends JPanel {
    //Imports all assets into the program
    Assets programAssets = new Assets();

    //Creates JLabels, JButtons, and JTextFields for the panel
    public JLabel theServerTitle = new JLabel("Server Lobby");
    private final JLabel theChatTitle = new JLabel("Server Chat");
    public JTextField theChatField = new JTextField("Message Here");
    private final JLabel theIPTitle = new JLabel("Server IP Address:");
    public JLabel theIPAdress = new JLabel("255.255.255.255");
    public JTextArea theChatArea = new JTextArea();
    private final JScrollPane theChatScroll = new JScrollPane(theChatArea);

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(programAssets.imgBackground, 0, 0, null);
        g.setColor(programAssets.clrGray);
        g.fillRect(960, 0, 320, 720);
    }

    public ServerLobby(){
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
    }
}