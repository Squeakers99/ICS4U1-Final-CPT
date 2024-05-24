import Panels.*;
import java.awt.event.*;
import javax.swing.*;

public class View implements ActionListener{
    Model theModel = new Model(this);

    MainScreen theMainScreen = new MainScreen();
    ServerLobby theServerLobby = new ServerLobby();

    JFrame theFrame = new JFrame("Main Screen");

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == theMainScreen.theHostButton){
            if(theModel.initializeHost(theMainScreen.theNameField.getText())){
                System.out.println("IP: " + theModel.HostSocket.getMyAddress());
            }else{
                theMainScreen.theNameField.setText("Player 1");
            }
            theFrame.setContentPane(theServerLobby);
            theFrame.pack();
        }else if(e.getSource() == theMainScreen.theJoinButton){
            System.out.println("Join Button Pressed");
        }else if(e.getSource() == theMainScreen.theHelpButton){
            System.out.println("Help Button Pressed");
        }
    }

    public View(){
        //Main Screen Action Listeners
        theMainScreen.theHostButton.addActionListener(this);
        theMainScreen.theJoinButton.addActionListener(this);
        theMainScreen.theHelpButton.addActionListener(this);

        //Frame Setup
        theFrame.setVisible(true);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.setContentPane(theMainScreen);
        theFrame.pack();
    }
    public static void main(String[] args){
        new View();
    }
}
