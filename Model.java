//Import required libraries
import Panels.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/*
 * NOTE: null is used as a placeholder for empty parameters
 * 
 * Port 6000 - Main Port
 * 
 * Format: Name, Designation#, Role#, Action#, param1, param2, param3
 *   Name: Username
 *   Designation#: 0 - Host, 1 - Client
 *   Role#: 0 - Spectator, 1 - Red, 2 - Black
 *   Action#: 0 - Client Joined, 1 - Server Chat Message
 * 
 * Possible Client Sent Messages:
 * strUsername, 0, null, 0, strNewClient[], null, null (Client Joined - Message sent to host)
 * 
 * Possible Host Sent Messages:
 * strUsername, 1, null, 0, strPlayerList[][], intPlayersConnected, intRoleDate[] (Client Joined - Message returned to all clients)

/**
 * The Model class represents the model component of the application. It contains the properties, arrays, and methods
 * necessary for managing the game state and communication with the network. This class handles initializing the host or
 * client, sending and receiving messages over the network, converting arrays to strings and vice versa, loading the game
 * board and theme images, and validating moves in multiplayer and help mode.
 */
public class Model {

    //Properties from other files
    View theView;
    Assets programAssets = new Assets();
    SuperSocketMaster theSocket;

    //Boolean variables
    boolean blnConnected = false;
    boolean blnIsHost;
    boolean blnPieceSelected = false;
    boolean blnIsMyTurn = false;
    boolean blnJumpAvailable = false;
    boolean blnValidMove = false;

    //Arrays
    String strPlayerList[] = new String[5];
    String strMessage[];
    String strBoard[][] = new String[8][8];
    String[] strChosenTheme = new String[4];

    //Strings
    String strRole;
    String strUsername;
    String strPieceGrabbed;

    //Integers
    int intPlayersConnected = 0;
    int intCurrentRow = 0;
    int intRequestedRow = 0;
    int intCurrentCol = 0;
    int intRequestedCol = 0;
    int intRedPieces = 12;
    int intBlackPieces = 12;
    int intRoleData[] = new int[3]; //0 - Spectator, 1 - Red, 2 - Black

    /**
     * Initializes the host with the specified username.
     * @param strName Username of the host
     * @return Indicates whether the host was successfully initialized
     */
    public boolean initializeHost(String strName) {
        if (!strName.equals("")) {
            intRoleData[0] = 1;
            intRoleData[1] = 0;
            intRoleData[2] = 0;
            blnIsHost = true;
            strUsername = strName;
            theSocket = new SuperSocketMaster(6000, theView);
            blnConnected = theSocket.connect();
            if (blnConnected) {
                intPlayersConnected = 1;
                System.out.println("Host Connected");
                strPlayerList[0] = strName;
                strRole = "0";
                return true;
            }
        }
        return false;
    }

    /**
     * Initializes the client with the specified username and IP address.
     * @param strName Username of the client
     * @param strIP IP address of the host
     * @return Indicates whether the client was successfully initialized
     */
    public boolean initializeClient(String strName, String strIP) {
        blnIsHost = false;
        strUsername = strName;
        theSocket = new SuperSocketMaster(strIP, 6000, theView);
        blnConnected = theSocket.connect();
        if (blnConnected) {
            System.out.println("Client Connected");
            strRole = "0";
            sendMessage(strName, "0", "0", "0", strName, null, null);
            return true;
        }
        return false;
    }
    
    /**
     * Sends a message to the network with the specified parameters.
     * @param strUsername Username of the sender
     * @param strDesignationID Designation number of the recipient (0 - Host, 1 - Client)
     * @param strRoleID Role number of the sender (0 - Spectator, 1 - Red, 2 - Black)
     * @param strActionID Action number for recipient to perform
     * @param strParam1 First parameter of the message
     * @param strParam2 Second parameter of the message
     * @param strParam3 Third parameter of the message
     */
    public void sendMessage(String strUsername, String strDesignationID, String strRoleID, String strActionID, String strParam1, String strParam2, String strParam3) {
        theSocket.sendText(strUsername + ":" + strDesignationID + ":" + strRoleID + ":" + strActionID + ":" + strParam1 + ":" + strParam2 + ":" + strParam3);
    }

    /**
     * Receives a message from the network and splits it into an array.
     * @param strIncomingMessage Message received from the network
     */
    public void receiveMessage(String strIncomingMessage) {
        strMessage = strIncomingMessage.split(":");
    }

    /**
     * Converts a 1D string array into a string.
     * @param strArray Array to be converted
     * @return String representation of the array
     */
    public String ArrayToString1(String[] strArray) {
        String strReturn = "";
        for (String strArray1 : strArray) {
            strReturn += strArray1 + ",";
        }
        return strReturn;
    }

    /**
     * Converts a 1D integer array into a string.
     * @param intArray Array to be converted
     * @return String representation of the array
     */
    public String ArrayToString1(int[] intArray) {
        String strArray[] = new String[intArray.length];
        for (int intLoop = 0; intLoop < intArray.length; intLoop++) {
            strArray[intLoop] = String.valueOf(intArray[intLoop]);
        }
        return ArrayToString1(strArray);
    }
    
    /**
     * Converts a 2D string array into a string.
     * @param strArray Array to be converted
     * @return String representation of the array
     */
    public String ArrayToString2(String[][] strArray) {
        String strReturn = "";
        for (String[] strArray1 : strArray) {
            for (String item : strArray1) {
                strReturn += item + ",";
            }
            strReturn += ";";
        }
        return strReturn;
    }

    /**
     * Converts a string into a 1D string array.
     * @param strArray String to be converted
     * @return 1D string array representation of the string
     */
    public String[] StringToStrArray1(String strArray) {
        return strArray.split(",");
    }

    /**
     * Converts a string into a 1D integer array.
     * @param strArray String to be converted
     * @return 1D integer array representation of the string
     */
    public int[] StringToIntArray1(String strArray) {
        String strTempArray[] = strArray.split(",");
        int intReturn[] = new int[strTempArray.length];
        for (int intLoop = 0; intLoop < strTempArray.length; intLoop++) {
            intReturn[intLoop] = Integer.parseInt(strTempArray[intLoop]);
        }
        return intReturn;
    }

    /**
     * Converts a string into a 2D string array.
     * @param strArray String to be converted
     * @return 2D string array representation of the string
     */
    public String[][] StringToArray2(String strArray) {
        String[] strTempArray = strArray.split(";");
        String[][] strReturn = new String[strTempArray.length][strTempArray[0].split(",").length];
        for (int i = 0; i < strTempArray.length; i++) {
            strReturn[i] = strTempArray[i].split(",");
        }
        return strReturn;
    }

    /**
     * Loads the game board from a CSV file into a 2D string array.
     */
    public void loadBoard() {
        BufferedReader theBufferedReader;
        FileReader theFileReader;

        //Tries to load the file
        try {
            theFileReader = new FileReader("Maps/board.csv");
            theBufferedReader = new BufferedReader(theFileReader);
            String strLine = theBufferedReader.readLine();
            int intOuterLoop = 0;
            while (strLine != null) {
                String[] strTempArray = strLine.split(",");
                for (int intInnerLoop = 0; intInnerLoop < strTempArray.length; intInnerLoop++) {
                    strBoard[intOuterLoop][intInnerLoop] = strTempArray[intInnerLoop];
                }
                strLine = theBufferedReader.readLine();
                intOuterLoop++;
            }
            theBufferedReader.close();
            theFileReader.close();
        
        //Catches an IO exception
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * Loads the chosen theme images into the program.
     */
    public void loadImages() {
        Assets.imgBoard = programAssets.loadImage("Assets/Themes/" + this.strChosenTheme[1]);
        Assets.imgRed = programAssets.loadImage("Assets/Themes/" + this.strChosenTheme[2]);
        Assets.imgBlack = programAssets.loadImage("Assets/Themes/" + this.strChosenTheme[3]);

        if (strRole.equals("2")) {
            Assets.imgBoard = rotate(Assets.imgBoard);
        }
    }

    /**
     * Validates a move in multiplayer mode and returns a boolean.
     * @return Indicates whether the move is valid
     */
    public boolean validateMove() {
        //Checks if the move is inside the board
        if (intRequestedRow < 0 || intRequestedRow > 7 || intRequestedCol < 0 || intRequestedCol > 7) {
            return false;
        
        //Checks if the move is on a piece
        } else if (!strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
            return false;

        //Checks if the move is on an empty space
        } else if (strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
            System.out.println(strRole + ": " + intCurrentCol + " " + intCurrentRow + " " + intRequestedCol + " " + intRequestedRow);
            
            //Checks for player 1
            if (strRole.equals("1")) {
                //Checks if the move is on a white space
                if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                    return false;
                
                //Checks if the move is a jump in one direction
                } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol - 1][intCurrentRow + 1].equals("2")) {
                    strBoard[intCurrentCol - 1][intCurrentRow + 1] = " ";
                    intBlackPieces--;
                    blnJumpAvailable = jumpAvailable();
                    System.out.println(blnJumpAvailable);
                    return true;
                
                //Checks if the move is a jump in the other direction
                } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol - 1][intCurrentRow - 1].equals("2")) {
                    strBoard[intCurrentCol - 1][intCurrentRow - 1] = " ";
                    intBlackPieces--;
                    blnJumpAvailable = jumpAvailable();
                    System.out.println(blnJumpAvailable);
                    return true;
                
                //Checks if the move is a normal move
                } else if (intRequestedCol != intCurrentCol - 1) {
                    return false;
                } else if ((intRequestedRow < intCurrentRow - 1 || intRequestedRow > intCurrentRow + 1)) {
                    return false;
                }
            
            //Checks for player 2
            } else if (strRole.equals("2")) {
                //Checks if the move is on a white space
                if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                    return false;
                
                //Checks if the move is a jump in one direction
                } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol + 1][intCurrentRow + 1].equals("1")) {
                    strBoard[intCurrentCol + 1][intCurrentRow + 1] = " ";
                    intRedPieces--;
                    blnJumpAvailable = jumpAvailable();
                    return true;
                
                //Checks if the move is a jump in the other direction
                } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol + 1][intCurrentRow - 1].equals("1")) {
                    strBoard[intCurrentCol + 1][intCurrentRow - 1] = " ";
                    intRedPieces--;
                    blnJumpAvailable = jumpAvailable();
                    return true;
                
                //Checks if the move is a normal move
                } else if (intRequestedCol != intCurrentCol + 1) {
                    return false;
                } else if ((intRequestedRow < intCurrentRow - 1 || intRequestedRow > intCurrentRow + 1)) {
                    return false;
                }
            }
        }
        //If the move is valid, return true
        return true;
    }

    /**
     * Checks if the move is available for the help screen.
     * @return Indicates whether a jump is available
    */
    public boolean validateMoveHelpScreen() {
        //Checks for player 1
        if(strPieceGrabbed.equals("1")){
            //Checks if the move is inside the board
            if (intRequestedRow < 0 || intRequestedRow > 7 || intRequestedCol < 0 || intRequestedCol > 7) {
                return false;
            
            //Checks if the move is on a piece
            } else if (!strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
                return false;
            
            //Checks if the move is on an empty space
            } else if (strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
                //Checks if the move is on a white space
                if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                    return false;
                
                //Checks if the move is a jump in one direction
                } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol - 1][intCurrentRow + 1].equals("2")) {
                    strBoard[intCurrentCol - 1][intCurrentRow + 1] = " ";
                    intBlackPieces--;
                    return true;
                
                //Checks if the move is a jump in the other direction
                } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol - 1][intCurrentRow - 1].equals("2")) {
                    strBoard[intCurrentCol - 1][intCurrentRow - 1] = " ";
                    intBlackPieces--;
                    return true;
                
                //Checks if the move is a normal move
                } else if (intRequestedCol != intCurrentCol - 1) {
                    return false;
                } else if ((intRequestedRow < intCurrentRow - 1 || intRequestedRow > intCurrentRow + 1)) {
                    return false;
                }
            }
        //Checks for player 2
        }else{
            //Checks if the move is inside the board
            if (intRequestedRow < 0 || intRequestedRow > 7 || intRequestedCol < 0 || intRequestedCol > 7) {
                return false;
            
            //Checks if the move is on a piece
            } else if (!strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
                return false;
            
            //Checks if the move is on an empty space
            } else if (strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
                //Checks if the move is on a white space
                if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                    return false;
                //Checks if the move is a jump in one direction
                } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol + 1][intCurrentRow + 1].equals("1")) {
                    strBoard[intCurrentCol + 1][intCurrentRow + 1] = " ";
                    intRedPieces--;
                    return true;
                //Checks if the move is a jump in the other direction
                } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol + 1][intCurrentRow - 1].equals("1")) {
                    strBoard[intCurrentCol + 1][intCurrentRow - 1] = " ";
                    intRedPieces--;
                    return true;
                //Checks if the move is a normal move
                } else if (intRequestedCol != intCurrentCol + 1) {
                    return false;
                } else if ((intRequestedRow < intCurrentRow - 1 || intRequestedRow > intCurrentRow + 1)) {
                    return false;
                } 
            }
        }
        //If the move is valid, return true
        return true;
    }

    /**
     * Checks if a jump is available.
     * @return Indicates whether a jump is available
     */
    public boolean jumpAvailable() {      
        //Updates player positions  
        intCurrentCol = intRequestedCol;
        intCurrentRow = intRequestedRow;

        //Debugging statements
        System.out.println(intCurrentCol + ", " + intCurrentRow);
        System.out.println("Current Board:");
        for (int i = 0; i < strBoard.length; i++) {
            for (int j = 0; j < strBoard[i].length; j++) {
                System.out.print(strBoard[i][j] + "-");
            }
            System.out.println();
        }

        //Checks for player 1
        if (strRole.equals("1")) {
            //Checks for jumps in one direction
            if (intCurrentRow < 6 && intCurrentCol > 1) {
                if (strBoard[intCurrentCol - 2][intCurrentRow + 2].equals(" ") && strBoard[intCurrentCol - 1][intCurrentRow + 1].equals("2")) {
                    return true;
                }
            }

            //Checks for jumps in the other direction
            if (intCurrentRow > 1 && intCurrentCol > 1) {
                if (strBoard[intCurrentCol - 2][intCurrentRow - 2].equals(" ") && strBoard[intCurrentCol - 1][intCurrentRow - 1].equals("2")) {
                    return true;
                }
            }
        
        //Checks for player 2
        } else if (strRole.equals("2")) {
            //Checks for jumps in one direction
            if (intCurrentRow < 6 && intCurrentCol < 6) {
                System.out.println("2 UP: " + strBoard[intCurrentCol + 2][intCurrentRow + 2] + " 1 UP: " + strBoard[intCurrentCol + 1][intCurrentRow + 1]);
                if (strBoard[intCurrentCol + 2][intCurrentRow + 2].equals(" ") && strBoard[intCurrentCol + 1][intCurrentRow + 1].equals("1")) {
                    return true;
                }
            }

            //Checks for jumps in the other direction
            if (intCurrentRow > 1 && intCurrentCol < 6) {
                if (strBoard[intCurrentCol + 2][intCurrentRow - 2].equals(" ") && strBoard[intCurrentCol + 1][intCurrentRow - 1].equals("1")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Validates a jump move.
     * @return Indicates whether the jump is valid
     */
    public boolean validateJump() {
        //Checks for player 1
        if (strRole.equals("1")) {
            //Checks if the move is on a white space
            if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                return false;
            //Checks if the move is a jump in one direction
            } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol - 1][intCurrentRow + 1].equals("2")) {
                strBoard[intCurrentCol - 1][intCurrentRow + 1] = " ";
                intBlackPieces--;
                blnJumpAvailable = jumpAvailable();
                return true;
            //Checks if the move is a jump in the other direction
            } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol - 1][intCurrentRow - 1].equals("2")) {
                strBoard[intCurrentCol - 1][intCurrentRow - 1] = " ";
                intBlackPieces--;
                blnJumpAvailable = jumpAvailable();
                return true;
            }
        //Checks for player 2
        } else if (strRole.equals("2")) {
            //Checks if the move is on a white space
            if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                return false;
            //Checks if the move is a jump in one direction
            } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol + 1][intCurrentRow + 1].equals("1")) {
                strBoard[intCurrentCol + 1][intCurrentRow + 1] = " ";
                intRedPieces--;
                blnJumpAvailable = jumpAvailable();
                return true;
            //Checks if the move is a jump in the other direction
            } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol + 1][intCurrentRow - 1].equals("1")) {
                strBoard[intCurrentCol + 1][intCurrentRow - 1] = " ";
                intRedPieces--;
                blnJumpAvailable = jumpAvailable();
                return true;
            }
        }
        //If the move is invalid, returns false
        return false;
    }

    /**
     * Checks for any available moves
     * @return
     */
    public boolean movesAvailable(){
        //Checks for player 1
        if(strRole.equals("1")){
            //Checks the whole board for available moves
            for(int intRow = 0; intRow < 8; intRow++){
                for(int intCol = 0; intCol < 8; intCol++){
                    if(strBoard[intCol][intRow].equals("1")){
                        if(intRow < 6 && intCol > 1){
                            if(strBoard[intCol - 2][intRow + 2].equals(" ") && strBoard[intCol - 1][intRow + 1].equals("2")){
                                return true;
                            }
                        }
                        if(intRow > 1 && intCol > 1){
                            if(strBoard[intCol - 2][intRow - 2].equals(" ") && strBoard[intCol - 1][intRow - 1].equals("2")){
                                return true;
                            }
                        }
                        if(intRow < 7 && intCol > 0){
                            if(strBoard[intCol - 1][intRow + 1].equals(" ")){
                                return true;
                            }
                        }
                        if(intRow > 0 && intCol > 0){
                            if(strBoard[intCol - 1][intRow - 1].equals(" ")){
                                return true;
                            }
                        }
                    }
                }
            }
        //Checks for player 2
        }else if(strRole.equals("2")){
            //Checks the whole board for available moves
            for(int intRow = 0; intRow < 8; intRow++){
                for(int intCol = 0; intCol < 8; intCol++){
                    if(strBoard[intCol][intRow].equals("2")){
                        if(intRow < 6 && intCol < 6){
                            if(strBoard[intCol + 2][intRow + 2].equals(" ") && strBoard[intCol + 1][intRow + 1].equals("1")){
                                return true;
                            }
                        }
                        if(intRow > 1 && intCol < 6){
                            if(strBoard[intCol + 2][intRow - 2].equals(" ") && strBoard[intCol + 1][intRow - 1].equals("1")){
                                return true;
                            }
                        }
                        if(intRow < 7 && intCol < 7){
                            if(strBoard[intCol + 1][intRow + 1].equals(" ")){
                                return true;
                            }
                        }
                        if(intRow > 0 && intCol < 7){
                            if(strBoard[intCol + 1][intRow - 1].equals(" ")){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        //returns false if no moves are available
        return false;
    }

    /**
     * Rotates an image 90 degrees
     * @param img Image to be rotated
     * @return Rotated image
     */    
    public static BufferedImage rotate(BufferedImage img) {
        // Getting Dimensions of image
        int intWidth = img.getWidth();
        int intHeight = img.getHeight();

        // Creating a new buffered image
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        // Creates graphics
        Graphics2D g2 = newImage.createGraphics();

        //Rotates the images, sets new dimenstions
        g2.rotate(Math.toRadians(90), intWidth / 2, intHeight / 2);
        g2.drawImage(img, null, 0, 0);

        // Return rotated buffer image
        return newImage;
    }

    /**
     * Constructor for the Model class
     * @param theView View object
     */
    public Model(View theView) {
        this.theView = theView;
    }
}
