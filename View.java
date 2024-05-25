
import Panels.*;
import java.awt.event.*;
import javax.swing.*;

public class View implements ActionListener {

    String strTempArray1[];
    String strTempArray2[][];

    Model theModel = new Model(this);

    MainScreen theMainScreen = new MainScreen();
    ServerLobby theServerLobby = new ServerLobby();

    JFrame theFrame = new JFrame("Main Screen");

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == theMainScreen.theHostButton) {
            if (theModel.initializeHost(theMainScreen.theNameField.getText())) {
                theServerLobby.theIPAdress.setText(theModel.theSocket.getMyAddress());
                theServerLobby.theServerTitle.setText("My Lobby");
                theFrame.setContentPane(theServerLobby);
            } else {
                theMainScreen.theNameField.setText("Player");
            }
        } else if (e.getSource() == theMainScreen.theJoinButton) {
            if (theModel.initializeClient(theMainScreen.theNameField.getText(), "10.0.0.94")) {
                theServerLobby.theIPAdress.setText("10.0.0.94");
                theModel.strUsername = theMainScreen.theNameField.getText();
                theFrame.setContentPane(theServerLobby);
            } else {
                theMainScreen.theNameField.setText("Player");
            }
        } else if (e.getSource() == theMainScreen.theHelpButton) {
            System.out.println("Help Button Pressed");
        } else if (e.getSource() == theServerLobby.theChatField) {
            theServerLobby.theChatArea.append(theMainScreen.theNameField.getText() + ": " + theServerLobby.theChatField.getText() + "\n");
            if (theModel.blnIsHost) {
                theModel.sendMessage(theModel.strUsername, "1", "0", "1", theServerLobby.theChatField.getText(), null);
            } else {
                theModel.sendMessage(theModel.strUsername, "0", "0", "1", theServerLobby.theChatField.getText(), null);
            }
            theServerLobby.theChatField.setText("");
        }
        if (e.getSource() == theModel.theSocket) {
            //Gets the message from the socket
            theModel.receiveMessage(theModel.theSocket.readText());

            //Intended for Host
            if (theModel.strMessage[1].equals("0")) {
                //Action 0: Client Connected
                if (theModel.strMessage[3].equals("0")) {
                    theModel.intPlayersConnected++;
                    strTempArray1 = theModel.StringToArray1(theModel.strMessage[4]);
                    theModel.strPlayerList[theModel.intPlayersConnected - 1][0] = strTempArray1[0];
                    theModel.strPlayerList[theModel.intPlayersConnected - 1][1] = strTempArray1[1];
                    theServerLobby.theChatArea.append("Server: " + theModel.strPlayerList[theModel.intPlayersConnected - 1][0] + " has joined the lobby\n");
                    theModel.sendMessage(null, "1", null, "0", theModel.ArrayToString2(theModel.strPlayerList), String.valueOf(theModel.intPlayersConnected));
                }
                //Action 1: Server Lobby Text
                if (theModel.strMessage[3].equals("1")) {
                    theServerLobby.theChatArea.append(theModel.strMessage[0] + ": " + theModel.strMessage[4] + "\n");
                }
            } //Intended for Client
            else if (theModel.strMessage[1].equals("1")) {
                //Action 0: New Client Joined
                if (theModel.strMessage[3].equals("0")) {
                    strTempArray2 = theModel.StringToArray2(theModel.strMessage[4]);
                    theModel.strPlayerList = strTempArray2;
                    theModel.intPlayersConnected = theModel.strPlayerList.length;
                    theServerLobby.theServerTitle.setText(theModel.strPlayerList[0][0] + "'s Lobby");
                    theModel.intPlayersConnected = Integer.parseInt(theModel.strMessage[5]);
                    theServerLobby.theChatArea.append("Server: " + theModel.strPlayerList[theModel.intPlayersConnected - 1][0] + " has joined the lobby\n");
                    theFrame.setContentPane(theServerLobby);
                }
                //Action 1: Server Lobby Text
                if (theModel.strMessage[3].equals("1")) {
                    theServerLobby.theChatArea.append(theModel.strMessage[0] + ": " + theModel.strMessage[4] + "\n");
                }
            }
        }
        theFrame.pack();
    }

    public View() {
        //Main Screen Action Listeners
        theMainScreen.theHostButton.addActionListener(this);
        theMainScreen.theJoinButton.addActionListener(this);
        theMainScreen.theHelpButton.addActionListener(this);

        //Server Lobby Action Listeners
        theServerLobby.theChatField.addActionListener(this);
        theServerLobby.theRedButton.addActionListener(this);
        theServerLobby.theBlackButton.addActionListener(this);
        theServerLobby.theStartButton.addActionListener(this);
        theServerLobby.theSpectatorButton.addActionListener(this);

        //Frame Setup
        theFrame.setVisible(true);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setContentPane(theMainScreen);
        theFrame.pack();
    }

    public static void main(String[] args) {
        new View();
    }
}
