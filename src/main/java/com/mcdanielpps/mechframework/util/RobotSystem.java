package com.mcdanielpps.mechframework.util;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.mcdanielpps.mechframework.util.task.ITask;
import com.mcdanielpps.mechframework.util.task.TaskStatus;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class RobotSystem {

    private final ArrayList<ITask> m_Tasks = new ArrayList<>();

    private Telemetry m_Telemetry = null;
    private TelemetryPacket m_Packet = null;
    private HardwareMap m_HardwareMap = null;
    private Gamepad m_Gamepad1 = null;
    private Gamepad m_Gamepad2 = null;

    // Lifecycle methods

    public void Init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2)
    {
        m_Telemetry = telemetry;
        m_HardwareMap = hardwareMap;
        m_Gamepad1 = gamepad1;
        m_Gamepad2 = gamepad2;

        Time.Init();
    }

    public void Update() {
        Time.Update();

        m_Packet = new TelemetryPacket();
        m_Packet.put("Delta Time (ms)", Time.DeltaTime() * 1000.0);

        for (int i = 0; i < m_Tasks.size(); i++) {
            ITask task = m_Tasks.get(i);
            task.Update();
        }

        // Remove tasks that are done
        m_Tasks.removeIf(task -> (task.GetStatus() == TaskStatus.Done));

        FtcDashboard.getInstance().sendTelemetryPacket(m_Packet);
        m_Telemetry.update();
    }

    // Functionality

    public void SpawnTask(ITask task) {
        m_Tasks.add(task);
        task.Start();
    }

    public Telemetry GetTelemetry() { return m_Telemetry; }
    public TelemetryPacket GetTelemetryPacket() { return m_Packet; }
    public HardwareMap GetHardwareMap() { return m_HardwareMap; }
    public Gamepad GetGamepad1() { return m_Gamepad1; }
    public Gamepad GetGamepad2() { return m_Gamepad2; }


    // Singleton pattern
    private RobotSystem() {}
    private static RobotSystem s_Instance = null;
    public static RobotSystem getInstance() {
        if (s_Instance == null)
            s_Instance = new RobotSystem();
        return s_Instance;
    }
}
