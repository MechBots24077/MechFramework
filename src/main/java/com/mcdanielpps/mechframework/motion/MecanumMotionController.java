package com.mcdanielpps.mechframework.motion;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MecanumMotionController {
    private MecanumWheelController m_WheelController = new MecanumWheelController();

    public void SetMotors(DcMotor fl, DcMotor fr, DcMotor rl, DcMotor rr) {
        m_WheelController.FL = fl;
        m_WheelController.FR = fr;
        m_WheelController.RL = rl;
        m_WheelController.RR = rr;
    }
}
