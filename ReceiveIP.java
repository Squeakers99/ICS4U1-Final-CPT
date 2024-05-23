import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class ReceiveIP implements Runnable{
    Model theModel;

    public void run(){
        try{
            //Create a DatagramSocket
            DatagramSocket inSocket = new DatagramSocket(6001);

            while(!theModel.blnGameStarted){
                //Create a DatagramPacket to receive the message
                byte[] data = new byte[1024];
                DatagramPacket inPacket = new DatagramPacket(data, data.length);
                
                try{
                    //Receive the packet
                    inSocket.receive(inPacket);
                    
                    //Get the message
                    String strMessage = new String(inPacket.getData(), 0, inPacket.getLength());
                    
                    //Parse the message
                    String[] strAvailableHost = strMessage.split(",");
                    String strIP = strAvailableHost[0];
                    String strName = strAvailableHost[1];

                    System.out.println(strName + " has been found at IP " + strIP);
                    
                    }catch(SocketTimeoutException e){}
                    
                    //Close the socket
                    inSocket.close();
                }
            }
            catch(Exception e){
                
            }
    } 
    public ReceiveIP(Model theModel){
        this.theModel = theModel;
    }
}