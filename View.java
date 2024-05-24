import Panels.*;
import java.awt.event.*;
import javax.swing.*;

public class View implements ActionListener{
    String strTempArray1[];
    String strTempArray2[][];

    Model theModel = new Model(this);

    MainScreen theMainScreen = new MainScreen();
    ServerLobby theServerLobby = new ServerLobby();

    JFrame theFrame = new JFrame("Main Screen");

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == theMainScreen.theHostButton){
            if(theModel.initializeHost(theMainScreen.theNameField.getText())){
                theServerLobby.theIPAdress.setText(theModel.theSocket.getMyAddress());
                theServerLobby.theServerTitle.setText(theMainScreen.theNameField.getText() + "'s Lobby");
            }else{
                theMainScreen.theNameField.setText("Player 1");
            }
            theFrame.setContentPane(theServerLobby);
            theFrame.pack();
        }else if(e.getSource() == theMainScreen.theJoinButton){
            theModel.strUsername = theMainScreen.theNameField.getText();
            theModel.initializeClient(theModel.strUsername, "10.0.0.94");
        }else if(e.getSource() == theMainScreen.theHelpButton){
            System.out.println("Help Button Pressed");
        }else if(e.getSource() == theServerLobby.theChatField){
            theServerLobby.theChatArea.append(theMainScreen.theNameField.getText() + ": " + theServerLobby.theChatField.getText() + "\n");
            if(theModel.blnIsHost){
                theModel.sendMessage(theModel.strUsername, "0", "0", "0", theServerLobby.theChatField.getText());
            }else{
                theModel.sendMessage(theModel.strUsername, "1", "1", "0", theServerLobby.theChatField.getText());
            }
            theServerLobby.theChatField.setText("");
        }
        if(e.getSource() == theModel.theSocket){
            //Gets the message from the socket
            theModel.receiveMessage(theModel.theSocket.readText());

            //Intended for Host
            if(theModel.strMessage[1].equals("0")){
                //Action 0: Client Connected
                if(theModel.strMessage[3].equals("0")){
                    theModel.intClientsConnected++;
                    strTempArray1 = theModel.StringToArray1(theModel.strMessage[4]);
                    theModel.strPlayerList[theModel.intClientsConnected][0] = strTempArray1[0];
                    theModel.strPlayerList[theModel.intClientsConnected][1] = strTempArray1[1];
                    theServerLobby.theChatArea.append("Server: " + theModel.strPlayerList[theModel.intClientsConnected][0] + " has joined the lobby\n");
                    theModel.sendMessage(null, "1", null, "0", theModel.ArrayToString2(theModel.strPlayerList));
                }
                //Action 1: Server Lobby Text
                if(theModel.strMessage[3].equals("1")){
                    theServerLobby.theChatArea.append(theModel.strMessage[0] + ": " + theModel.strMessage[4] + "\n");
                }
            }
            //Intended for Client
            else if(theModel.strMessage[1].equals("1")){
                //Action 0: Server Chat Message
                if(theModel.strMessage[3].equals("0")){
                    strTempArray2 = theModel.StringToArray2(theModel.strMessage[4]);
                    theModel.strPlayerList = strTempArray2;
                    theServerLobby.theServerTitle.setText(theModel.strPlayerList[0][0] + "'s Lobby");
                    theFrame.setContentPane(theServerLobby);
                    theFrame.pack();
                }
                //Action 1: Server Lobby Text
                if(theModel.strMessage[3].equals("1")){
                    theServerLobby.theChatArea.append(theModel.strMessage[0] + ": " + theModel.strMessage[4] + "\n");
                }
            }
        }
    }

    public View(){
        //Main Screen Action Listeners
        theMainScreen.theHostButton.addActionListener(this);
        theMainScreen.theJoinButton.addActionListener(this);
        theMainScreen.theHelpButton.addActionListener(this);

        //Server Lobby Action Listeners
        theServerLobby.theChatField.addActionListener(this);

        //Frame Setup
        theFrame.setVisible(true);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setContentPane(theMainScreen);
        theFrame.pack();
    }
    public static void main(String[] args){
        new View();
    }
}
