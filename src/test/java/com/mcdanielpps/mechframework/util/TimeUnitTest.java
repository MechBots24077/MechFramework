package com.mcdanielpps.mechframework.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TimeUnitTest {
    @Test
    public void DeltaTime_isCorrect() {
        // Control the current time value used by Time
        CurrentTimeTester timeTester = new CurrentTimeTester();
        Time.TimeGetter = timeTester;

        // Both current and last time should be 500
        timeTester.CurrentTime = 500;
        Time.Init();
        Time.Update();
        assertEquals(0.0, Time.DeltaTime(), 0.00000000001);

        // Current should be 1000 and last should be 500
        timeTester.CurrentTime = 1000;
        Time.Update();
        assertEquals(0.5, Time.DeltaTime(), 0.00000000001);

        // Current should be 1250 and last should be 1000
        timeTester.CurrentTime = 1250;
        Time.Update();
        assertEquals(0.25, Time.DeltaTime(), 0.00000000001);
    }
}
