import Panels.*;
import java.awt.event.*;
import javax.swing.*;

public class View implements ActionListener{
    Model theModel = new Model(this);

    MainScreen mainScreen = new MainScreen();
    JFrame theFrame = new JFrame("Main Screen");
    Thread ReceiveIP = new Thread(new ReceiveIP(theModel));

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == mainScreen.theHostButton){
            if(theModel.initializeHost(mainScreen.theNameField.getText())){
                System.out.println("IP: " + theModel.HostSocket.getMyAddress());
            }else{
                mainScreen.theNameField.setText("Player 1");
            }
        }else if(e.getSource() == mainScreen.theJoinButton){
            ReceiveIP.start();
            System.out.println("Connected to " + theModel.strPlayerList[0][1] + " at " + theModel.strPlayerList[0][0]);
        }else if(e.getSource() == mainScreen.theHelpButton){
            System.out.println("Help Button Pressed");
        }
    }

    public View(){
        //Main Screen Action Listeners
        mainScreen.theHostButton.addActionListener(this);
        mainScreen.theJoinButton.addActionListener(this);
        mainScreen.theHelpButton.addActionListener(this);

        //Frame Setup
        theFrame.setVisible(true);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setContentPane(mainScreen);
        theFrame.pack();
    }
    public static void main(String[] args){
        new View();
    }
}
