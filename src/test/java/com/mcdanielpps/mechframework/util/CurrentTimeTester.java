package com.mcdanielpps.mechframework.util;

public class CurrentTimeTester implements CurrentTimeGetter{
    public long CurrentTime = 0;

    @Override
    public long currentTimeMillis() {
        return CurrentTime;
    }
}
