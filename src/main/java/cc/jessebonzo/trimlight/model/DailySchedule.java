package cc.jessebonzo.trimlight.model;

public record DailySchedule(
        int id,
        boolean enable,
        int effectId,
        int repetition,
        ScheduleTime startTime,
        ScheduleTime endTime) {
}
