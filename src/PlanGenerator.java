import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class PlanGenerator {

    String IMAGE_NAME = "Grundrissplan";
    String FILE_EXTENSION = "jpg"; // jpg has 72ppi = 2835 pixel per m
    String VISA = "fe";
    String PATH = ".";
    int NBR_ROOMS;
    int APARTMENT_WIDTH_IN_M;
    int APARTMENT_HEIGHT_IN_M = 5;
    int FACTOR_M_IN_PIXEL = 100;
    int APARTMENT_WIDTH_IN_PIXEL;
    int APARTMENT_HEIGHT_IN_PIXEL;
    int MARGIN_IN_PIXEL = 100;
    boolean IS_DARK_MODE = true;
    double FACTOR_LIVING_ROOM_BATHROOM = 0.7;
    double FACTOR_BEDROOM_KITCHEN = 0.6;

    public void generateImage() {
        long start = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Breite:");
        //APARTMENT_WIDTH_IN_M = scanner.nextInt();
        APARTMENT_WIDTH_IN_M = 10;
        System.out.println("Höhe:");
        //APARTMENT_HEIGHT_IN_M = scanner.nextInt();
        APARTMENT_HEIGHT_IN_M = 5;
        scanner.close();

        NBR_ROOMS = calculateNbrOfRooms(APARTMENT_WIDTH_IN_M, APARTMENT_HEIGHT_IN_M);
        
        APARTMENT_WIDTH_IN_PIXEL = APARTMENT_WIDTH_IN_M * FACTOR_M_IN_PIXEL;
        APARTMENT_HEIGHT_IN_PIXEL = APARTMENT_HEIGHT_IN_M * FACTOR_M_IN_PIXEL;
        
        int imageWidthInPixel = APARTMENT_WIDTH_IN_PIXEL + 2 * MARGIN_IN_PIXEL;
        int imageHeightInPixel = APARTMENT_HEIGHT_IN_PIXEL + 2 * MARGIN_IN_PIXEL;

        BufferedImage bufferedImage = new BufferedImage(imageWidthInPixel, imageHeightInPixel, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        Image.switchDarkMode(graphics2D, IS_DARK_MODE, imageWidthInPixel, imageHeightInPixel);

        // Header
        graphics2D.drawString("Grundrissplan für " + NBR_ROOMS + " Zimmer mit " + APARTMENT_WIDTH_IN_M * APARTMENT_HEIGHT_IN_M + "m^2", 20, 35);
        graphics2D.drawLine(0, MARGIN_IN_PIXEL / 2 + 10, imageWidthInPixel, MARGIN_IN_PIXEL / 2 + 10);

        // Ground plan
        graphics2D.drawRect(MARGIN_IN_PIXEL, MARGIN_IN_PIXEL, APARTMENT_WIDTH_IN_PIXEL, APARTMENT_HEIGHT_IN_PIXEL);
        graphics2D.drawString(String.valueOf(APARTMENT_WIDTH_IN_PIXEL), MARGIN_IN_PIXEL - 5 + APARTMENT_WIDTH_IN_PIXEL / 2, MARGIN_IN_PIXEL - 10);
        graphics2D.drawString(String.valueOf(APARTMENT_HEIGHT_IN_PIXEL), MARGIN_IN_PIXEL - 30, MARGIN_IN_PIXEL + 5 + APARTMENT_HEIGHT_IN_PIXEL / 2);

        // Rooms
        drawRoom("Wohnzimmer", MARGIN_IN_PIXEL, MARGIN_IN_PIXEL + APARTMENT_HEIGHT_IN_PIXEL / 2, (int) (FACTOR_LIVING_ROOM_BATHROOM * APARTMENT_WIDTH_IN_PIXEL), APARTMENT_HEIGHT_IN_PIXEL / 2, graphics2D);
        drawRoom("Badezimmer", MARGIN_IN_PIXEL + (int) (FACTOR_LIVING_ROOM_BATHROOM * APARTMENT_WIDTH_IN_PIXEL), MARGIN_IN_PIXEL + APARTMENT_HEIGHT_IN_PIXEL / 2, (int) (APARTMENT_WIDTH_IN_PIXEL * (1 - FACTOR_LIVING_ROOM_BATHROOM)), APARTMENT_HEIGHT_IN_PIXEL / 2, graphics2D);
        drawRoom("Schlafzimmer", MARGIN_IN_PIXEL, MARGIN_IN_PIXEL, (int) (APARTMENT_WIDTH_IN_PIXEL * FACTOR_BEDROOM_KITCHEN), APARTMENT_HEIGHT_IN_PIXEL / 2, graphics2D);
        drawRoom("Küche", MARGIN_IN_PIXEL + (int) (APARTMENT_WIDTH_IN_PIXEL * FACTOR_BEDROOM_KITCHEN), MARGIN_IN_PIXEL, (int) (APARTMENT_WIDTH_IN_PIXEL * (1 - FACTOR_BEDROOM_KITCHEN)), APARTMENT_HEIGHT_IN_PIXEL / 2, graphics2D);

        // Footer
        graphics2D.drawLine(0, imageHeightInPixel - MARGIN_IN_PIXEL / 2 - 10, imageWidthInPixel, imageHeightInPixel - MARGIN_IN_PIXEL / 2 - 10);
        graphics2D.drawString("Gezeichnet: " + getCurrentTime(), 20, imageHeightInPixel - 25);
        graphics2D.drawLine(300, imageHeightInPixel - MARGIN_IN_PIXEL / 2 - 10, 300, imageHeightInPixel);
        graphics2D.drawString("Author: " + VISA, 310, imageHeightInPixel - 25);
        graphics2D.drawLine(500, imageHeightInPixel - MARGIN_IN_PIXEL / 2 - 10, 500, imageHeightInPixel);
        graphics2D.drawString("M: 1:" + 100, 510, imageHeightInPixel - 25);
        graphics2D.drawLine(700, imageHeightInPixel - MARGIN_IN_PIXEL / 2 - 10, 700, imageHeightInPixel);

        graphics2D.dispose();

        File file = new File(PATH + File.separator + IMAGE_NAME + "." + FILE_EXTENSION);

        try {
            ImageIO.write(bufferedImage, "jpg", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long timeElapsed = System.currentTimeMillis() - start;
        System.out.println("Generated image in \u001B[34m" + timeElapsed / 1000.0 + "s\u001B[0m at path " + file.getAbsolutePath());

    }

    private void drawRoom(String roomName, int roomX, int roomY, int roomWidth, int roomHeight, Graphics2D graphics2D) {
        int roomArea = roomWidth * roomHeight / (FACTOR_M_IN_PIXEL * FACTOR_M_IN_PIXEL);
        String livingRoomText = roomName + " " + roomArea + "m^2";
        graphics2D.drawRect(roomX, roomY, roomWidth, roomHeight);
        graphics2D.drawString(livingRoomText, roomX + roomWidth / 2 - graphics2D.getFontMetrics().stringWidth(livingRoomText) / 2, roomY + roomHeight / 2 - 5);
    }

    private int calculateNbrOfRooms(int width, int height) {
        int area = width * height;
        int averageAreaKitchen = 12;
        int averageAreaBath = 8;
        int averageAreaRoom = 15;
        int areaRooms = area - averageAreaKitchen - averageAreaBath;
        return areaRooms / averageAreaRoom;
    }

    private String getCurrentTime() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date.getDayOfMonth() + "." + date.getMonthValue()+ "." + date.getYear() + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
    }

}
