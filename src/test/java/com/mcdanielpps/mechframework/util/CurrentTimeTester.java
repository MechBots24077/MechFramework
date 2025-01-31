package com.mcdanielpps.mechframework.util;

public class CurrentTimeTester implements ICurrentTimeGetter {
    public long CurrentTime = 0;

    @Override
    public long currentTimeMillis() {
        return CurrentTime;
    }
}
