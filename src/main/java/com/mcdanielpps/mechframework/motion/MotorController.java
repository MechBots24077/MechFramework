package com.mcdanielpps.mechframework.motion;

import com.mcdanielpps.mechframework.util.PIDController;
import com.mcdanielpps.mechframework.util.Time;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

public class MotorController {
    public DcMotor Motor;

    public int Goal = 0;
    private int m_CurrentPosition = 0;

    public final PIDController m_PID;
    private long m_LastMeasurement = 0;

    public MotorController(DcMotor motor, double kp, double ki, double kd) {
        Motor = motor;
        Motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        double ku = 5.0;
        double pu = 0.2;

        // 2.5, 0.1, 0.01
        m_PID = new PIDController(kp, ki, kd, 0.02, -100.0, 100.0, 0.01);
    }

    public void Init() {
        Motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public double Update() {
        long currentTime = Time.TimeGetter.currentTimeMillis();
        if ((currentTime - m_LastMeasurement) < 10) { return 0.0; }
        m_LastMeasurement = currentTime;

        m_CurrentPosition = Motor.getCurrentPosition();

        double output = m_PID.Update((double)Goal, (double)m_CurrentPosition);
        Motor.setPower(output / 100.0);

        return output;
    }

    public int GetCurrentPos() { return m_CurrentPosition; }
}
