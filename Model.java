/*
 * Port 6000 - Main Port
 * 
 * Format: Designation#, Action#, IP, Board[]
 *   Designation#: 0 - Host, 1 - Client
*/

public class Model {
    View theView;

    //Shared Properties
    boolean blnConnected = false;
    boolean blnIsHost;
    boolean blnGameStarted = false; 
    String strPlayerList[][] = new String[2][2];
    String strUsername;

    //Host Properties
    SuperSocketMaster HostSocket;
    Thread BroadcastIP = new Thread(new BroadcastIP(this));

    //Client Properties
    SuperSocketMaster ClientSocket;

    public boolean initializeHost(String strName){
        if(!strName.equals("")){
            blnIsHost = true;
            strUsername = strName;
            HostSocket = new SuperSocketMaster(6000, theView);
            blnConnected = HostSocket.connect();
            if(blnConnected){
                System.out.println("Host Connected");
                BroadcastIP.start();
                strPlayerList[0][0] = HostSocket.getMyAddress();
                strPlayerList[0][1] = strName;
                return true;
            }
        }
        return false;
    }

    public boolean initializeClient(String strName, String strIP){
        blnIsHost = false;
        strUsername = strName;
        ClientSocket = new SuperSocketMaster(strIP, 6000, theView);
        blnConnected = ClientSocket.connect();
        if(blnConnected){
            System.out.println("Client Connected");
            strPlayerList[1][0] = ClientSocket.getMyAddress();
            strPlayerList[1][1] = strName;
            return true;
        }
        return false;
    }

    public String getStatus(){
        return HostSocket.getMyAddress()+","+strUsername+","+strPlayerList.length;
    }

    public Model(View theView){
        this.theView = theView;
    }
}
