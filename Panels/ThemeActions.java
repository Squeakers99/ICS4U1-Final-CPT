package Panels;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ThemeActions {

    private BufferedReader theBufferedReader = null;
    private boolean blnEOF = false;
    private FileReader theFileReader = null;
    private String[][] strThemes = null;
    private String[] strThemeNames = null;
    private String[] strThemeReturn = new String[4];
    private int intLineCount = 0;
    private String strFileData = "";
    private String strLine = "";

    // Reads a line from the file
    /**
     * Reads a line from the file
     *
     * @return the line that was read
     */
    public String readLine() {
        String strOldLine;
        try {
            if (this.blnEOF == true) {
                System.out.println("Attempting to read past the end of the file");
            }
            strOldLine = this.strLine;
            this.strLine = this.theBufferedReader.readLine();
            if (this.strLine == null || this.strLine.length() == 0) {
                this.blnEOF = true;
            }
            return strOldLine;
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    // Closes the file
    /**
     * Closes the file from reading or writing
     */
    public void close() {
        try {
            this.theBufferedReader.close();
            this.theFileReader.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        blnEOF = true;
    }

    // Loads all the theme data into a string
    /**
     * Loads the theme data into a string
     */
    public void leaderboardDataLoading() {
        while (this.blnEOF == false) {
            intLineCount++;
            this.strFileData += this.readLine() + ";";
        }
        this.close();
    }

    // Loads the theme data into the strThemes array
    /**
     * Loads the theme data into an array
     */
    public void LoadArray() {
        this.leaderboardDataLoading();
        strThemes = new String[intLineCount][4];
        strThemeNames = new String[intLineCount];
        String[] strThemeData = new String[intLineCount];
        String[] strThemeData2 = new String[4];
        strThemeData = this.strFileData.split(";");
        for (int intLineNumber = 0; intLineNumber < intLineCount; intLineNumber++) {
            strThemeData2 = strThemeData[intLineNumber].split(",");
            this.strThemes[intLineNumber][0] = strThemeData2[0];
            this.strThemes[intLineNumber][1] = strThemeData2[1];
            this.strThemes[intLineNumber][2] = strThemeData2[2];
            this.strThemes[intLineNumber][3] = strThemeData2[3];
        }
        for (int intLoop = 0; intLoop < strThemes.length; intLoop++) {
            strThemeNames[intLoop] = strThemes[intLoop][0];
        }
    }

    public String[] getAvailableThemes() {
        return this.strThemeNames;
    }

    public int getLineCount() {
        return this.intLineCount;
    }

    public String[] getThemeData(String strRequestedTheme){
        for (int intLoop = 0; intLoop < strThemes.length; intLoop++) {
            if (strThemes[intLoop][0].equals(strRequestedTheme)) {
                strThemeReturn = strThemes[intLoop];
                return strThemeReturn;
            }
        }
        return null;
    }

    public ThemeActions(String strFileName) {
        try {
            this.theFileReader = new FileReader(strFileName);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }
        if (this.theFileReader != null) {
            this.theBufferedReader = new BufferedReader(this.theFileReader);
            // now that the file is opened for reading... read the first line but don't return it
            try {
                this.strLine = this.theBufferedReader.readLine();
                if (this.strLine == null || this.strLine.length() == 0) {
                    this.blnEOF = true;
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        } else {
            this.blnEOF = true;
        }
        this.LoadArray();
    }
}
