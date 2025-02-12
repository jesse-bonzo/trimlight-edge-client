package cc.jessebonzo.trimlight.model;

import java.time.LocalDateTime;

public class CurrentDate {
    public static CurrentDate fromLocalDateTime(LocalDateTime localDateTime) {
        var currentDate = new CurrentDate();
        currentDate.year = localDateTime.getYear() - 2000; // :(
        currentDate.month = localDateTime.getMonthValue();
        currentDate.day = localDateTime.getDayOfMonth();
        currentDate.weekday = switch (localDateTime.getDayOfWeek()) {
            case SUNDAY -> 1;
            case MONDAY -> 2;
            case TUESDAY -> 3;
            case WEDNESDAY -> 4;
            case THURSDAY -> 5;
            case FRIDAY -> 6;
            case SATURDAY -> 7;
        };
        currentDate.hours = localDateTime.getHour();
        currentDate.minutes = localDateTime.getMinute();
        currentDate.seconds = localDateTime.getSecond();
        return currentDate;
    }

    public int year;
    public int month;
    public int day;
    public int weekday;
    public int hours;
    public int minutes;
    public int seconds;

    @Override
    public String toString() {
        return "CurrentDate{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", weekday=" + weekday +
                ", hours=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                '}';
    }
}
