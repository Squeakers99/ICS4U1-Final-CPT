package Panels;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class GameScreen extends JPanel {
    Assets programAssets = new Assets();

    //Creates JLabels, JButtons, and JTextFields for the panel
    public JTextField theChatField = new JTextField("Message Here");
    public JTextArea theChatArea = new JTextArea();

    private final JLabel theChatTitle = new JLabel("Game Chat");
    private final JScrollPane theChatScroll = new JScrollPane(theChatArea);

    //Board to manage drawing
    public String[][] strBoard = new String[8][8];
    public String strRole = null;

    //Variables to manage pieces being dragged
    public int intMouseX = 1325;
    public int intMouseY = 765;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Draws the background image
        g.drawImage(programAssets.imgBackground, 0, 0, null);

        //Draws the Sidebar
        g.setColor(programAssets.clrGray);
        g.fillRect(960, 0, 320, 720);

        //Draws the board
        g.drawImage(programAssets.imgBoard, 120, 0, null);

        //Draws the pieces on the board
        for (int intOuterLoop = 0; intOuterLoop < 8; intOuterLoop++) {
            for (int intInnerLoop = 0; intInnerLoop < 8; intInnerLoop++) {
                if (strBoard[intOuterLoop][intInnerLoop] != null) {
                    if (strBoard[intOuterLoop][intInnerLoop].equals("1")) {
                        if(strRole.equals("1")){
                            g.drawImage(programAssets.imgRed, 120 + (intInnerLoop * 90), (intOuterLoop * 90), null);
                        }else{
                            g.drawImage(programAssets.imgRed, 120 + (intInnerLoop * 90), 630 - (intOuterLoop * 90), null);
                        }
                    } else if (strBoard[intOuterLoop][intInnerLoop].equals("2")) {
                        if(strRole.equals("1")){
                            g.drawImage(programAssets.imgBlack, 120 + (intInnerLoop * 90), (intOuterLoop * 90), null);
                        }else{
                            g.drawImage(programAssets.imgBlack, 120 + (intInnerLoop * 90), 630 - (intOuterLoop * 90), null);
                        }
                    }
                }
            }
        }

        //Draws the piece being dragged
        if (strRole.equals("1")) {
            g.drawImage(programAssets.imgRed, intMouseX - 45, intMouseY - 45, null);
        } else {
            g.drawImage(programAssets.imgBlack, intMouseX - 45, intMouseY - 45, null);
        }
    }

    public GameScreen() {
        //Panel Formatting
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(null);

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
    }
}
