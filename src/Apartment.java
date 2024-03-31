import java.awt.*;

public class Apartment {

    public static double getRooms(int area) {
        if (area < 65) {
            return 1.5;
        } else if (area < 75) {
            return 2.5;
        } else if(area < 100) {
            return 3.5;
        } else if (area < 125) {
            return 4.5;
        } else {
            return 5.5;
        }
    }

    public static int getArea(int width, int height, double wallAreaInPercent, int staircaseArea) {
        return (int) (width * height * (1.0 - wallAreaInPercent)) - staircaseArea;
    }

    public static int getArea(int width, int height, int staircaseArea) {
        return width * height - staircaseArea;
    }

    public static void setColor(Graphics2D graphics2D, double roomSize) {
        if (roomSize == 1.5) {
            graphics2D.setColor(new Color(233, 189, 243));
        } else if (roomSize == 2.5) {
            graphics2D.setColor(new Color(138, 211, 234));
        } else if (roomSize == 3.5) {
            graphics2D.setColor(new Color(177, 238, 180));
        } else if (roomSize == 4.5) {
            graphics2D.setColor(new Color(236, 236, 45));
        } else if (roomSize == 5.5) {
            graphics2D.setColor(new Color(245, 152, 152));
        }
    }

    public static void resetColor(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
    }

}
