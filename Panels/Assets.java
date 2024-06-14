package Panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * The Assets class represents a collection of fonts, colors, and images used in the application.
 */
public class Assets {

    //Fonts
    /**Helvetica font, size 12 */
    public Font fntHelvetica12 = new Font("Helvetica", Font.BOLD, 12);
    /**Helvetica font, size 20 */
    public Font fntHelvetica20 = new Font("Helvetica", Font.BOLD, 20);
    /**Helvetica font, size 30 */
    public Font fntHelvetica30 = new Font("Helvetica", Font.BOLD, 30);
    /**Helvetica font, size 50 */
    public Font fntHelvetica50 = new Font("Helvetica", Font.BOLD, 50);
    /**Helvetica font, size 100 */
    public Font fntHelvetica100 = new Font("Helvetica", Font.BOLD, 100);
    /**Helvetica font, size 120 */
    public Font fntHelvetica120 = new Font("Helvetica", Font.BOLD, 120);

    //Colors
    /** White Color */
    public Color clrWhite = new Color(255, 255, 255);
    /** Black Color */
    public Color clrBlack = new Color(0, 0, 0);
    /** Gray Color */
    public Color clrGray = new Color(192, 192, 192);
    /** Crimson Color */
    public Color clrCrimson = new Color(220, 20, 60);
    /** Green Color */
    public Color clrGreen = new Color(0, 128, 0);

    //Image Resources
    InputStream imageClass;
    BufferedImage img;

    //Images
    BufferedImage imgBackground = this.loadImage("Assets/Background.png");
    BufferedImage imgHelp = this.loadImage("Assets/Help.png");
    /** Game Board */
    public static BufferedImage imgBoard = null;
    /** Red Checker */
    public static BufferedImage imgRed = null;
    /** Black Checker */
    public static BufferedImage imgBlack = null;
    /** Checker being dragged */
    public static BufferedImage imgDragged = null;

    /**
     * Loads an image from the specified file path.
     *
     * @param strFileName the file path of the image to load
     * @return the loaded image as a BufferedImage object
     */
    public BufferedImage loadImage(String strFileName) {
        imageClass = null;
        img = null;

        imageClass = this.getClass().getResourceAsStream(strFileName);

        if (imageClass == null) {
            System.out.println("Unable to load image file");
        } else {
            try {
                img = ImageIO.read(imageClass);
            } catch (IOException e) {
                System.out.println(e.toString());
                System.out.println("Unable to load image file");
            }
        }

        try {
            img = ImageIO.read(new File(strFileName));
        } catch (IOException e) {
            System.out.println("Unable to load local image file");
        }

        return img;
    }

    /** Constructor for this class */
    public Assets(){}
}
