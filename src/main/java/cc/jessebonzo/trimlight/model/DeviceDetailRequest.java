package cc.jessebonzo.trimlight.model;

import java.time.LocalDateTime;

public record DeviceDetailRequest(String deviceId, CurrentDate currentDate) {
    public DeviceDetailRequest(String deviceId) {
        this(deviceId, CurrentDate.fromLocalDateTime(LocalDateTime.now()));
    }
}
