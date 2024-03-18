import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class ApartmentVariations {
    String IMAGE_NAME = "Wohnungsaufteilung";
    String FILE_EXTENSION = "jpg"; // jpg has 72ppi = 2835 pixel per m
    String VISA = "fe";
    String PATH = ".";
    int AREA;
    int FACTOR_M_IN_PIXEL = 100;
    int BUILDING_WIDTH_IN_PIXEL;
    int BUILDING_HEIGHT_IN_PIXEL;
    int MARGIN_IN_PIXEL = 100;
    boolean IS_DARK_MODE = true;

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

        int imageWidthInPixel = BUILDING_WIDTH_IN_PIXEL + 2 * MARGIN_IN_PIXEL;
        int imageHeightInPixel = BUILDING_HEIGHT_IN_PIXEL + 2 * MARGIN_IN_PIXEL;

        BufferedImage bufferedImage = new BufferedImage(imageWidthInPixel, imageHeightInPixel, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        if (IS_DARK_MODE) {
            graphics2D.setBackground(Color.black);
            graphics2D.setColor(Color.white);
        } else {
            // we need to draw a filled rect with white since setBackground(Color.white) does not work
            graphics2D.setColor(Color.white);
            graphics2D.fillRect(0,0,imageWidthInPixel, imageHeightInPixel);
            graphics2D.setColor(Color.black);
            graphics2D.drawRect(0,0,imageWidthInPixel - 1, imageWidthInPixel - 1);
        }

        // Header
        graphics2D.drawString("Aufteilung von " + AREA + " m^2 in Wohnungen " + "m^2", 20, 35);
        graphics2D.drawLine(0, MARGIN_IN_PIXEL / 2 + 10, imageWidthInPixel, MARGIN_IN_PIXEL / 2 + 10);

        // Ground plan
        graphics2D.drawRect(MARGIN_IN_PIXEL, MARGIN_IN_PIXEL, BUILDING_WIDTH_IN_PIXEL, BUILDING_HEIGHT_IN_PIXEL);
        graphics2D.drawString(String.valueOf(BUILDING_WIDTH_IN_PIXEL), MARGIN_IN_PIXEL - 5 + BUILDING_HEIGHT_IN_PIXEL / 2, MARGIN_IN_PIXEL - 10);
        graphics2D.drawString(String.valueOf(BUILDING_HEIGHT_IN_PIXEL), MARGIN_IN_PIXEL - 30, MARGIN_IN_PIXEL + 5 + BUILDING_HEIGHT_IN_PIXEL / 2);

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

    private String getCurrentTime() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date.getDayOfMonth() + "." + date.getMonthValue()+ "." + date.getYear() + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
    }

}
