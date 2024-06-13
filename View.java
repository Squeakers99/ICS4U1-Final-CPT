
import Panels.*;
import java.awt.event.*;
import javax.swing.*;

public class View implements ActionListener, MouseMotionListener, MouseListener {

    Model theModel = new Model(this);

    Assets programAssets = new Assets();
    MainScreen theMainScreen = new MainScreen();
    ServerLobby theServerLobby = new ServerLobby();
    JoinIP theIPScreen = new JoinIP();
    GameScreen theGameScreen = new GameScreen();
    ThemeSelect theThemeSelect = new ThemeSelect();
    ClientWaiting theClientWaiting = new ClientWaiting();

    JFrame theFrame = new JFrame("Checkers");

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == theMainScreen.theHostButton) {
            if (theModel.initializeHost(theMainScreen.theNameField.getText())) {
                theServerLobby.theIPAdress.setText(theModel.theSocket.getMyAddress());
                theServerLobby.theServerTitle.setText("My Lobby");
                theServerLobby.theSpectatorPlayers.setText("Spectators: " + theModel.intRoleData[0]);
                theFrame.setContentPane(theServerLobby);
            } else {
                theMainScreen.theNameField.setText("Player");
            }
        } else if (e.getSource() == theMainScreen.theJoinButton) {
            if (theMainScreen.theNameField.getText().equals("")) {
                theMainScreen.theNameField.setText("Player");
            } else {
                theModel.strUsername = theMainScreen.theNameField.getText();
                theFrame.setContentPane(theIPScreen);
            }
        } else if (e.getSource() == theMainScreen.theHelpButton) {
            theModel.strChosenTheme = theThemeSelect.theThemeActions.getThemeData("Default");
            theFrame.setContentPane(theThemeSelect);
        } else if (e.getSource() == theServerLobby.theChatField) {
            if (theModel.blnIsHost) {
                theServerLobby.theChatArea.append(theMainScreen.theNameField.getText() + ": " + theServerLobby.theChatField.getText() + "\n");
                theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "1", theServerLobby.theChatField.getText(), null, null);
            } else {
                theModel.sendMessage(theModel.strUsername, "0", theModel.strRole, "1", theServerLobby.theChatField.getText(), null, null);
            }
            theServerLobby.theChatField.setText("");
        } else if (e.getSource() == theIPScreen.theJoinButton) {
            if (theModel.initializeClient(theMainScreen.theNameField.getText(), theIPScreen.theIPField.getText()) && !theIPScreen.theIPField.getText().equals("")) {
                theServerLobby.theIPAdress.setText(theIPScreen.theIPField.getText());
                theFrame.setContentPane(theServerLobby);
            } else {
                theIPScreen.theErrorMessage.setText("Server Not Found");
            }
        } else if (e.getSource() == theServerLobby.theRedButton) {
            if (theModel.intRoleData[1] == 0) {
                SwitchRoles("1");
            } else {
                theServerLobby.theChatArea.append("Server: Red Team is Full\n");
            }
        } else if (e.getSource() == theServerLobby.theBlackButton) {
            if (theModel.intRoleData[2] == 0) {
                SwitchRoles("2");
            } else {
                theServerLobby.theChatArea.append("Server: Black Team is Full\n");
            }
        } else if (e.getSource() == theServerLobby.theStartButton) {
            if (theModel.intRoleData[1] > 0 && theModel.intRoleData[2] > 0) {
                theModel.strChosenTheme = theThemeSelect.theThemeActions.getThemeData("Default");
                theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "3", null, null, null);
                theFrame.setContentPane(theThemeSelect);
            } else {
                theServerLobby.theChatArea.append("Server: Both Teams Must Have At Least One Player\n");
            }
        } else if (e.getSource() == theServerLobby.theSpectatorButton) {
            SwitchRoles("0");
        } else if (e.getSource() == theThemeSelect.theSelectButton) {
            theModel.loadImages();
            theModel.loadBoard();
            theGameScreen.strBoard = theModel.strBoard;
            theGameScreen.strRole = theModel.strRole;
            theFrame.setContentPane(theGameScreen);
            switch (theModel.strRole) {
                case "0" ->
                    theGameScreen.theTeam.setText("Spectator");
                case "1" -> {
                    theGameScreen.theTeam.setText("Team Red");
                    theModel.blnIsMyTurn = true;
                }
                default ->
                    theGameScreen.theTeam.setText("Team Black");
            }
            theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "4", theModel.ArrayToString1(theModel.strChosenTheme), null, null);
        } else if (e.getSource() == theModel.theSocket) {
            //Gets the message from the socket
            theModel.receiveMessage(theModel.theSocket.readText());

            //Intended for Host
            if (theModel.strMessage[1].equals("0") && theModel.blnIsHost) {
                //Action 0: Client Connected
                if (theModel.strMessage[3].equals("0")) {
                    //Updates the variables
                    theModel.intPlayersConnected++;
                    theModel.strPlayerList[theModel.intPlayersConnected - 1] = theModel.strMessage[4];
                    theModel.intRoleData[0]++;

                    //Updates the Panel
                    theServerLobby.theSpectatorPlayers.setText("Spectators: " + theModel.intRoleData[0]);
                    theServerLobby.theChatArea.append("Server: " + theModel.strPlayerList[theModel.intPlayersConnected - 1] + " has joined the lobby\n");
                    theFrame.setContentPane(theServerLobby);

                    //Sends the message to all clients
                    theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "0", theModel.ArrayToString1(theModel.strPlayerList), theModel.ArrayToString1(theModel.intRoleData), String.valueOf(theModel.intPlayersConnected));
                }
                //Action 1: Server Lobby Text
                if (theModel.strMessage[3].equals("1")) {
                    theServerLobby.theChatArea.append(theModel.strMessage[0] + ": " + theModel.strMessage[4] + "\n");
                    theModel.sendMessage(theModel.strMessage[0], "1", null, "1", theModel.strMessage[4], null, null);
                }
                //Action 2: Client Role Change
                if (theModel.strMessage[3].equals("2")) {
                    //Sends a message in chat
                    updateChat(theModel.strMessage[0], theModel.strMessage[4]);
                    UpdateRoles(theModel.strMessage[0], theModel.strMessage[2], theModel.strMessage[4]);
                    theModel.sendMessage(theModel.strMessage[0], "1", theModel.strMessage[4], "2", theModel.ArrayToString1(theModel.intRoleData), null, null); //Alteration to usual format...sends client username/role in place of username/role
                }
                //Action 3: Client Moved
                if (theModel.strMessage[3].equals("3")) {
                    theModel.strBoard = theModel.StringToArray2(theModel.strMessage[4]);
                    theGameScreen.strBoard = theModel.strBoard;
                    theGameScreen.repaint();
                    if (!theModel.strRole.equals("0")) {
                        theModel.blnIsMyTurn = true;
                    }
                    theModel.intRedPieces = Integer.parseInt(theModel.strMessage[5]);
                    theModel.intBlackPieces = Integer.parseInt(theModel.strMessage[6]);
                    theGameScreen.theRedPiecesLeft.setText("Red Pieces Left: " + theModel.intRedPieces);
                    theGameScreen.theBlackPiecesLeft.setText("Black Pieces Left: " + theModel.intBlackPieces);
                    theModel.sendMessage(theModel.strMessage[0], "1", theModel.strRole, "6", theModel.ArrayToString2(theModel.strBoard), String.valueOf(theModel.intRedPieces), String.valueOf(theModel.intBlackPieces));
                }
                //Intended for Client
            } else if (theModel.strMessage[1].equals("1") && !theModel.blnIsHost) {
                //Action 0: New Client Joined
                if (theModel.strMessage[3].equals("0")) {
                    //Updates all variables
                    theModel.strPlayerList = theModel.StringToStrArray1(theModel.strMessage[4]);
                    theModel.intRoleData = theModel.StringToIntArray1(theModel.strMessage[5]);
                    theModel.intPlayersConnected = Integer.parseInt(theModel.strMessage[6]);

                    //Updates the Panel
                    theServerLobby.theServerTitle.setText(theModel.strPlayerList[0] + "'s Lobby");
                    theServerLobby.theSpectatorPlayers.setText("Spectators: " + theModel.intRoleData[0]);
                    theServerLobby.theRedPlayers.setText("Red Players: " + theModel.intRoleData[1]);
                    theServerLobby.theBlackPlayers.setText("Black Players: " + theModel.intRoleData[2]);
                    theServerLobby.theChatArea.append("Server: " + theModel.strPlayerList[theModel.intPlayersConnected - 1] + " has joined the lobby\n");
                    theServerLobby.theStartButton.setEnabled(false);

                    //Updates the frame
                    theFrame.setContentPane(theServerLobby);
                }
                //Action 1: Server Lobby Text
                if (theModel.strMessage[3].equals("1")) {
                    //Goofy error cuz idk why
                    theServerLobby.theChatArea.append(theModel.strMessage[0] + ": " + theModel.strMessage[4] + "\n");
                }
                //Action 2: Client Role Change
                if (theModel.strMessage[3].equals("2")) {
                    //Updates the variables
                    theModel.intRoleData = theModel.StringToIntArray1(theModel.strMessage[4]);

                    //Updates the Panel
                    theServerLobby.theSpectatorPlayers.setText("Spectators: " + theModel.intRoleData[0]);
                    theServerLobby.theRedPlayers.setText("Red Players: " + theModel.intRoleData[1]);
                    theServerLobby.theBlackPlayers.setText("Black Players: " + theModel.intRoleData[2]);

                    //Updates chat
                    updateChat(theModel.strMessage[0], theModel.strMessage[2]);
                }
                //Action 3: Host Started Game
                if (theModel.strMessage[3].equals("3")) {
                    theFrame.setContentPane(theClientWaiting);
                }
                //Action 4: Host Selected a theme
                if (theModel.strMessage[3].equals("4")) {
                    theModel.strChosenTheme = theModel.StringToStrArray1(theModel.strMessage[4]);
                    theModel.loadImages();
                    theModel.loadBoard();
                    theGameScreen.strBoard = theModel.strBoard;
                    theGameScreen.strRole = theModel.strRole;
                    theFrame.setContentPane(theGameScreen);
                    switch (theModel.strRole) {
                        case "0" ->
                            theGameScreen.theTeam.setText("Spectator");
                        case "1" -> {
                            theGameScreen.theTeam.setText("Team Red");
                            theModel.blnIsMyTurn = true;
                        }
                        default ->
                            theGameScreen.theTeam.setText("Team Black");
                    }
                }
                //Action 5: Host Moved
                if (theModel.strMessage[3].equals("5")) {
                    theModel.strBoard = theModel.StringToArray2(theModel.strMessage[4]);
                    theGameScreen.strBoard = theModel.strBoard;
                    theGameScreen.repaint();
                    if (!theModel.strRole.equals("0")) {
                        theModel.blnIsMyTurn = true;
                    }
                    theModel.intRedPieces = Integer.parseInt(theModel.strMessage[5]);
                    theModel.intBlackPieces = Integer.parseInt(theModel.strMessage[6]);
                    theGameScreen.theRedPiecesLeft.setText("Red Pieces Left: " + theModel.intRedPieces);
                    theGameScreen.theBlackPiecesLeft.setText("Black Pieces Left: " + theModel.intBlackPieces);
                }
                //Action 6: Updates all spectators with the new board
                if (theModel.strMessage[3].equals("6")) {
                    theModel.strBoard = theModel.StringToArray2(theModel.strMessage[4]);
                    theGameScreen.strBoard = theModel.strBoard;
                    theGameScreen.repaint();
                    if (!theModel.strMessage[0].equals(theModel.strUsername) && !theModel.strRole.equals("0")) {
                        theModel.blnIsMyTurn = true;
                    }
                    theModel.intRedPieces = Integer.parseInt(theModel.strMessage[5]);
                    theModel.intBlackPieces = Integer.parseInt(theModel.strMessage[6]);
                    theGameScreen.theRedPiecesLeft.setText("Red Pieces Left: " + theModel.intRedPieces);
                    theGameScreen.theBlackPiecesLeft.setText("Black Pieces Left: " + theModel.intBlackPieces);
                }
            }
        }
        //Action Listener for Theme Select
        for (int intLoop1 = 0; intLoop1 < theThemeSelect.themeButtons.length; intLoop1++) {
            if (e.getSource() == theThemeSelect.themeButtons[intLoop1]) {
                for (int intLoop2 = 0; intLoop2 < theThemeSelect.themeButtons.length; intLoop2++) {
                    if (theThemeSelect.themeButtons[intLoop2].getText().equals(theModel.strChosenTheme[0])) {
                        theThemeSelect.themeButtons[intLoop2].setBackground(programAssets.clrWhite);
                        break;
                    }
                }
                theModel.strChosenTheme = theThemeSelect.theThemeActions.getThemeData(theThemeSelect.themeButtons[intLoop1].getText());
                theThemeSelect.themeButtons[intLoop1].setBackground(programAssets.clrGreen);
            }
        }
        theFrame.pack();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //Updates the piece being dragged
        if (theModel.blnPieceSelected) {
            theGameScreen.intMouseX = e.getX();
            theGameScreen.intMouseY = e.getY();
            theGameScreen.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() > 120 && e.getX() < 1080 && theFrame.getContentPane() == theGameScreen && (theModel.strRole.equals("1") || theModel.strRole.equals("2")) && theModel.blnIsMyTurn) {
            //Takes the piece off the board
            if (theModel.strRole.equals("1")) {
                theModel.intCurrentCol = (int) (e.getY() / 90);
            } else {
                theModel.intCurrentCol = (int) ((720 - e.getY()) / 90);
            }
            theModel.intCurrentRow = (int) ((e.getX() - 120) / 90);
            if (theModel.strBoard[theModel.intCurrentCol][theModel.intCurrentRow].equals(theModel.strRole)) {
                theModel.blnPieceSelected = true;
                theModel.strBoard[theModel.intCurrentCol][theModel.intCurrentRow] = " ";
                theGameScreen.strBoard = theModel.strBoard;

                //Draws the piece being dragged
                theGameScreen.intMouseX = e.getX();
                theGameScreen.intMouseY = e.getY();

                //Repaints the screen
                theGameScreen.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //Puts the piece being dragged back in the corner
        theGameScreen.intMouseX = 1325;
        theGameScreen.intMouseY = 765;

        //Puts the piece back on the board
        if (theModel.blnPieceSelected) {
            if (theModel.strRole.equals("1")) {
                theModel.intRequestedCol = (int) (e.getY() / 90);
            } else {
                theModel.intRequestedCol = (int) ((720 - e.getY()) / 90);
            }
            theModel.intRequestedRow = (int) ((e.getX() - 120) / 90);
            if (theModel.blnJumpAvailable) {
                theModel.blnValidMove = theModel.validateJump();
            } else {
                theModel.blnValidMove = theModel.validateMove();
            }
            if (!theModel.blnValidMove) {
                theModel.strBoard[theModel.intCurrentCol][theModel.intCurrentRow] = theModel.strRole;
                theGameScreen.strBoard = theModel.strBoard;
                theGameScreen.repaint();
                System.out.println("Invalid Move");
            } else {
                theModel.strBoard[theModel.intRequestedCol][theModel.intRequestedRow] = theModel.strRole;
                theGameScreen.strBoard = theModel.strBoard;
                theGameScreen.repaint();
                if (!theModel.blnJumpAvailable) {
                    theModel.blnIsMyTurn = false;
                    if (theModel.blnIsHost) {
                        theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "5", theModel.ArrayToString2(theModel.strBoard), String.valueOf(theModel.intRedPieces), String.valueOf(theModel.intBlackPieces));
                    } else {
                        theModel.sendMessage(theModel.strUsername, "0", theModel.strRole, "3", theModel.ArrayToString2(theModel.strBoard), String.valueOf(theModel.intRedPieces), String.valueOf(theModel.intBlackPieces));
                    }
                }
            }
            theModel.blnPieceSelected = false;
        }
    }

    //Unused Mouse Listeners - Mandatory overrides
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    public void UpdateRoles(String strUsername, String strOldRole, String strNewRole) {
        //Subtracts from previous role and adds to new role
        theModel.intRoleData[Integer.parseInt(strOldRole)]--;
        theModel.intRoleData[Integer.parseInt(strNewRole)]++;

        //Updates the Panel
        theServerLobby.theSpectatorPlayers.setText("Spectators: " + theModel.intRoleData[0]);
        theServerLobby.theRedPlayers.setText("Red Players: " + theModel.intRoleData[1]);
        theServerLobby.theBlackPlayers.setText("Black Players: " + theModel.intRoleData[2]);
    }

    public void SwitchRoles(String strNewRole) {
        if (theModel.blnIsHost) {
            UpdateRoles(theModel.strUsername, theModel.strRole, strNewRole);
            theModel.sendMessage(theModel.strUsername, "1", strNewRole, "2", theModel.ArrayToString1(theModel.intRoleData), null, null);
            updateChat(theModel.strUsername, strNewRole);
        } else {
            theModel.sendMessage(theModel.strUsername, "0", theModel.strRole, "2", strNewRole, null, null);
        }
        theModel.strRole = strNewRole;
    }

    public void updateChat(String strUsername, String strRole) {
        //Sends a message in chat
        switch (strRole) {
            case "0" ->
                theServerLobby.theChatArea.append("Server: " + strUsername + " has switched to Spectator\n");
            case "1" ->
                theServerLobby.theChatArea.append("Server: " + strUsername + " has switched to Red\n");
            case "2" ->
                theServerLobby.theChatArea.append("Server: " + strUsername + " has switched to Black\n");
            default -> {
            }
        }
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

        //Join IP Action Listeners
        theIPScreen.theJoinButton.addActionListener(this);

        //Theme Screen action listeners
        for (int intLoop = 0; intLoop < theThemeSelect.themeButtons.length; intLoop++) {
            theThemeSelect.themeButtons[intLoop].addActionListener(this);
        }
        theThemeSelect.theSelectButton.addActionListener(this);

        //Mouse Listeners
        theFrame.addMouseListener(this);
        theFrame.addMouseMotionListener(this);

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
