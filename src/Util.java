import java.time.LocalDate;
import java.time.LocalTime;

public class Util {

    public static String getCurrentTime() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date.getDayOfMonth() + "." + date.getMonthValue()+ "." + date.getYear() + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
    }
}
