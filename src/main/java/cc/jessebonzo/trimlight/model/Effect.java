package cc.jessebonzo.trimlight.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Effect(
        /*
          Effect ID.
          ID of the saved effect.
          Note that it may be -1 in currentEffect , when the controller is
          running a preview effect (not yet saved).
         */
        int id,
        String name,
        /*
         * 0 - built in effect
         * 1 - custom effect
         */
        int category,
        /*
         * Effect mode.
         * Build-in effect (category value is 0) mode range: [0, 179].
         * Custom effect (category value is 1) mode range: [0, 16].
         */
        int mode,
        /*
         * range [0, 255]
         */
        int speed,
        /*
         * range [0, 255]
         */
        int brightness,
        /*
         * Effect pixel length.
         * (Only required for build-in effects)
         * Pixel length range: [1, 90].
         */
        Integer pixelLen,
        /*
         * Reverse effect.
         * (Only required for build-in effects)
         */
        Boolean reverse,
        /*
         * Custom effect pixels.
         * (Only required for build-in effects)
         */
        List<Pixel> pixels) {

    public static final int BUILT_IN = 0;
    public static final int CUSTOM = 1;
}
