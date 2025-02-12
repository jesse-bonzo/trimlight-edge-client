package cc.jessebonzo.trimlight.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Connectivity {
    OFFLINE(0), ONLINE(1);

    private final int intValue;

    Connectivity(int intValue) {
        this.intValue = intValue;
    }

    @JsonValue
    public int getIntValue() {
        return intValue;
    }
}
