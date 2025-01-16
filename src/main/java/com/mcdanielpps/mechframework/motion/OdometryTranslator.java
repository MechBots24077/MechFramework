package com.mcdanielpps.mechframework.motion;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.mcdanielpps.mechframework.util.RobotSystem;
import com.qualcomm.robotcore.hardware.DcMotor;

public class OdometryTranslator {
    private RobotPosition m_Position = new RobotPosition(0.0f, 0.0f, 0.0f);

    private DcMotor LeftEncoder;
    private DcMotor CenterEncoder;
    private DcMotor RightEncoder;

    private float m_N1 = 0.0f;
    private float m_N2 = 0.0f;
    private float m_N3 = 0.0f;

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

    public void Init() {
        m_N1 = LeftEncoder.getCurrentPosition();
        m_N2 = RightEncoder.getCurrentPosition();
        m_N3 = CenterEncoder.getCurrentPosition();
    }

    public RobotPosition Update() {
        // Read encoders
        float n1 = LeftEncoder.getCurrentPosition();
        float n2 = RightEncoder.getCurrentPosition();
        float n3 = CenterEncoder.getCurrentPosition();

        TelemetryPacket packet = RobotSystem.getInstance().GetTelemetryPacket();
        packet.put("n1", n1);
        packet.put("n2", n2);
        packet.put("n3", n3);

        // Change in encoder readout
        float dn1 = n1 - m_N1; // Left
        float dn2 = n2 - m_N2; // Right
        float dn3 = n3 - m_N3; // Center

        packet.put("dn1", dn1);
        packet.put("dn2", dn2);
        packet.put("dn3", dn3);

        m_N1 = n1;
        m_N2 = n2;
        m_N3 = n3;

        // Change in position in robot space
        float dx = C * ((dn1 + dn2) / 2.0f);
        float dtheta =C * ((dn2 - dn1) / L);
        float dy = C * (dn3 - B * dtheta);

        packet.put("dx", dx);
        packet.put("dtheta", dtheta);
        packet.put("dy", dy);

        // Change in position in global space
        m_Position = new RobotPosition(
                (float)(m_Position.X + dx * Math.cos(m_Position.Rotation) - dy * Math.sin(m_Position.Rotation)),
                (float)(m_Position.Y + dx * Math.sin(m_Position.Rotation) + dy * Math.cos(m_Position.Rotation)),
                m_Position.Rotation + dtheta
        );

        return m_Position;
    }
}
