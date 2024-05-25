
/*
 * Port 6000 - Main Port
 * 
 * Format: Name, Designation#, Role#, Action#, param1, param2
 *   Name: Username
 *   Designation#: 0 - Host, 1 - Client
 *   Role#: 0 - Spectator, 1 - Red, 2 - Black
 *   Action#: 0 - Client Joined, 1 - Server Chat Message
 */
public class Model {

    View theView;

    //Shared Properties
    boolean blnConnected = false;
    boolean blnIsHost;
    boolean blnGameStarted = false;
    String strPlayerList[][] = new String[5][2];
    String strMessage[];
    String strUsername;
    SuperSocketMaster theSocket;
    int intPlayersConnected = 0;

    //Host Properties
    //Client Properties
    String strNewClient[] = new String[2];

    public boolean initializeHost(String strName) {
        if (!strName.equals("")) {
            blnIsHost = true;
            strUsername = strName;
            theSocket = new SuperSocketMaster(6000, theView);
            blnConnected = theSocket.connect();
            if (blnConnected) {
                System.out.println("Host Connected");
                strPlayerList[0][0] = strName;
                strPlayerList[0][1] = "0";
                return true;
            }
        }
        return false;
    }

    public boolean initializeClient(String strName, String strIP) {
        if (!strName.equals("")) {
            blnIsHost = false;
            strUsername = strName;
            intPlayersConnected = 1;
            theSocket = new SuperSocketMaster(strIP, 6000, theView);
            blnConnected = theSocket.connect();
            if (blnConnected) {
                System.out.println("Client Connected");
                strNewClient[0] = strName;
                strNewClient[1] = "0";
                sendMessage(strName, "0", "", "0", ArrayToString1(strNewClient), null);
                return true;
            }
        }
        return false;
    }

    public void sendMessage(String strUsername, String strDesignationID, String strRoleID, String strActionID, String strParam1, String strParam2) {
        theSocket.sendText(strUsername + ";;" + strDesignationID + ";;" + strRoleID + ";;" + strActionID + ";;" + strParam1 + ";;" + strParam2);
    }

    public void receiveMessage(String strIncomingMessage) {
        strMessage = strIncomingMessage.split(";;");
    }

    public String ArrayToString1(String[] strArray) {
        String strReturn = "";
        for (String strArray1 : strArray) {
            strReturn += strArray1 + ",";
        }
        return strReturn;
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

    public String[] StringToArray1(String strArray) {
        return strArray.split(",");
    }

    public String[][] StringToArray2(String strArray) {
        String[] strTempArray = strArray.split(";");
        String[][] strReturn = new String[strTempArray.length][strTempArray[0].split(",").length];
        for (int i = 0; i < strTempArray.length; i++) {
            strReturn[i] = strTempArray[i].split(",");
        }
        return strReturn;
    }

    public String getStatus() {
        return theSocket.getMyAddress() + "," + strUsername + "," + strPlayerList.length;
    }

    public Model(View theView) {
        this.theView = theView;
    }
}
