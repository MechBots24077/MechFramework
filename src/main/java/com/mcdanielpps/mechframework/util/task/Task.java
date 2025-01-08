package com.mcdanielpps.mechframework.util.task;

public interface Task {
    public TaskStatus GetStatus();

    public void Start();
    public void Update();
}
