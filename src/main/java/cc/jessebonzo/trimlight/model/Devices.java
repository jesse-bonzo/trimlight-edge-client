package cc.jessebonzo.trimlight.model;

import java.util.List;

public record Devices(int code, String desc, Payload payload) {
    public record Payload(
            int total,
            int current,
            int page,
            List<Data> data) {
        public record Data(
                String deviceId,
                String name,
                SwitchState switchState,
                Connectivity connectivity,
                DeviceState state,
                String fwVersionName) {
        }
    }
}
