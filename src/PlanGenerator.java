import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class PlanGenerator {

    int IMAGE_HEIGHT = 2480; // A4 height
    int IMAGE_WIDTH = 3508; // A4 width
    String IMAGE_NAME = "Grundriss";
    String FILE_EXTENSION = "jpg"; // jpg has 72ppi = 2835 pixel per m
    String VISA = "fe";
    String PATH = ".";
    int FACTOR_M_IN_PIXEL = 50;
    int MARGIN_IN_PIXEL = 75;

    public void generateImage() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Length:");
        //int length = scanner.nextInt();
        int widthInM = 10;

        System.out.println("Width:");
        //int width = scanner.nextInt();
        int heightInM = 5;

        System.out.println("Rooms:");
        //int nbrRooms = scanner.nextInt();
        int nbrRooms = 3;

        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.white);

        // Header
        graphics2D.drawString("Grundriss für " + nbrRooms + " Zimmer mit " + widthInM * heightInM + "m^2", 20, 20);
        graphics2D.drawLine(0, MARGIN_IN_PIXEL / 2 + 10, IMAGE_WIDTH, MARGIN_IN_PIXEL / 2 + 10);
        //g2d.fillOval(0, 0, 5, 5);

        // Ground plan
        graphics2D.drawRect(MARGIN_IN_PIXEL + 20, MARGIN_IN_PIXEL, MARGIN_IN_PIXEL + widthInM * FACTOR_M_IN_PIXEL, MARGIN_IN_PIXEL + heightInM * FACTOR_M_IN_PIXEL);

        // Footer
        graphics2D.drawLine(0, IMAGE_HEIGHT - MARGIN_IN_PIXEL / 2 - 10, IMAGE_WIDTH, IMAGE_HEIGHT - MARGIN_IN_PIXEL / 2 - 10);
        graphics2D.drawString("Gezeichnet: " + getCurrentTime(), 20, IMAGE_HEIGHT - 15);
        graphics2D.drawLine(300, IMAGE_HEIGHT - MARGIN_IN_PIXEL / 2 - 10, 300, IMAGE_HEIGHT);
        graphics2D.drawString("Author: " + VISA, 310, IMAGE_HEIGHT - 15);
        graphics2D.drawLine(500, IMAGE_HEIGHT - MARGIN_IN_PIXEL / 2 - 10, 500, IMAGE_HEIGHT);
        graphics2D.drawString("Massstab: 1:" + 100, 510, IMAGE_HEIGHT - 15);
        graphics2D.drawLine(700, IMAGE_HEIGHT - MARGIN_IN_PIXEL / 2 - 10, 700, IMAGE_HEIGHT);

        graphics2D.dispose();

        File file = new File(PATH + File.separator + IMAGE_NAME + "." + FILE_EXTENSION);

        try {
            ImageIO.write(bufferedImage, "jpg", file);
            System.out.println("Write image to " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCurrentTime() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date.getDayOfMonth() + "." + date.getMonthValue()+ "." + date.getYear() + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
    }

    private void drawGroundplan() {

    }

}
