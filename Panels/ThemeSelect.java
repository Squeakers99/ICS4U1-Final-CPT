package Panels;

import java.awt.*;
import javax.swing.*;

/**
 * The ThemeSelect class represents a JPanel that allows the user to select a theme.
 * It displays a list of available themes as buttons and provides a "Select Theme" button.
 * The selected theme can be used to customize the appearance of the application.
 */
public class ThemeSelect extends JPanel{
    Assets programAssets = new Assets();

    /**Theme actions Object to perform actions with the given theme */
    public ThemeActions theThemeActions = new ThemeActions("Assets/Themes/skins.csv");
    /** Buttons for the theme selection */
    public JButton[] themeButtons = new JButton[theThemeActions.getLineCount()];
    /** Button to select the theme */
    public JButton theSelectButton = new JButton("Select Theme");

    private String[] strThemes = new String[theThemeActions.getLineCount()];
    private int intButtonY = 0;
    private JLabel theThemeSelectTitle = new JLabel("Select a Theme");

    /**
     * Paints the background image on the panel.
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(programAssets.imgBackground, 0, 0, null);
    }

    /**
     * Constructs a new ThemeSelect panel.
     */
    public ThemeSelect() {
        // Panel Formatting
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(null);

        // Title Formatting
        theThemeSelectTitle.setFont(programAssets.fntHelvetica100);
        theThemeSelectTitle.setForeground(programAssets.clrWhite);
        theThemeSelectTitle.setBounds(0, 30, 1280, 150);
        theThemeSelectTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(theThemeSelectTitle);

        //Gets Themes
        strThemes = theThemeActions.getAvailableThemes();
        
        //Theme Button Formatting
        for(int intLoop = 0; intLoop < strThemes.length; intLoop++){
            themeButtons[intLoop] = new JButton(strThemes[intLoop]);
            if(themeButtons[intLoop].getText().equals("Default")){
                themeButtons[intLoop].setBackground(programAssets.clrGreen);
            }else{
                themeButtons[intLoop].setBackground(programAssets.clrWhite);
            }
            themeButtons[intLoop].setFont(programAssets.fntHelvetica30);
            themeButtons[intLoop].setForeground(programAssets.clrBlack);
            themeButtons[intLoop].setBorder(null);
            this.add(themeButtons[intLoop]);
        }

        //Theme Buttons Positioning
        for(int intLoop = 0; intLoop < themeButtons.length; intLoop++){
            switch (intLoop % 3) {
                case 0 -> themeButtons[intLoop].setBounds(50 + (intLoop/3), 225 + (intButtonY * 75), 376, 50);
                case 1 -> themeButtons[intLoop].setBounds(451 + ((intLoop-1)/3), 225 + (intButtonY * 75), 376, 50);
                default -> {
                    themeButtons[intLoop].setBounds(852 + ((intLoop-2)/3), 225 + (intButtonY * 75), 376, 50);
                    intButtonY++;
                }
            }
        }

        //Select Button Formatting
        theSelectButton.setFont(programAssets.fntHelvetica30);
        theSelectButton.setForeground(programAssets.clrBlack);
        theSelectButton.setBounds(440, 600, 400, 50);
        theSelectButton.setBorder(null);
        theSelectButton.setHorizontalAlignment(SwingConstants.CENTER);
        theSelectButton.setBackground(programAssets.clrWhite);
        this.add(theSelectButton);
    }
}
