package cc.jessebonzo.trimlight.model;

import java.time.LocalTime;

public record ScheduleTime(
        /*
          Schedule hours, range from [1, 24].
         */
        int hours,
        /*
          Schedule minutes, range from [1, 60].
         */
        int minutes) {
    public ScheduleTime(LocalTime localTime) {
        this(localTime.getHour() + 1, localTime.getMinute() + 1);
    }
}
