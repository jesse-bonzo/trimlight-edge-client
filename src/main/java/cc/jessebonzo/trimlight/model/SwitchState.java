package cc.jessebonzo.trimlight.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SwitchState {
    LIGHT_OFF(0), MANUAL_MODE(1), TIMER_MODE(2);

    private final int intValue;

    SwitchState(int intValue) {
        this.intValue = intValue;
    }

    @JsonValue
    public int getIntValue() {
        return intValue;
    }
}
