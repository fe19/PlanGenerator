import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Util {

    public static String getCurrentTime() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date.getDayOfMonth() + "." + date.getMonthValue()+ "." + date.getYear() + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
    }

    public static int getTextLength(String text, Graphics2D graphics2D) {
        return graphics2D.getFontMetrics().stringWidth(text);
    }

    public static int getTextHeight(Graphics2D graphics2D) {
        return graphics2D.getFontMetrics().getMaxAscent();
    }

}
