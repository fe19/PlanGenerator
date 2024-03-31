import java.awt.*;

public class Image {

    public static void drawHeader(Graphics2D graphics2D, int width, int height, int margin, double wallAreaInPercentage, int staircaseArea, int widthInPixel) {
        graphics2D.drawString("Varianten zur Wohnungsaufteilung mit einem Grundriss von " + width * height + "m^2. "
                + "Die nutzbare Fläche beträgt " + Apartment.getArea(width, height, staircaseArea) + "m^2 (abzüglich " + staircaseArea + "m^2 für das Treppenhaus). "
                + "Die Nettowohnfläche beträgt " + Apartment.getArea(width, height, wallAreaInPercentage, staircaseArea) + "m^2 ("
                + "abzüglich " + 100 * wallAreaInPercentage + "% = " + (int) (width * height * wallAreaInPercentage) + "m^2 für die Wände).", 20, 35);
        graphics2D.drawLine(0, margin / 2 + 10, widthInPixel, margin / 2 + 10);
    }

    public static void drawFooter(Graphics2D graphics2D, int imageWidth, int imageHeight, int margin, String visa) {
        graphics2D.drawLine(0, imageHeight - margin / 2 - 10, imageWidth, imageHeight - margin / 2 - 10);
        graphics2D.drawString("Gezeichnet: " + Util.getCurrentTime(), 20, imageHeight - 25);
        graphics2D.drawLine(300, imageHeight - margin / 2 - 10, 300, imageHeight);
        graphics2D.drawString("Author: " + visa, 310, imageHeight - 25);
        graphics2D.drawLine(500, imageHeight - margin / 2 - 10, 500, imageHeight);
        graphics2D.drawString("M: ", 510, imageHeight - 25);
        graphics2D.drawLine(700, imageHeight - margin / 2 - 10, 700, imageHeight);
    }

    public static void switchDarkMode(Graphics2D graphics2D, boolean isDarkMode, int width, int height) {
        if (isDarkMode) {
            graphics2D.setBackground(Color.black);
            graphics2D.setColor(Color.white);
        } else {
            // we need to draw a filled rect with white since setBackground(Color.white) does not work
            graphics2D.setColor(Color.white);
            graphics2D.fillRect(0,0,width, height);
            graphics2D.setColor(Color.black);
            graphics2D.drawRect(0,0,width - 1, height - 1);
        }
    }
}
