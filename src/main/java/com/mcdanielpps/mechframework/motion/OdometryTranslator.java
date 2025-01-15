package com.mcdanielpps.mechframework.motion;

import com.qualcomm.robotcore.hardware.DcMotor;

public class OdometryTranslator {
    private RobotPosition m_Position = new RobotPosition(0.0f, 0.0f, 0.0f);

    private DcMotor LeftEncoder;
    private DcMotor CenterEncoder;
    private DcMotor RightEncoder;

    public float L = 328.7625f;
    public float B = 140.0f;

    public float R = 48.0f;
    public int N = 8192;

    private float C = (2.0f * (float)Math.PI * R) / N;

    public OdometryTranslator(DcMotor leftEncoder, DcMotor centerEncoder, DcMotor rightEncoder) {
        LeftEncoder = leftEncoder;
        CenterEncoder = centerEncoder;
        RightEncoder = rightEncoder;
    }

    public RobotPosition Update() {
        float dn1 = 0.0f; // Left
        float dn2 = 0.0f; // Right
        float dn3 = 0.0f; // Center

        float dx = C * ((dn1 + dn2) / 2.0f);
        float dtheta =C * ((dn2 - dn1) / L);
        float dy = C * (dn3 - B * dtheta);

        // left off at https://youtu.be/Av9ZMjS--gY?si=pzfDlOjQRVyZec9N&t=1136

        return m_Position;
    }
}
