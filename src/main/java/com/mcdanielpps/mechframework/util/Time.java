package com.mcdanielpps.mechframework.util;

public class Time {
    public static CurrentTimeGetter TimeGetter = System::currentTimeMillis;

    public static double DeltaTime() { return s_DeltaTime; }

    public static void Update() {
        long currentTime = TimeGetter.currentTimeMillis();
        s_DeltaTime = ((double)currentTime - (double)s_LastTime) / 1000.0;
        s_LastTime = currentTime;
    }

    public static void Init() {
        s_LastTime = TimeGetter.currentTimeMillis();
    }

    private static long s_LastTime = 0;
    private static double s_DeltaTime = 0.0;
}
