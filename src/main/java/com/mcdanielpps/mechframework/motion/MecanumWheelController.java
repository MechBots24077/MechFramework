package com.mcdanielpps.mechframework.motion;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MecanumWheelController {
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

    public void UpdateWheels(double theta, double power, double turn, double speedCoefficient) {
        // Copied from https://www.youtube.com/watch?v=gnSW2QpkGXQ

        double sin = Math.sin(theta - Math.PI / 4.0);
        double cos = Math.cos(theta - Math.PI / 4.0);
        double max = Math.max(Math.abs(sin), Math.abs(cos));

        double leftFront = power * cos / max + CastInvert(InvertFL) * turn;
        double rightFront = power * sin / max - CastInvert(InvertFR) * turn;
        double leftRear = power * sin / max + CastInvert(InvertRL) * turn;
        double rightRear = power * cos / max - CastInvert(InvertRR) * turn;

        if ((power + Math.abs(turn)) > 1.0) {
            leftFront /= power + turn;
            rightFront /= power + turn;
            leftRear /= power + turn;
            rightRear /= power + turn;
        }

        FL.setPower(leftFront * speedCoefficient);
        FR.setPower(rightFront * speedCoefficient);
        RL.setPower(leftRear * speedCoefficient);
        RR.setPower(rightRear * speedCoefficient);
    }

//    public void UpdateWheelsGamepad(double x, double y, double turn, double speedCoefficient) {
//        double theta = Math.atan2(y, x);
//        double power = Math.hypot(x, y);
//        UpdateWheels(theta, power, turn, speedCoefficient);
//    }

    public void UpdateWheelsGamepad(double x, double y, double turn, double speedCoefficient) {
        // Crazy magic math https://github.com/FIRST-Tech-Challenge/FtcRobotController/blob/master/FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples/BasicOmniOpMode_Linear.java
        double flPower  = x + y - turn;
        double frPower = x - y - turn;
        double rlPower   = x - y + turn;
        double rrPower  = x + y + turn;

        // Normalize the values
        double max = Math.max(Math.abs(flPower), Math.abs(frPower));
        max = Math.max(max, Math.abs(rlPower));
        max = Math.max(max, Math.abs(rrPower));

        if (max > 1.0) {
            flPower  /= max;
            frPower /= max;
            rlPower   /= max;
            rrPower  /= max;
        }

        // Apply the calculated power to the wheels
        FL.setPower(flPower * speedCoefficient * CastInvert(InvertFL));
        FR.setPower(frPower * speedCoefficient * CastInvert(InvertFR));
        RL.setPower(rlPower * speedCoefficient * CastInvert(InvertRL));
        RR.setPower(rrPower * speedCoefficient * CastInvert(InvertRR));
    }

    // Helper function to convert the boolean invert input to a coefficient that can be used for the math
    private double CastInvert(boolean invert) {
        return invert ? -1.0 : 1.0;
    }
}
