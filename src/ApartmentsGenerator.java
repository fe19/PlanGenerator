import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ApartmentsGenerator {
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

        Image.switchDarkMode(graphics2D, IS_DARK_MODE, IMAGE_WIDTH_IN_PIXEL, IMAGE_HEIGHT_IN_PIXEL);

        // Header
        Image.drawHeader(graphics2D, BUILDING_WIDTH_IN_M, BUILDING_HEIGHT_IN_M, MARGIN_IN_PIXEL, BUILDING_AREA_IN_SQUARE_M, WALL_AREA_IN_PERCENTAGE, STAIRCASE_AREA_IN_SQUARE_M, IMAGE_WIDTH_IN_PIXEL);

        // Ground plan
        for (int i = 0; i < NBR_VARIATIONS; i++) {
            drawApartmentVariation("Variante " + (i + 1), i, graphics2D);
        }

        // Footer
        Image.drawFooter(graphics2D, IMAGE_WIDTH_IN_PIXEL, IMAGE_HEIGHT_IN_PIXEL, MARGIN_IN_PIXEL, CREATOR_VISA);

        File file = getFile(bufferedImage, graphics2D);

        long timeElapsed = System.currentTimeMillis() - start;
        System.out.println("Generated image in \u001B[34m" + timeElapsed / 1000.0 + "s\u001B[0m at path " + file.getAbsolutePath());

    }

    private static File getFile(BufferedImage bufferedImage, Graphics2D graphics2D) {
        graphics2D.dispose();

        File file = new File(IMAGE_FILE_PATH + File.separator + IMAGE_FILE_NAME + "." + IMAGE_FILE_EXTENSION);

        try {
            ImageIO.write(bufferedImage, IMAGE_FILE_EXTENSION, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private void drawApartmentVariation(String variationName, int variationNumber, Graphics2D graphics2D) {
        int buildingX = 2 * MARGIN_IN_PIXEL + variationNumber * VARIATION_BOX_WIDTH_IN_PIXEL;
        int buildingY = 2 * MARGIN_IN_PIXEL;
        String xLegend = BUILDING_WIDTH_IN_M + "m";
        String yLegend = BUILDING_HEIGHT_IN_M + "m";
        String variationText = variationName + "";

        int h1InM = HLineVariation.getVariationLine(BUILDING_HEIGHT_IN_M);
        int h2InM = BUILDING_HEIGHT_IN_M - h1InM;
        int h1InPixel = h1InM * FACTOR_M_IN_PIXEL;
        int h2InPixel = h2InM * FACTOR_M_IN_PIXEL;
        int areaApartment1 = Apartment.getNettoArea(BUILDING_WIDTH_IN_M, h1InM, WALL_AREA_IN_PERCENTAGE, STAIRCASE_AREA_IN_SQUARE_M);
        int areaApartment2 = Apartment.getNettoArea(BUILDING_WIDTH_IN_M, h2InM, WALL_AREA_IN_PERCENTAGE, STAIRCASE_AREA_IN_SQUARE_M);
        String sizeApartment1 = areaApartment1 + "m^2";
        String sizeApartment2 = areaApartment2+ "m^2";
        String roomsApartment1 = Apartment.getRooms(areaApartment1) + "Zi";
        String roomsApartment2 = Apartment.getRooms(areaApartment2) + "Zi";

        Apartment.setColor(graphics2D, Apartment.getRooms(areaApartment1));
        graphics2D.fillRect(buildingX, buildingY, BUILDING_WIDTH_IN_PIXEL, h1InPixel);
        Apartment.resetColor(graphics2D);
        graphics2D.drawString(roomsApartment1, buildingX + BUILDING_WIDTH_IN_PIXEL / 2 - Util.getTextLength(roomsApartment1, graphics2D) / 2, buildingY + h1InPixel / 2 - Util.getTextHeight(graphics2D) / 3);
        graphics2D.drawString(sizeApartment1, buildingX + BUILDING_WIDTH_IN_PIXEL / 2 - Util.getTextLength(sizeApartment1, graphics2D) / 2, buildingY + h1InPixel / 2 + Util.getTextHeight(graphics2D));

        Apartment.setColor(graphics2D, Apartment.getRooms(areaApartment2));
        graphics2D.fillRect(buildingX, buildingY + h1InPixel, BUILDING_WIDTH_IN_PIXEL, h2InPixel);
        Apartment.resetColor(graphics2D);
        graphics2D.drawString(roomsApartment2, buildingX + BUILDING_WIDTH_IN_PIXEL / 2 - Util.getTextLength(roomsApartment1, graphics2D) / 2, buildingY + h1InPixel + h2InPixel / 2 - Util.getTextHeight(graphics2D) / 3);
        graphics2D.drawString(sizeApartment2, buildingX + BUILDING_WIDTH_IN_PIXEL / 2 - Util.getTextLength(sizeApartment2, graphics2D) / 2, buildingY + h1InPixel + h2InPixel / 2 + Util.getTextHeight(graphics2D));

        Font font = new Font(null, Font.PLAIN, 10);
        graphics2D.setFont(font);

        graphics2D.drawString(xLegend, buildingX + BUILDING_WIDTH_IN_PIXEL / 2 - Util.getTextLength(xLegend, graphics2D) / 2, buildingY - 10);

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(-Math.PI/2);
        graphics2D.setFont(font.deriveFont(affineTransform));
        graphics2D.drawString(yLegend, buildingX - 10, buildingY + BUILDING_HEIGHT_IN_PIXEL / 2 + Util.getTextLength(yLegend, graphics2D) / 2);
        graphics2D.setFont(DEFAULT_FONT);

        graphics2D.drawString(variationName, buildingX + BUILDING_WIDTH_IN_PIXEL / 2 - Util.getTextLength(variationText, graphics2D) / 2, buildingY + VARIATION_BOX_HEIGHT_IN_PIXEL - MARGIN_IN_PIXEL - 20);
    }

}
