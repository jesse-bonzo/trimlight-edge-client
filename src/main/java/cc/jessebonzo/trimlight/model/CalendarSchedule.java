package cc.jessebonzo.trimlight.model;

public record CalendarSchedule(
        /*
          Calendar schedule ID.
          Schedule ID range: [0, 59].
          Up to 60 calendar schedules can be saved.
         */
        int id,
        int effectId,
        ScheduleDate startDate,
        ScheduleDate endDate,
        ScheduleTime startTime,
        ScheduleTime endTime) {
}
