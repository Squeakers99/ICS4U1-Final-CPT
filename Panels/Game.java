package Panels;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class Game extends JPanel {
    public Game() {
        // Panel Formatting
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(null);
    }
    public static void GameLogic() throws IOException{
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        String chrBoard[][] = new String[8][8];
        int intRed = 12;
        int intBlack = 12;
        int intTurn = 0;
        boolean blnTurnRed;
        FileReader theFileReader = new FileReader("Maps/currentBoard.csv");
        BufferedReader theBufferedReader = new BufferedReader(theFileReader);

        for(int intLoop = 0; intLoop < 8; intLoop++){
            chrBoard[intLoop] = theBufferedReader.readLine().split(",");
        }

        //for(char[] c:chrBoard)
          //  for(char letter:c)



        while(intRed > 0 && intBlack > 0){
            
        }

        
    
    }
}