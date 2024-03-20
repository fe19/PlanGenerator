import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class ApartmentSegmentationGenerator {
    static final String IMAGE_FILE_NAME = "Wohnungsaufteilung";
    static final String IMAGE_FILE_PATH = ".";
    static final String IMAGE_FILE_EXTENSION = "jpg"; // jpg has 72ppi = 2835 pixel per m
    static final String CREATOR_VISA = "fe";
    static final int NBR_VARIATIONS = 4;
    static final int MARGIN_IN_PIXEL = 100;
    static final double WALL_AREA_IN_PERCENTAGE = 0.15;
    static final int STAIRCASE_AREA_IN_SQUARE_M = 15;
    static final int FACTOR_M_IN_PIXEL = 20;
    static final boolean IS_DARK_MODE = false;

    static int BUILDING_WIDTH_IN_M;
    static int BUILDING_HEIGHT_IN_M;
    static int BUILDING_WIDTH_IN_PIXEL;
    static int BUILDING_HEIGHT_IN_PIXEL;
    static int BUILDING_AREA_IN_SQUARE_M;
    static int IMAGE_WIDTH_IN_PIXEL;
    static int IMAGE_HEIGHT_IN_PIXEL;
    static int VARIATION_BOX_WIDTH_IN_PIXEL;
    static int VARIATION_BOX_HEIGHT_IN_PIXEL;
    static Font DEFAULT_FONT = new Font(null, Font.PLAIN, 12);

    public void generateApartments() {
        long start = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Breite:");
        //BUILDING_WIDTH_IN_M = scanner.nextInt();
        BUILDING_WIDTH_IN_M = 10;

        System.out.println("Höhe:");
        //BUILDING_HEIGHT_IN_M = scanner.nextInt();
        BUILDING_HEIGHT_IN_M = 20;
        scanner.close();

        BUILDING_AREA_IN_SQUARE_M = (int) (BUILDING_WIDTH_IN_M * BUILDING_HEIGHT_IN_M * (1.0 - WALL_AREA_IN_PERCENTAGE)) - STAIRCASE_AREA_IN_SQUARE_M;

        BUILDING_WIDTH_IN_PIXEL = BUILDING_WIDTH_IN_M * FACTOR_M_IN_PIXEL;
        BUILDING_HEIGHT_IN_PIXEL = BUILDING_HEIGHT_IN_M * FACTOR_M_IN_PIXEL;

        VARIATION_BOX_WIDTH_IN_PIXEL = BUILDING_WIDTH_IN_PIXEL + 2 * MARGIN_IN_PIXEL;
        VARIATION_BOX_HEIGHT_IN_PIXEL = BUILDING_HEIGHT_IN_PIXEL + 2 * MARGIN_IN_PIXEL;

        IMAGE_WIDTH_IN_PIXEL = NBR_VARIATIONS * VARIATION_BOX_WIDTH_IN_PIXEL + 2 * MARGIN_IN_PIXEL ;
        IMAGE_HEIGHT_IN_PIXEL = VARIATION_BOX_HEIGHT_IN_PIXEL + 2 * MARGIN_IN_PIXEL;

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

        // Header TODO Extract into method. Maybe add draw package
        graphics2D.drawString("Varianten zur Wohnungsaufteilung mit einem Grundriss von " + BUILDING_WIDTH_IN_M * BUILDING_HEIGHT_IN_M + "m^2. "
                + "Die Nettowohnfläche beträgt " + BUILDING_AREA_IN_SQUARE_M + "m^2 ("
                + "abzüglich " + 100 * WALL_AREA_IN_PERCENTAGE + "% = " + (int) (BUILDING_WIDTH_IN_M * BUILDING_HEIGHT_IN_M * WALL_AREA_IN_PERCENTAGE) + "m^2 für die Wände"
                + " und " + STAIRCASE_AREA_IN_SQUARE_M + "m^2 für das Treppenhaus).", 20, 35);
        graphics2D.drawLine(0, MARGIN_IN_PIXEL / 2 + 10, IMAGE_WIDTH_IN_PIXEL, MARGIN_IN_PIXEL / 2 + 10);

        // Ground plan
        drawApartmentVariation("Variante 1", 0, graphics2D);
        drawApartmentVariation("Variante 2", 1, graphics2D);
        drawApartmentVariation("Variante 3", 2, graphics2D);
        drawApartmentVariation("Variante 4", 3, graphics2D);

        // Footer
        graphics2D.drawLine(0, IMAGE_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL / 2 - 10, IMAGE_WIDTH_IN_PIXEL, IMAGE_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL / 2 - 10);
        graphics2D.drawString("Gezeichnet: " + getCurrentTime(), 20, IMAGE_HEIGHT_IN_PIXEL - 25);
        graphics2D.drawLine(300, IMAGE_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL / 2 - 10, 300, IMAGE_HEIGHT_IN_PIXEL);
        graphics2D.drawString("Author: " + CREATOR_VISA, 310, IMAGE_HEIGHT_IN_PIXEL - 25);
        graphics2D.drawLine(500, IMAGE_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL / 2 - 10, 500, IMAGE_HEIGHT_IN_PIXEL);
        graphics2D.drawString("M: ", 510, IMAGE_HEIGHT_IN_PIXEL - 25);
        graphics2D.drawLine(700, IMAGE_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL / 2 - 10, 700, IMAGE_HEIGHT_IN_PIXEL);

        graphics2D.dispose();

        File file = new File(IMAGE_FILE_PATH + File.separator + IMAGE_FILE_NAME + "." + IMAGE_FILE_EXTENSION);

        try {
            ImageIO.write(bufferedImage, "jpg", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long timeElapsed = System.currentTimeMillis() - start;
        System.out.println("Generated image in \u001B[34m" + timeElapsed / 1000.0 + "s\u001B[0m at path " + file.getAbsolutePath());

    }

    private void drawApartmentVariation(String variationName, int variationNumber, Graphics2D graphics2D) {
        int buildingX = 2 * MARGIN_IN_PIXEL + variationNumber * VARIATION_BOX_WIDTH_IN_PIXEL;
        int buildingY = 2 * MARGIN_IN_PIXEL;
        String xLegend = BUILDING_WIDTH_IN_M + "m";
        String yLegend = BUILDING_HEIGHT_IN_M + "m";
        String variationText = variationName;

        graphics2D.drawRect(buildingX, buildingY, BUILDING_WIDTH_IN_PIXEL, BUILDING_HEIGHT_IN_PIXEL);

        int h1InM = getVariationLine(BUILDING_HEIGHT_IN_M);
        int h2InM = BUILDING_HEIGHT_IN_M - h1InM;
        int h1InPixel = h1InM * FACTOR_M_IN_PIXEL;
        int h2InPixel = h2InM * FACTOR_M_IN_PIXEL;
        int areaApartment1 = (int) (h1InM * BUILDING_WIDTH_IN_M * (1.0 - WALL_AREA_IN_PERCENTAGE) - STAIRCASE_AREA_IN_SQUARE_M * h1InM / BUILDING_HEIGHT_IN_M);
        int areaApartment2 = (int) (h2InM * BUILDING_WIDTH_IN_M * (1.0 - WALL_AREA_IN_PERCENTAGE) - STAIRCASE_AREA_IN_SQUARE_M * h2InM / BUILDING_HEIGHT_IN_M);
        String roomsApartment1 = getRooms(areaApartment1) + "Zi";
        String roomsApartment2 = getRooms(areaApartment2) + "Zi";

        graphics2D.drawRect(buildingX, buildingY, BUILDING_WIDTH_IN_PIXEL, h1InPixel);
        graphics2D.drawString(roomsApartment1, buildingX + BUILDING_WIDTH_IN_PIXEL / 2 - getTextLength(roomsApartment1) / 2, buildingY + h1InPixel / 2 + 10);
        // TODO Add area

        graphics2D.drawRect(buildingX, buildingY + h1InPixel, BUILDING_WIDTH_IN_PIXEL, h2InPixel);
        graphics2D.drawString(roomsApartment2, buildingX + BUILDING_WIDTH_IN_PIXEL / 2 - getTextLength(roomsApartment1) / 2, buildingY + h1InPixel + h2InPixel / 2 + 10);


        Font font = new Font(null, Font.PLAIN, 10);
        graphics2D.setFont(font);

        graphics2D.drawString(xLegend, buildingX + BUILDING_WIDTH_IN_PIXEL / 2 - getTextLength(xLegend) / 2, buildingY - 10);

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(-Math.PI/2);
        graphics2D.setFont(font.deriveFont(affineTransform));
        graphics2D.drawString(yLegend, buildingX - 10, buildingY + BUILDING_HEIGHT_IN_PIXEL / 2 + getTextLength(yLegend) / 2);
        graphics2D.setFont(DEFAULT_FONT);

        graphics2D.drawString(variationName, buildingX + BUILDING_WIDTH_IN_PIXEL / 2 - getTextLength(variationText) / 2, buildingY + VARIATION_BOX_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL - 20);
    }

    private double getRooms(int area) {
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

    private int getVariationLine(int heightInMeter) {
        int minimum = 5;
        return (int) (Math.random() * (heightInMeter - 2 * minimum)) + minimum;
    }

    private int getTextLength(String text) {
        BufferedImage bufferedImage = new BufferedImage(500, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        return graphics2D.getFontMetrics().stringWidth(text);
    }

    private String getCurrentTime() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date.getDayOfMonth() + "." + date.getMonthValue()+ "." + date.getYear() + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
    }

}
