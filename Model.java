
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

    public void sendMessage(String strUsername, String strDesignationID, String strRoleID, String strActionID, String strParam1, String strParam2, String strParam3) {
        theSocket.sendText(strUsername + ":" + strDesignationID + ":" + strRoleID + ":" + strActionID + ":" + strParam1 + ":" + strParam2 + ":" + strParam3);
    }

    public void receiveMessage(String strIncomingMessage) {
        strMessage = strIncomingMessage.split(":");
    }

    public String ArrayToString1(String[] strArray) {
        String strReturn = "";
        for (String strArray1 : strArray) {
            strReturn += strArray1 + ",";
        }
        return strReturn;
    }

    public String ArrayToString1(int[] intArray) {
        String strArray[] = new String[intArray.length];
        for (int intLoop = 0; intLoop < intArray.length; intLoop++) {
            strArray[intLoop] = String.valueOf(intArray[intLoop]);
        }
        return ArrayToString1(strArray);
    }

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

    public String[] StringToStrArray1(String strArray) {
        return strArray.split(",");
    }

    public int[] StringToIntArray1(String strArray) {
        String strTempArray[] = strArray.split(",");
        int intReturn[] = new int[strTempArray.length];
        for (int intLoop = 0; intLoop < strTempArray.length; intLoop++) {
            intReturn[intLoop] = Integer.parseInt(strTempArray[intLoop]);
        }
        return intReturn;
    }

    public String[][] StringToArray2(String strArray) {
        String[] strTempArray = strArray.split(";");
        String[][] strReturn = new String[strTempArray.length][strTempArray[0].split(",").length];
        for (int i = 0; i < strTempArray.length; i++) {
            strReturn[i] = strTempArray[i].split(",");
        }
        return strReturn;
    }

    public void loadBoard() {
        BufferedReader theBufferedReader;
        FileReader theFileReader;

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
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public void loadImages() {
        Assets.imgBoard = programAssets.loadImage("Assets/Themes/" + this.strChosenTheme[1]);
        Assets.imgRed = programAssets.loadImage("Assets/Themes/" + this.strChosenTheme[2]);
        Assets.imgBlack = programAssets.loadImage("Assets/Themes/" + this.strChosenTheme[3]);

        if (strRole.equals("2")) {
            Assets.imgBoard = rotate(Assets.imgBoard);
        }
    }

    public boolean validateMove() {
        if (intRequestedRow < 0 || intRequestedRow > 7 || intRequestedCol < 0 || intRequestedCol > 7) {
            return false;
        } else if (!strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
            return false;
        } else if (strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
            System.out.println(strRole + ": " + intCurrentCol + " " + intCurrentRow + " " + intRequestedCol + " " + intRequestedRow);
            if (strRole.equals("1")) {
                if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                    return false;
                } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol - 1][intCurrentRow + 1].equals("2")) {
                    strBoard[intCurrentCol - 1][intCurrentRow + 1] = " ";
                    intBlackPieces--;
                    blnJumpAvailable = jumpAvailable();
                    System.out.println(blnJumpAvailable);
                    return true;
                } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol - 1][intCurrentRow - 1].equals("2")) {
                    strBoard[intCurrentCol - 1][intCurrentRow - 1] = " ";
                    intBlackPieces--;
                    blnJumpAvailable = jumpAvailable();
                    System.out.println(blnJumpAvailable);
                    return true;
                } else if (intRequestedCol != intCurrentCol - 1) {
                    return false;
                } else if ((intRequestedRow < intCurrentRow - 1 || intRequestedRow > intCurrentRow + 1)) {
                    return false;
                }
            } else if (strRole.equals("2")) {
                if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                    return false;
                } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol + 1][intCurrentRow + 1].equals("1")) {
                    strBoard[intCurrentCol + 1][intCurrentRow + 1] = " ";
                    intRedPieces--;
                    blnJumpAvailable = jumpAvailable();
                    return true;
                } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol + 1][intCurrentRow - 1].equals("1")) {
                    strBoard[intCurrentCol + 1][intCurrentRow - 1] = " ";
                    intRedPieces--;
                    blnJumpAvailable = jumpAvailable();
                    return true;
                } else if (intRequestedCol != intCurrentCol + 1) {
                    return false;
                } else if ((intRequestedRow < intCurrentRow - 1 || intRequestedRow > intCurrentRow + 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateMoveHelpScreen() {
        if(strPieceGrabbed.equals("1")){
            if (intRequestedRow < 0 || intRequestedRow > 7 || intRequestedCol < 0 || intRequestedCol > 7) {
                return false;
            } else if (!strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
                return false;
            } else if (strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
                if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                    return false;
                } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol - 1][intCurrentRow + 1].equals("2")) {
                    strBoard[intCurrentCol - 1][intCurrentRow + 1] = " ";
                    intBlackPieces--;
                    return true;
                } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol - 1][intCurrentRow - 1].equals("2")) {
                    strBoard[intCurrentCol - 1][intCurrentRow - 1] = " ";
                    intBlackPieces--;
                    return true;
                } else if (intRequestedCol != intCurrentCol - 1) {
                    return false;
                } else if ((intRequestedRow < intCurrentRow - 1 || intRequestedRow > intCurrentRow + 1)) {
                    return false;
                }
            }
        }else{
            if (intRequestedRow < 0 || intRequestedRow > 7 || intRequestedCol < 0 || intRequestedCol > 7) {
                return false;
            } else if (!strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
                return false;
            } else if (strBoard[intRequestedCol][intRequestedRow].equals(" ")) {
                if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                    return false;
                } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol + 1][intCurrentRow + 1].equals("1")) {
                    strBoard[intCurrentCol + 1][intCurrentRow + 1] = " ";
                    intRedPieces--;
                    return true;
                } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol + 1][intCurrentRow - 1].equals("1")) {
                    strBoard[intCurrentCol + 1][intCurrentRow - 1] = " ";
                    intRedPieces--;
                    return true;
                } else if (intRequestedCol != intCurrentCol + 1) {
                    return false;
                } else if ((intRequestedRow < intCurrentRow - 1 || intRequestedRow > intCurrentRow + 1)) {
                    return false;
                } 
            }
        }
        return true;
    }

    public boolean jumpAvailable() {        
        intCurrentCol = intRequestedCol;
        intCurrentRow = intRequestedRow;
        System.out.println(intCurrentCol + ", " + intCurrentRow);
        System.out.println("Current Board:");
        for (int i = 0; i < strBoard.length; i++) {
            for (int j = 0; j < strBoard[i].length; j++) {
                System.out.print(strBoard[i][j] + "-");
            }
            System.out.println();
        }
        if (strRole.equals("1")) {
            if (intCurrentRow < 6 && intCurrentCol > 1) {
                if (strBoard[intCurrentCol - 2][intCurrentRow + 2].equals(" ") && strBoard[intCurrentCol - 1][intCurrentRow + 1].equals("2")) {
                    return true;
                }
            }
            if (intCurrentRow > 1 && intCurrentCol > 1) {
                if (strBoard[intCurrentCol - 2][intCurrentRow - 2].equals(" ") && strBoard[intCurrentCol - 1][intCurrentRow - 1].equals("2")) {
                    return true;
                }
            }
        } else if (strRole.equals("2")) {
            if (intCurrentRow < 6 && intCurrentCol < 6) {
                System.out.println("2 UP: " + strBoard[intCurrentCol + 2][intCurrentRow + 2] + " 1 UP: " + strBoard[intCurrentCol + 1][intCurrentRow + 1]);
                if (strBoard[intCurrentCol + 2][intCurrentRow + 2].equals(" ") && strBoard[intCurrentCol + 1][intCurrentRow + 1].equals("1")) {
                    return true;
                }
            }
            if (intCurrentRow > 1 && intCurrentCol < 6) {
                if (strBoard[intCurrentCol + 2][intCurrentRow - 2].equals(" ") && strBoard[intCurrentCol + 1][intCurrentRow - 1].equals("1")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean validateJump() {
        if (strRole.equals("1")) {
            if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                return false;
            } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol - 1][intCurrentRow + 1].equals("2")) {
                strBoard[intCurrentCol - 1][intCurrentRow + 1] = " ";
                intBlackPieces--;
                blnJumpAvailable = jumpAvailable();
                return true;
            } else if (intRequestedCol == intCurrentCol - 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol - 1][intCurrentRow - 1].equals("2")) {
                strBoard[intCurrentCol - 1][intCurrentRow - 1] = " ";
                intBlackPieces--;
                blnJumpAvailable = jumpAvailable();
                return true;
            }
        } else if (strRole.equals("2")) {
            if ((intRequestedCol % 2 == 0 && intRequestedRow % 2 == 0) || (intRequestedCol % 2 == 1 && intRequestedRow % 2 == 1)) {
                return false;
            } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow + 2 && strBoard[intCurrentCol + 1][intCurrentRow + 1].equals("1")) {
                strBoard[intCurrentCol + 1][intCurrentRow + 1] = " ";
                intRedPieces--;
                blnJumpAvailable = jumpAvailable();
                return true;
            } else if (intRequestedCol == intCurrentCol + 2 && intRequestedRow == intCurrentRow - 2 && strBoard[intCurrentCol + 1][intCurrentRow - 1].equals("1")) {
                strBoard[intCurrentCol + 1][intCurrentRow - 1] = " ";
                intRedPieces--;
                blnJumpAvailable = jumpAvailable();
                return true;
            }
        }
        return false;
    }

    public boolean movesAvailable(){
        if(strRole.equals("1")){
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
        }else if(strRole.equals("2")){
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
        return false;
    }

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

    public Model(View theView) {
        this.theView = theView;
    }
}
