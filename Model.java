import Panels.*;

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

    View theView;
    Assets programAssets = new Assets();

    //Shared Properties
    boolean blnConnected = false;
    boolean blnIsHost;
    String strPlayerList[] = new String[5];
    String strRole;
    String strMessage[];
    String strUsername;
    int intPlayersConnected = 0;
    String strBoard[][] = new String[8][8];
    SuperSocketMaster theSocket;
    int intCurrentRow = 0;
    int intRequestedRow = 0;
    int intCurrentCol = 0;
    int intRequestedCol = 0;
    boolean blnPieceSelected = false;

    //Host Properties
    int intRoleData[] = new int[3]; //0 - Spectator, 1 - Red, 2 - Black
    String[] strChosenTheme = new String[4];

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

    public String ArrayToString1(int[] intArray){
        String strArray[] = new String[intArray.length];
        for(int intLoop = 0; intLoop < intArray.length; intLoop++){
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

    public int[] StringToIntArray1(String strArray){
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

    public void loadBoard(){
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
    
    public void loadImages(){
        Assets.imgBoard = programAssets.loadImage("Assets/Themes/" + this.strChosenTheme[1]);
        Assets.imgRed = programAssets.loadImage("Assets/Themes/" + this.strChosenTheme[2]);
        Assets.imgBlack = programAssets.loadImage("Assets/Themes/" + this.strChosenTheme[3]);
    }

    public boolean validateMove(){
        if(intRequestedRow < 0 || intRequestedRow > 7 || intRequestedCol < 0 || intRequestedCol > 7){
            return false;
        }else if(!strBoard[intRequestedCol][intRequestedRow].equals(" ")){
            return false;
        }else if(intRequestedCol % 2 == 0 && intRequestedRow % 2 == 1){
            return false;
        }else if(intRequestedCol % 2 == 1 && intRequestedRow % 2 == 0){
            return false;
        }else{
            return true;
        }
    }
    
    public Model(View theView) {
        this.theView = theView;
    }
}
