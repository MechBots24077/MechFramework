package com.mcdanielpps.mechframework.motion;

import com.qualcomm.robotcore.hardware.DcMotor;

// THIS IS A REALLY BAD EXAMPLE OF WORKING CODE AND NEEDS TO BE CHANGED TO AN ACTUALLY FUNCTIONAL EXAMPLE
// This code should work without InvertFL set to true, but it doesn't.
public class TestedMecanumWheelController {
    public DcMotor FL = null;
    public DcMotor FR = null;
    public DcMotor RL = null;
    public DcMotor RR = null;

    public boolean InvertFL = false;
    public boolean InvertFR = false;
    public boolean InvertRL = false;
    public boolean InvertRR = false;

    public void ResetMotors() {
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void InitMotors(boolean useEncoder) {
        FL.setMode(useEncoder ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(useEncoder ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RL.setMode(useEncoder ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RR.setMode(useEncoder ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void UpdateWheels(double x, double y, double turn, double speedCoefficient) {
        double a = x + y;
        double b = y - x;

        double flPower = a - turn;
        double frPower = b + turn;
        double rlPower = b - turn;
        double rrPower = a + turn;

        double max = Math.max(Math.abs(flPower), Math.abs(frPower));
        max = Math.max(max, Math.abs(rlPower));
        max = Math.max(max, Math.abs(rrPower));

        if (max > 1.0) {
            flPower /= max;
            frPower /= max;
            rlPower /= max;
            rrPower /= max;
        }

        FL.setPower(CastInvert(InvertFL) * flPower * speedCoefficient);
        FR.setPower(CastInvert(InvertFR) * frPower * speedCoefficient);
        RL.setPower(CastInvert(InvertRL) * rlPower * speedCoefficient);
        RR.setPower(CastInvert(InvertRR) * rrPower * speedCoefficient);
    }

    // Helper function to convert the boolean invert input to a coefficient that can be used for the math
    private double CastInvert(boolean invert) {
        return invert ? -1.0 : 1.0;
    }
}
