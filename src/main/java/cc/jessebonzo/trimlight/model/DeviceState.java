package cc.jessebonzo.trimlight.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DeviceState {
    NORMAL(0), UPGRADING(1);

    private final int intValue;

    DeviceState(int intValue) {
        this.intValue = intValue;
    }

    @JsonValue
    public int getIntValue() {
        return intValue;
    }
}
