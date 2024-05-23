//Intended to be run on its own thread

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastIP implements Runnable{
    //Properties
    Model theModel;

    //Methods
    @Override
    public void run(){
        try{
            //Create a DatagramSocket
            DatagramSocket outSocket = new DatagramSocket();
            
            //Set the broadcast address
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

            while(!theModel.blnGameStarted){
                //Message to be sent
                String message = theModel.getStatus();
                byte[] data = message.getBytes();

                //Create a DatagramPacket with the message, length, and broadcast address
                DatagramPacket outPacket = new DatagramPacket(data, data.length, broadcastAddress, 6001);

                //Send the packet
                outSocket.send(outPacket);

                try{
                    Thread.sleep(1000);
                }
                catch(InterruptedException e){}
            }

            //Close the socket
            outSocket.close();
        }
        catch(Exception e){}
    }

    //Constructor
    public BroadcastIP(Model theModel){
        this.theModel = theModel;
    }
}