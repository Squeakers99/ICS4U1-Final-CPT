import Panels.*;
import java.awt.event.*;
import javax.swing.*;

public class View implements ActionListener{
    MainScreen mainScreen = new MainScreen();
    JFrame theFrame = new JFrame("Main Screen");

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == mainScreen.theHostButton){
            System.out.println("Host Button Pressed");
        }else if(e.getSource() == mainScreen.theJoinButton){
            System.out.println("Join Button Pressed");
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
