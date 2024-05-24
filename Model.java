import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Port 6000 - Main Port
 * 
 * Format: Designation#, Role#, Action#, IP, Board[]
 *   Designation#: 0 - Host, 1 - Client
 *   Role#: 0 - Red, 1 - Black, 2 - Spectator
*/

public class Model implements ActionListener {
    View theView;

    //Shared Properties
    boolean blnConnected = false;
    boolean blnIsHost;
    boolean blnIsSpectator;
    boolean blnGameStarted = false; 
    String strPlayerList[][] = new String[3][5];
    String[] strServerLoad;
    String strUsername;

    //Host Properties
    SuperSocketMaster HostSocket;

    //Client Properties
    SuperSocketMaster ClientSocket;

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == HostSocket){
            System.out.println("Host Socket Data Recieved");
        }else if(e.getSource() == ClientSocket){
            System.out.println("Client Socket Data Recieved");
        }
    }

    public boolean initializeHost(String strName){
        if(!strName.equals("")){
            blnIsHost = true;
            strUsername = strName;
            HostSocket = new SuperSocketMaster(6000, theView);
            blnConnected = HostSocket.connect();
            if(blnConnected){
                System.out.println("Host Connected");
                strPlayerList[0][0] = HostSocket.getMyAddress();
                strPlayerList[0][1] = strName;
                strPlayerList[0][2] = "0";
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