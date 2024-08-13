package timify.com.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    /**
     * YYYYMMDD 형식의 string을 LocalDate type으로 변환
     *
     * @param dateString
     * @return
     */
    public static LocalDate stringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * yyyyMMddHHmm 형식의 string을 LocalTime type으로 변환
     *
     * @param dateTimeString
     * @return
     */
    public static LocalDateTime stringToLocalTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}
