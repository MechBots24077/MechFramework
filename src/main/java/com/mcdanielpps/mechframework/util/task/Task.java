package com.mcdanielpps.mechframework.util.task;

public interface Task {
    TaskStatus GetStatus();

    void Start();
    void Update();
}
