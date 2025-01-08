package com.mcdanielpps.mechframework.util.task;

import com.mcdanielpps.mechframework.util.Time;

import java.util.ArrayList;

public class TaskManager {

    private final ArrayList<Task> m_Tasks = new ArrayList<>();

    public void SpawnTask(Task task) {
        m_Tasks.add(task);
        task.Start();
    }

    public void Update() {
        Time.Update();

        for (int i = 0; i < m_Tasks.size(); i++) {
            Task task = m_Tasks.get(i);
            task.Update();
        }

        // Remove tasks that are done
        m_Tasks.removeIf(task -> (task.GetStatus() == TaskStatus.Done));
    }

    // Singleton pattern
    private TaskManager() {}
    private static TaskManager s_Instance = null;
    public static TaskManager getInstance() {
        if (s_Instance == null)
            s_Instance = new TaskManager();
        return s_Instance;
    }
}
