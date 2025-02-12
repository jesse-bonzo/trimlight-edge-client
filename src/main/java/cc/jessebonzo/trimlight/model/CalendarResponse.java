package cc.jessebonzo.trimlight.model;

public record CalendarResponse(int code, String desc, Payload payload) {
    public record Payload(int id /* calendar id */) {
    }
}
