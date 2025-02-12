package cc.jessebonzo.trimlight.model;

import java.time.LocalDate;

public record ScheduleDate(
        /*
          Schedule month, range from [1, 12].
         */
        int month,
        /*
          Schedule day, range from [1, 31].
         */
        int day) {
    public ScheduleDate(LocalDate localDate) {
        this(localDate.getMonth().getValue(), localDate.getDayOfMonth());
    }
}
