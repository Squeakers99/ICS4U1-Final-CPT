package Panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Assets {

    //Fonts
    public Font fntHelvetica12 = new Font("Helvetica", Font.BOLD, 12);
    public Font fntHelvetica20 = new Font("Helvetica", Font.BOLD, 20);
    public Font fntHelvetica30 = new Font("Helvetica", Font.BOLD, 30);
    public Font fntHelvetica50 = new Font("Helvetica", Font.BOLD, 50);
    public Font fntHelvetica100 = new Font("Helvetica", Font.BOLD, 100);
    public Font fntHelvetica120 = new Font("Helvetica", Font.BOLD, 120);

    //Colors
    public Color clrWhite = new Color(255, 255, 255);
    public Color clrBlack = new Color(0, 0, 0);
    public Color clrGray = new Color(192, 192, 192);
    public Color clrCrimson = new Color(220, 20, 60);
    public Color clrGreen = new Color(0, 128, 0);

    //Image Resources
    InputStream imageClass;
    BufferedImage img;

    //Images
    BufferedImage imgBackground = this.loadImage("../Assets/Background.png");
    public static BufferedImage imgBoard = null;
    public static BufferedImage imgTop = null;
    public static BufferedImage imgBottom = null;

    //Load Image Method
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
}
