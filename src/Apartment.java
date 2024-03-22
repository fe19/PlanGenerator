public class Apartment {

    public static double getRooms(int area) {
        if (area < 65) {
            return 2.5;
        } else if(area < 80) {
            return 3.5;
        } else if (area < 120) {
            return 4.5;
        } else {
            return 5.5;
        }
    }

    public static int getNettoArea(int width, int height, double wallAreaInPercent, int staircaseArea) {
        return (int) (width * height * (1.0 - wallAreaInPercent) - staircaseArea * height / height);
    }
}
