package com.mcdanielpps.mechframework.motion;

import com.mcdanielpps.mechframework.util.task.ITask;
import com.mcdanielpps.mechframework.util.task.TaskStatus;

public abstract class CalibrationTask implements ITask {
    private final int m_Delta;
    private final int m_ReboundDistance;

    private enum InternalState {
        FindingLimit,
        Rebounding,
        Done
    }
    private InternalState m_State = InternalState.FindingLimit;

    // Delta is how much it moves on each update step
    // Rebound distance is how far it moves back up after hitting the limit switch
    protected CalibrationTask(int delta, int reboundDistance) {
        m_Delta = delta;
        m_ReboundDistance = reboundDistance;
    }

    @Override
    public TaskStatus GetStatus() {
        return m_State == InternalState.Done ? TaskStatus.Done : TaskStatus.Running;
    }

    @Override
    public void Start() {}

    @Override
    public void Update() {
        switch (m_State) {
            case FindingLimit:
                FindingLimitStep();
                break;

            case Rebounding:
                ReboundingStep();
                break;

            default:
                break;
        }
    }

    private void FindingLimitStep() {
        if (IsAtLimit()) {
            Move(m_ReboundDistance);
            m_State = InternalState.Rebounding;
        } else {
            Move(-m_Delta);
        }
    }

    private void ReboundingStep() {
        if (IsAtGoal()){
            ResetEncoders();
            m_State = InternalState.Done;
        }
    }

    public abstract boolean IsAtLimit();
    public abstract boolean IsAtGoal();
    public abstract void Move(int amount);
    public abstract void ResetEncoders();
}
