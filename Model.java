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
    String strPlayerList[];
    String strUsername;

    //Host Properties
    SuperSocketMaster HostSocket;
    Thread BroadcastIP = new Thread(new BroadcastIP(this));

    //Client Properties
    SuperSocketMaster ClientSocket;

    public void initializeHost(String strName){
        if(!strName.equals("")){
            blnIsHost = true;
            strUsername = strName;
            HostSocket = new SuperSocketMaster(6000, theView);
            blnConnected = HostSocket.connect();
            if(blnConnected){
                System.out.println("Host Connected");
                BroadcastIP.start();
            }
        }
    }

    public void initializeClient(String strName, String strIP){
        blnIsHost = false;
    }

    public String getStatus(){
        return HostSocket.getMyAddress()+","+strUsername+","+strPlayerList.length;
    }

    public Model(View theView){
        this.theView = theView;
    }
}
