import Panels.*;
import javax.swing.*;

public class View {
    MainScreen mainScreen = new MainScreen();
    JFrame theFrame = new JFrame("Main Screen");

    public View(){
        theFrame.setVisible(true);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.add(mainScreen);
        theFrame.pack();
    }
    public static void main(String[] args){
        new View();
    }
}
