package com.mcdanielpps.mechframework.motion;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class OdometryTranslator {
    public DcMotor Left = null;
    public DcMotor Center = null;
    public DcMotor Right = null;

    public void UpdateTelemetry(Telemetry telemetry) {
        telemetry.addData("Left", Left.getCurrentPosition());
        telemetry.addData("Center", Center.getCurrentPosition());
        telemetry.addData("Right", Right.getCurrentPosition());
    }
}
