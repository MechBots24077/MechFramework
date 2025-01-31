package com.mcdanielpps.mechframework.util.task;

public interface ITask {
    TaskStatus GetStatus();

    void Start();
    void Update();
}
