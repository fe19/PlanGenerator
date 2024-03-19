import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class ApartmentVariations {
    static final String IMAGE_NAME = "Wohnungsaufteilung";
    static final String FILE_EXTENSION = "jpg"; // jpg has 72ppi = 2835 pixel per m
    static final String VISA = "fe";
    static final String PATH = ".";
    static final int NBR_VARIATIONS = 3;
    static int AREA;
    static int FACTOR_M_IN_PIXEL = 20;
    static int BUILDING_WIDTH_IN_PIXEL;
    static int BUILDING_HEIGHT_IN_PIXEL;
    static int IMAGE_WIDTH_IN_PIXEL = 1200;
    static int IMAGE_HEIGHT_IN_PIXEL = 700;
    static int MARGIN_IN_PIXEL = 100;
    static boolean IS_DARK_MODE = true;

    public void generateApartments() {
        long start = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Fläche:");
        //AREA = scanner.nextInt();
        AREA = 200;
        scanner.close();

        // Form of building. First approach: building is a square
        BUILDING_WIDTH_IN_PIXEL = (int) (FACTOR_M_IN_PIXEL * Math.sqrt(1.0 * AREA));
        BUILDING_HEIGHT_IN_PIXEL = (int) (FACTOR_M_IN_PIXEL * Math.sqrt(1.0 * AREA));

        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH_IN_PIXEL, IMAGE_HEIGHT_IN_PIXEL, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        if (IS_DARK_MODE) {
            graphics2D.setBackground(Color.black);
            graphics2D.setColor(Color.white);
        } else {
            // we need to draw a filled rect with white since setBackground(Color.white) does not work
            graphics2D.setColor(Color.white);
            graphics2D.fillRect(0,0,IMAGE_WIDTH_IN_PIXEL, IMAGE_HEIGHT_IN_PIXEL);
            graphics2D.setColor(Color.black);
            graphics2D.drawRect(0,0,IMAGE_WIDTH_IN_PIXEL - 1, IMAGE_WIDTH_IN_PIXEL - 1);
        }

        // Header
        graphics2D.drawString("Aufteilung von " + AREA + " m^2 in Wohnungen " + "m^2", 20, 35);
        graphics2D.drawLine(0, MARGIN_IN_PIXEL / 2 + 10, IMAGE_WIDTH_IN_PIXEL, MARGIN_IN_PIXEL / 2 + 10);

        // Ground plan
        graphics2D.drawRect(MARGIN_IN_PIXEL, MARGIN_IN_PIXEL, BUILDING_WIDTH_IN_PIXEL, BUILDING_HEIGHT_IN_PIXEL);
        graphics2D.drawString(String.valueOf(BUILDING_WIDTH_IN_PIXEL), MARGIN_IN_PIXEL - 5 + BUILDING_HEIGHT_IN_PIXEL / 2, MARGIN_IN_PIXEL - 10);
        graphics2D.drawString(String.valueOf(BUILDING_HEIGHT_IN_PIXEL), MARGIN_IN_PIXEL - 30, MARGIN_IN_PIXEL + 5 + BUILDING_HEIGHT_IN_PIXEL / 2);

        // Footer
        graphics2D.drawLine(0, IMAGE_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL / 2 - 10, IMAGE_WIDTH_IN_PIXEL, IMAGE_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL / 2 - 10);
        graphics2D.drawString("Gezeichnet: " + getCurrentTime(), 20, IMAGE_HEIGHT_IN_PIXEL - 25);
        graphics2D.drawLine(300, IMAGE_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL / 2 - 10, 300, IMAGE_HEIGHT_IN_PIXEL);
        graphics2D.drawString("Author: " + VISA, 310, IMAGE_HEIGHT_IN_PIXEL - 25);
        graphics2D.drawLine(500, IMAGE_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL / 2 - 10, 500, IMAGE_HEIGHT_IN_PIXEL);
        graphics2D.drawString("M: ", 510, IMAGE_HEIGHT_IN_PIXEL - 25);
        graphics2D.drawLine(700, IMAGE_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL / 2 - 10, 700, IMAGE_HEIGHT_IN_PIXEL);

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

    private void drawApartmentVariation(String variationName, int roomX, int roomY, int roomWidth, int roomHeight, Graphics2D graphics2D) {
        int roomArea = roomWidth * roomHeight / (FACTOR_M_IN_PIXEL * FACTOR_M_IN_PIXEL);
        String livingRoomText = variationName + " " + roomArea + "m^2";
        graphics2D.drawRect(roomX, roomY, roomWidth, roomHeight);
        graphics2D.drawString(livingRoomText, roomX + roomWidth / 2 - graphics2D.getFontMetrics().stringWidth(livingRoomText) / 2, roomY + roomHeight / 2 - 5);
    }

    private String getCurrentTime() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date.getDayOfMonth() + "." + date.getMonthValue()+ "." + date.getYear() + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
    }

}
