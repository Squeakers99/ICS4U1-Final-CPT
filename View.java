/*
 * Checkers: The Game
 * Created by Jayred Robles, Kelvin Wang, and Soheil Rajabali
 * V 1.0
 */

// importing required libraries
import Panels.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * The View class implements ActionListener, MouseMotionListener, and MouseListener interfaces.
 * It represents the graphical user interface of the application and handles user interactions.
 */
public class View implements ActionListener, MouseMotionListener, MouseListener {

    // Initializing objects
    Model theModel = new Model(this);

    //Panels
    Assets programAssets = new Assets();
    MainScreen theMainScreen = new MainScreen();
    ServerLobby theServerLobby = new ServerLobby();
    JoinIP theIPScreen = new JoinIP();
    GameScreen theGameScreen = new GameScreen();
    ThemeSelect theThemeSelect = new ThemeSelect();
    ClientWaiting theClientWaiting = new ClientWaiting();
    GameScreen theHelpScreen = new GameScreen();
    GameOver theGameOver = new GameOver();

    // Creating JFrame
    JFrame theFrame = new JFrame("Checkers");

    /**
     * Overided methods for swing actions
     * @param e triggered events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Host button - Main Panel
        if (e.getSource() == theMainScreen.theHostButton) {
            if (theModel.initializeHost(theMainScreen.theNameField.getText())) {
                theServerLobby.theIPAdress.setText(theModel.theSocket.getMyAddress());
                theServerLobby.theServerTitle.setText("My Lobby");
                theServerLobby.theSpectatorPlayers.setText("Spectators: " + theModel.intRoleData[0]);
                theFrame.setContentPane(theServerLobby);
            } else {
                theMainScreen.theNameField.setText("Player");
            }
        //Join button - Main Panel
        } else if (e.getSource() == theMainScreen.theJoinButton) {
            if (theMainScreen.theNameField.getText().equals("")) {
                theMainScreen.theNameField.setText("Player");
            } else {
                theModel.strUsername = theMainScreen.theNameField.getText();
                theFrame.setContentPane(theIPScreen);
            }
        //Help button - Main Panel
        } else if (e.getSource() == theMainScreen.theHelpButton) {
            theModel.strChosenTheme = theThemeSelect.theThemeActions.getThemeData("Default");
            theModel.strRole = "1";
            theHelpScreen.strRole = "1";
            theModel.loadBoard();
            theModel.loadImages();
            theHelpScreen.strBoard = theModel.strBoard;
            theHelpScreen.blnHelpScreen = true;
            theHelpScreen.repaint();
            theFrame.setContentPane(theHelpScreen);
        //Chat Field - Server Lobby
        } else if (e.getSource() == theServerLobby.theChatField) {
            if (theModel.blnIsHost) {
                theServerLobby.theChatArea.append(theMainScreen.theNameField.getText() + ": " + theServerLobby.theChatField.getText() + "\n");
                theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "1", theServerLobby.theChatField.getText(), null, null);
            } else {
                theModel.sendMessage(theModel.strUsername, "0", theModel.strRole, "1", theServerLobby.theChatField.getText(), null, null);
            }
            theServerLobby.theChatField.setText("");
        //Join Button - Join IP Screen
        } else if (e.getSource() == theIPScreen.theJoinButton) {
            if (theModel.initializeClient(theMainScreen.theNameField.getText(), theIPScreen.theIPField.getText()) && !theIPScreen.theIPField.getText().equals("")) {
                theServerLobby.theIPAdress.setText(theIPScreen.theIPField.getText());
                theFrame.setContentPane(theServerLobby);
            } else {
                theIPScreen.theErrorMessage.setText("Server Not Found");
            }
        //Role Buttons - Server Lobby
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
        //Start Button - Server Lobby
        } else if (e.getSource() == theServerLobby.theStartButton) {
            if (theModel.intRoleData[1] > 0 && theModel.intRoleData[2] > 0) {
                theModel.strChosenTheme = theThemeSelect.theThemeActions.getThemeData("Default");
                theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "3", null, null, null);
                theFrame.setContentPane(theThemeSelect);
            } else {
                theServerLobby.theChatArea.append("Server: Both Teams Must Have At Least One Player\n");
            }
        //Spectator Button - Server Lobby
        } else if (e.getSource() == theServerLobby.theSpectatorButton) {
            SwitchRoles("0");
        //Theme Select Button - Theme Select
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
                    theGameScreen.theChatArea.append("Server: Your turn to move\n");
                    theModel.blnIsMyTurn = true;
                    Assets.imgDragged = Assets.imgRed;
                }
                default -> {
                    theGameScreen.theTeam.setText("Team Black");
                    Assets.imgDragged = Assets.imgBlack;
                }
            }
            theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "4", theModel.ArrayToString1(theModel.strChosenTheme), null, null);
        //Chat Field - Game Screen
        } else if(e.getSource() == theGameScreen.theChatField){
            if (theModel.blnIsHost) {
                theGameScreen.theChatArea.append(theModel.strUsername + ": " + theGameScreen.theChatField.getText() + "\n");
                theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "7", theGameScreen.theChatField.getText(), null, null);
            } else {
                theModel.sendMessage(theModel.strUsername, "0", theModel.strRole, "4", theGameScreen.theChatField.getText(), null, null);
            }
            theGameScreen.theChatField.setText("");
        //Chat Field - Help Screen
        } else if(e.getSource() == theHelpScreen.theChatField){
            theHelpScreen.theChatArea.append("Player: " + theHelpScreen.theChatField.getText() + "\n");
            theHelpScreen.theChatField.setText("");
        //Socket Listener
        }else if (e.getSource() == theModel.theSocket) {
            //Gets the message from the socket
            theModel.receiveMessage(theModel.theSocket.readText());

            //Intended for Host
            if (theModel.strMessage[1].equals("0") && theModel.blnIsHost) {
                //Action 0: Client Connected
                if (theModel.strMessage[3].equals("0")) {
                    //Updates the variables
                    theModel.intPlayersConnected++;
                    theModel.strPlayerList[theModel.intPlayersConnected - 1] = theModel.strMessage[0];
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
                    theModel.sendMessage(theModel.strMessage[0], "1", theModel.strMessage[4], "2", theModel.ArrayToString1(theModel.intRoleData), null, null);
                }
                //Action 3: Client Moved
                if (theModel.strMessage[3].equals("3")) {
                    theModel.strBoard = theModel.StringToArray2(theModel.strMessage[4]);
                    theGameScreen.strBoard = theModel.strBoard;
                    theGameScreen.repaint();
                    if (!theModel.strRole.equals("0")) {
                        theGameScreen.theChatArea.append("Server: " + theModel.strMessage[0] + " has moved\n");
                        theGameScreen.theChatArea.append("Server: Your turn to move\n");
                        theModel.blnIsMyTurn = true;
                    }
                    theModel.intRedPieces = Integer.parseInt(theModel.strMessage[5]);
                    theModel.intBlackPieces = Integer.parseInt(theModel.strMessage[6]);
                    theGameScreen.theRedPiecesLeft.setText("Red Pieces Left: " + theModel.intRedPieces);
                    theGameScreen.theBlackPiecesLeft.setText("Black Pieces Left: " + theModel.intBlackPieces);
                    theModel.sendMessage(theModel.strMessage[0], "1", theModel.strRole, "6", theModel.ArrayToString2(theModel.strBoard), String.valueOf(theModel.intRedPieces), String.valueOf(theModel.intBlackPieces));
                }
                //Action 4: Client Chat
                if(theModel.strMessage[3].equals("4")){
                    theGameScreen.theChatArea.append(theModel.strMessage[0] + ": " + theModel.strMessage[4] + "\n");
                    theModel.sendMessage(theModel.strMessage[0], "1", theModel.strRole, "7", theModel.strMessage[4], null, null);
                }
                //Action 5: Game Over
                if(theModel.strMessage[3].equals("5")){
                    System.out.println("GAME OVER: HOST");
                    if(theModel.strMessage[4].equals("1")){
                        theGameOver.theMessage.setText("Red Wins!");
                    }else if(theModel.strMessage[4].equals("2")){
                        theGameOver.theMessage.setText("Black Wins!");
                    }
                    theFrame.setContentPane(theGameOver);
                    theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "8", theModel.strMessage[4], null, null);
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
                            theGameScreen.theChatArea.append("Server: Your turn to move\n");
                            theModel.blnIsMyTurn = true;
                            Assets.imgDragged = Assets.imgRed;
                        }
                        default -> {
                            theGameScreen.theTeam.setText("Team Black");
                            Assets.imgDragged = Assets.imgBlack;
                        }
                    }
                }
                //Action 5: Host Moved
                if (theModel.strMessage[3].equals("5")) {
                    theModel.strBoard = theModel.StringToArray2(theModel.strMessage[4]);
                    theGameScreen.strBoard = theModel.strBoard;
                    theGameScreen.repaint();
                    if (!theModel.strRole.equals("0")) {
                        theGameScreen.theChatArea.append("Server: " + theModel.strMessage[0] + " has moved\n");
                        theGameScreen.theChatArea.append("Server: Your turn to move\n");
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
                        theGameScreen.theChatArea.append("Server: " + theModel.strMessage[0] + " has moved\n");
                        theGameScreen.theChatArea.append("Server: Your turn to move\n");
                        theModel.blnIsMyTurn = true;
                    }
                    theModel.intRedPieces = Integer.parseInt(theModel.strMessage[5]);
                    theModel.intBlackPieces = Integer.parseInt(theModel.strMessage[6]);
                    theGameScreen.theRedPiecesLeft.setText("Red Pieces Left: " + theModel.intRedPieces);
                    theGameScreen.theBlackPiecesLeft.setText("Black Pieces Left: " + theModel.intBlackPieces);
                }
                //Action 7: Client Chat
                if(theModel.strMessage[3].equals("7")){
                    theGameScreen.theChatArea.append(theModel.strMessage[0] + ": " + theModel.strMessage[4] + "\n");
                }
                //Action 8: Game Over
                if(theModel.strMessage[3].equals("8")){
                    System.out.println("GAME OVER: CLIENT");
                    if(theModel.strMessage[4].equals("1")){
                        theGameOver.theMessage.setText("Red Wins!");
                    }else if(theModel.strMessage[4].equals("2")){
                        theGameOver.theMessage.setText("Black Wins!");
                    }
                    theFrame.setContentPane(theGameOver);
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

        //Repacks the frame
        theFrame.pack();
    }

    /**
     * Mouse Dragged event
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        //Updates the piece being dragged
        if (theModel.blnPieceSelected && theFrame.getContentPane() == theGameScreen) {
            theGameScreen.intMouseX = e.getX();
            theGameScreen.intMouseY = e.getY();
            theGameScreen.repaint();
        }else if(theModel.blnPieceSelected && theFrame.getContentPane() == theHelpScreen){
            theHelpScreen.intMouseX = e.getX();
            theHelpScreen.intMouseY = e.getY();
            theHelpScreen.repaint();
        }
    }

    /**
     * Mouse Pressed event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() > 120 && e.getX() < 840 && theFrame.getContentPane() == theGameScreen && (theModel.strRole.equals("1") || theModel.strRole.equals("2")) && theModel.blnIsMyTurn) {
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
        } else if (e.getX() > 120 && e.getX() < 840 && theFrame.getContentPane() == theHelpScreen) {
            theModel.intCurrentCol = (int) (e.getY() / 90);
            theModel.intCurrentRow = (int) ((e.getX() - 120) / 90);

            if(!theModel.strBoard[theModel.intCurrentCol][theModel.intCurrentRow].equals(" ")){
                if(theModel.strBoard[theModel.intCurrentCol][theModel.intCurrentRow].equals("1")){
                    theModel.strPieceGrabbed = "1";
                    Assets.imgDragged = Assets.imgRed;
                }else if(theModel.strBoard[theModel.intCurrentCol][theModel.intCurrentRow].equals("2")){
                    theModel.strPieceGrabbed = "2";
                    Assets.imgDragged = Assets.imgBlack;
                }
                
                theModel.blnPieceSelected = true;
                theModel.strBoard[theModel.intCurrentCol][theModel.intCurrentRow] = " ";
                theHelpScreen.strBoard = theModel.strBoard;

                //Draws the piece being dragged
                theHelpScreen.intMouseX = e.getX();
                theHelpScreen.intMouseY = e.getY();

                //Repaints the screens
                theHelpScreen.repaint();
            }
        }
    }
    /**
     * Mouse Released event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        //Puts the piece back on the board - Game Screen
        if (theModel.blnPieceSelected && theFrame.getContentPane() == theGameScreen) {
            //Puts the piece being dragged back in the corner
            theGameScreen.intMouseX = 1325;
            theGameScreen.intMouseY = 765;
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

                //If its a draw, the other team wins
                if(!theModel.movesAvailable()){
                    if(theModel.strRole.equals("1")){
                        theGameOver.theMessage.setText("Black Wins!");
                        if(theModel.blnIsHost){
                            theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "8", "2", null, null);
                        }else{
                            theModel.sendMessage(theModel.strUsername, "0", theModel.strRole, "5", "2", null, null);
                        }
                    }else if(theModel.strRole.equals("2")){
                        theGameOver.theMessage.setText("Red Wins!");
                        if(theModel.blnIsHost){
                            theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "8", "1", null, null);
                        }else{
                            theModel.sendMessage(theModel.strUsername, "0", theModel.strRole, "5", "1", null, null);
                        }
                    }
                    theFrame.setContentPane(theGameOver);
                }

                //If black is 0, red wins
                if(theModel.intBlackPieces == 0){
                    theGameOver.theMessage.setText("Red Wins!");
                    theFrame.setContentPane(theGameOver);
                    if(theModel.blnIsHost){
                        theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "8", "1", null, null);
                    }else{
                        theModel.sendMessage(theModel.strUsername, "0", theModel.strRole, "5", "1", null, null);
                    }
                }

                //If red is 0, black wins
                if(theModel.intRedPieces == 0){
                    theGameOver.theMessage.setText("Black Wins!");
                    theFrame.setContentPane(theGameOver);
                    if(theModel.blnIsHost){
                        theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "8", "2", null, null);
                    }else{
                        theModel.sendMessage(theModel.strUsername, "0", theModel.strRole, "5", "2", null, null);
                    }
                }

                //Packs the frame
                theFrame.pack();
                
                //If the move is valid, the board is updated
                if (!theModel.blnJumpAvailable) {
                    theGameScreen.theBlackPiecesLeft.setText("Black Pieces Left: " + theModel.intBlackPieces);
                    theGameScreen.theRedPiecesLeft.setText("Red Pieces Left: " + theModel.intRedPieces);

                    theModel.blnIsMyTurn = false;
                    if (theModel.blnIsHost) {
                        theModel.sendMessage(theModel.strUsername, "1", theModel.strRole, "5", theModel.ArrayToString2(theModel.strBoard), String.valueOf(theModel.intRedPieces), String.valueOf(theModel.intBlackPieces));
                    } else {
                        theModel.sendMessage(theModel.strUsername, "0", theModel.strRole, "3", theModel.ArrayToString2(theModel.strBoard), String.valueOf(theModel.intRedPieces), String.valueOf(theModel.intBlackPieces));
                    }
                }
            }
            theModel.blnPieceSelected = false;
        //Help Screen - Mouse Released
        } else if (theModel.blnPieceSelected && theFrame.getContentPane() == theHelpScreen) {
            theHelpScreen.intMouseX = 1325;
            theHelpScreen.intMouseY = 765;

            theModel.intRequestedCol = (int) (e.getY() / 90);
            theModel.intRequestedRow = (int) ((e.getX() - 120) / 90);

            if (!theModel.validateMoveHelpScreen()) {
                theModel.strBoard[theModel.intCurrentCol][theModel.intCurrentRow] = theModel.strPieceGrabbed;
                theHelpScreen.strBoard = theModel.strBoard;
                theHelpScreen.repaint();
                System.out.println("Invalid Move");
            } else {
                theModel.strBoard[theModel.intRequestedCol][theModel.intRequestedRow] = theModel.strPieceGrabbed;
                System.out.println(theModel.strPieceGrabbed);
                System.out.println(theModel.strBoard[theModel.intRequestedCol][theModel.intRequestedRow] + " " + theModel.intRequestedCol + " " + theModel.intRequestedRow);
                theHelpScreen.strBoard = theModel.strBoard;
                theHelpScreen.repaint();
                if (!theModel.blnJumpAvailable) {
                    theModel.blnIsMyTurn = false;
                }
            }
            theHelpScreen.theRedPiecesLeft.setText("Red Pieces Left: " + theModel.intRedPieces);
            theHelpScreen.theBlackPiecesLeft.setText("Black Pieces Left: " + theModel.intBlackPieces);
            theModel.blnPieceSelected = false;
        }
    }

    //Unused Mouse Listeners - Mandatory overrides
    /** Mouse Entered event */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /** Mouse Exited event */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /** Mouse Moved event */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /** Mouse Clicked event */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Updates the roles of the players
     * @param strUsername Username of the player
     * @param strOldRole Old role of the player
     * @param strNewRole New role of the player
     */
    public void UpdateRoles(String strUsername, String strOldRole, String strNewRole) {
        //Subtracts from previous role and adds to new role
        theModel.intRoleData[Integer.parseInt(strOldRole)]--;
        theModel.intRoleData[Integer.parseInt(strNewRole)]++;

        //Updates the Panel
        theServerLobby.theSpectatorPlayers.setText("Spectators: " + theModel.intRoleData[0]);
        theServerLobby.theRedPlayers.setText("Red Players: " + theModel.intRoleData[1]);
        theServerLobby.theBlackPlayers.setText("Black Players: " + theModel.intRoleData[2]);
    }

    /**
     * Switches the roles of the players
     * @param strNewRole New role of the player
     */
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

    /**
     * Updates the chat
     * @param strUsername Username of the player
     * @param strRole Role of the player
     */
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

    /**
     * Constructs a new instance of the View class.
     */
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

        //Game Screen Listeners
        theGameScreen.theChatField.addActionListener(this);

        //Help Screen Listeners
        theHelpScreen.theChatField.addActionListener(this);

        //Frame Setup
        theFrame.setVisible(true);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setContentPane(theMainScreen);
        theFrame.pack();
    }
    
    /**
     * Main method to run the game
     * @param args terminal arguments
     */
    public static void main(String[] args) {
        new View();
    }
}
