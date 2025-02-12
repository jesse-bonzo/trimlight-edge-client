package cc.jessebonzo.trimlight.model;

public record DeviceStateRequest(
        String deviceId,
        Payload payload) {

    public DeviceStateRequest(String deviceId, SwitchState switchState) {
        this(deviceId, new Payload(switchState));
    }

    public DeviceStateRequest(String deviceId, String newName) {
        this(deviceId, new Payload(newName));
    }

    public DeviceStateRequest(String deviceId, ColorOrder newColorOrder) {
        this(deviceId, new Payload(newColorOrder));
    }

    public record Payload(
            SwitchState switchState,
            String name,
            ColorOrder colorOrder) {
        public Payload(SwitchState state) {
            this(state, null, null);
        }

        public Payload(String name) {
            this(null, name, null);
        }

        public Payload(ColorOrder colorOrder) {
            this(null, null, colorOrder);
        }
    }
}
