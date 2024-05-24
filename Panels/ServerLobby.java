package Panels;

import java.awt.*;
import javax.swing.*;

public class ServerLobby extends JPanel {
    //Imports all assets into the program
    Assets programAssets = new Assets();

    //Creates JLabels, JButtons, and JTextFields for the panel
    JLabel theServerTitle = new JLabel("Server Lobby");
    JLabel theChatTitle = new JLabel("Server Chat");
    JTextArea theChatArea = new JTextArea();
    JScrollPane theChatScroll = new JScrollPane(theChatArea);

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
        theServerTitle.setFont(programAssets.fntHelvetica120);
        theServerTitle.setForeground(programAssets.clrWhite);
        theServerTitle.setBounds(0, 30, 960, 150);
        theServerTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theServerTitle);

        //Chat Title Formatting
        theChatTitle.setFont(programAssets.fntHelvetica30);
        theChatTitle.setForeground(programAssets.clrBlack);
        theChatTitle.setBounds(960, 30, 320, 40);
        theChatTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theChatTitle);
    }
}