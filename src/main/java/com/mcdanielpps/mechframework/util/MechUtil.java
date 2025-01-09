package com.mcdanielpps.mechframework.util;

public class MechUtil {
    // https://docs.arduino.cc/language-reference/en/functions/math/map/
    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
