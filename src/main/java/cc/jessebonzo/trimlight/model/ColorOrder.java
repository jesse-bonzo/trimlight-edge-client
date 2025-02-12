package cc.jessebonzo.trimlight.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ColorOrder {
    RGB(0),
    RBG(1),
    GRB(2),
    GBR(3),
    BRG(4),
    BGR(5);

    private final int intValue;

    ColorOrder(int intValue) {
        this.intValue = intValue;
    }

    @JsonValue
    public int getIntValue() {
        return intValue;
    }
}
