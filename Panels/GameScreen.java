package Panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * The `GameScreen` class represents the game screen panel in the application.
 * It extends the `JPanel` class and contains various components such as labels,
 * buttons, and text fields for the game interface.
 */
public class GameScreen extends JPanel {
    Assets programAssets = new Assets();

    //Creates JLabels, JButtons, and JTextFields for the panel
    /** The chat field to chat with everyone on the server */
    public JTextField theChatField = new JTextField("Message Here");
    /** Area for the messages to display */
    public JTextArea theChatArea = new JTextArea();
    /** Red Pieces Left on the board */
    public JLabel theRedPiecesLeft = new JLabel("Red Pieces Left: 12");
    /** Black Pieces Left on the board */
    public JLabel theBlackPiecesLeft = new JLabel("Black Pieces Left: 12");
    /** The team the player is on */
    public JLabel theTeam = new JLabel("Team Red");

    //Private variables
    private final JLabel theChatTitle = new JLabel("Game Chat");
    private final JScrollPane theChatScroll = new JScrollPane(theChatArea);

    //Board to manage drawing
    /** Board to draw */
    public String[][] strBoard = new String[8][8];
    /** Role of the player */
    public String strRole = null;

    //Variables to manage pieces being dragged
    /** X position of the mouse */
    public int intMouseX = 1325;
    /** Y position of the mouse */
    public int intMouseY = 765;

    //Help Scren Variable
    /** If the help screen is being displayed */
    public boolean blnHelpScreen = false;

    /**
     * Paints the background image on the panel.
     */
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
                        if(!strRole.equals("2")){
                            g.drawImage(programAssets.imgRed, 120 + (intInnerLoop * 90), (intOuterLoop * 90), null);
                        }else{
                            g.drawImage(programAssets.imgRed, 120 + (intInnerLoop * 90), 630 - (intOuterLoop * 90), null);
                        }
                    } else if (strBoard[intOuterLoop][intInnerLoop].equals("2")) {
                        if(!strRole.equals("2")){
                            g.drawImage(programAssets.imgBlack, 120 + (intInnerLoop * 90), (intOuterLoop * 90), null);
                        }else{
                            g.drawImage(programAssets.imgBlack, 120 + (intInnerLoop * 90), 630 - (intOuterLoop * 90), null);
                        }
                    }
                }
            }
        }

        //Draws the piece being dragged
        g.drawImage(programAssets.imgDragged, intMouseX - 45, intMouseY - 45, null);

        //Draws the help screen if its the help screen
        if(blnHelpScreen){
            g.drawImage(programAssets.imgHelp, 0, 0, null);
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

        //Red Pieces Left Formatting
        theRedPiecesLeft.setFont(programAssets.fntHelvetica30);
        theRedPiecesLeft.setForeground(programAssets.clrBlack);
        theRedPiecesLeft.setBounds(960, 520, 320, 40);
        theRedPiecesLeft.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theRedPiecesLeft);

        //Black Pieces Left Formatting
        theBlackPiecesLeft.setFont(programAssets.fntHelvetica30);
        theBlackPiecesLeft.setForeground(programAssets.clrBlack);
        theBlackPiecesLeft.setBounds(960, 570, 320, 40);
        theBlackPiecesLeft.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theBlackPiecesLeft);

        //Team Formatting
        theTeam.setFont(programAssets.fntHelvetica30);
        theTeam.setForeground(programAssets.clrBlack);
        theTeam.setBounds(960, 620, 320, 40);
        theTeam.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theTeam);

        this.repaint();
    }
}