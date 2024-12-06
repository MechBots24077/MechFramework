package com.mcdanielpps.mechframework.motion;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.junit.Assert;

public class DcMotorTester implements DcMotor {
    // Configuration variables
    public ZeroPowerBehavior ZeroPower = DcMotor.ZeroPowerBehavior.UNKNOWN;
    public int Port = 0;
    public Manufacturer HardwareManufacturer = Manufacturer.Unknown;
    public String Name = "Unit Test Motor";

    // Control variables
    public double Power = 0.0;
    public int TargetPosition = 0;
    public RunMode Mode = RunMode.RUN_USING_ENCODER;
    public Direction InternalDirection = Direction.FORWARD; // FTC guarantees this to start as Forward

    // State variables
    public boolean IsBusy = false;
    public int CurrentPosition = 0;

    // Internal error state management variables to warn of certain errors that
    // may be caused by the FTC internal code
    private boolean m_TargetPositionSet = false;
    private boolean m_RunModeSet = false;
    private boolean m_Closed = false;

    // Motor configuration currently does nothing because using it in tests causes an exception
    @Override
    public MotorConfigurationType getMotorType() {
        CatchClosedFailure();

        return null;
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        CatchClosedFailure();
    }

    @Override
    public DcMotorController getController() {
        CatchClosedFailure();

        return null;
    }

    @Override
    public int getPortNumber() {
        CatchClosedFailure();

        return Port;
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        CatchClosedFailure();

        ZeroPower = zeroPowerBehavior;
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        CatchClosedFailure();

        return ZeroPower;
    }

    @Override
    public void setPowerFloat() {
        CatchClosedFailure();

        ZeroPower = ZeroPowerBehavior.FLOAT;
        Power = 0.0;
    }

    @Override
    public boolean getPowerFloat() {
        CatchClosedFailure();

        return ZeroPower == ZeroPowerBehavior.FLOAT && Power == 0.0;
    }

    @Override
    public void setTargetPosition(int position) {
        CatchClosedFailure();

        TargetPosition = position;
        m_TargetPositionSet = true;
    }

    @Override
    public int getTargetPosition() {
        CatchClosedFailure();

        return TargetPosition;
    }

    @Override
    public boolean isBusy() {
        CatchClosedFailure();

        return IsBusy;
    }

    @Override
    public int getCurrentPosition() {
        CatchClosedFailure();

        return CurrentPosition;
    }

    @Override
    public void setMode(RunMode mode) {
        CatchClosedFailure();

        if (mode == RunMode.RUN_TO_POSITION && !m_TargetPositionSet) {
            Assert.fail("RunMode was set to RUN_TO_POSITION before a target position was specified. This will cause an exception on the robot.");
        }

        Mode = mode;
        m_RunModeSet = true;
    }

    @Override
    public RunMode getMode() {
        CatchClosedFailure();

        return Mode;
    }

    @Override
    public void setDirection(Direction direction) {
        CatchClosedFailure();

        InternalDirection = direction;
    }

    @Override
    public Direction getDirection() {
        CatchClosedFailure();

        return InternalDirection;
    }

    @Override
    public void setPower(double power) {
        CatchClosedFailure();
        CatchRunModeSetFailure();

        Power = power;
    }

    @Override
    public double getPower() {
        CatchClosedFailure();
        CatchRunModeSetFailure();

        return Power;
    }

    @Override
    public Manufacturer getManufacturer() {
        CatchClosedFailure();

        return HardwareManufacturer;
    }

    @Override
    public String getDeviceName() {
        CatchClosedFailure();

        return Name;
    }

    @Override
    public String getConnectionInfo() {
        CatchClosedFailure();

        return "Simulated Connection";
    }

    @Override
    public int getVersion() {
        CatchClosedFailure();

        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        // Configuration variables
        ZeroPower = DcMotor.ZeroPowerBehavior.UNKNOWN;

        // Control variables
        Power = 0.0;
        TargetPosition = 0;
        Mode = RunMode.RUN_USING_ENCODER;
        InternalDirection = Direction.FORWARD;

        // State variables
        IsBusy = false;

        // Internal error state management variables
        m_TargetPositionSet = false;
        m_RunModeSet = false;
        m_Closed = false;
    }

    @Override
    public void close() {
        m_Closed = true;
    }

    // I don't know what actually happens when the connection is closed so for now the tester
    // will define that as an illegal state to access the motor in
    private void CatchClosedFailure() {
        if (m_Closed) {
            Assert.fail("Can't use a DcMotor after its connection has been closed.");
        }
    }

    // This one is more of a suggestion, but all of the code in this codebase should follow this rule
    private void CatchRunModeSetFailure() {
        if (!m_RunModeSet) {
            Assert.fail("Use of a DcMotor before setting its RunMode should be avoided.");
        }
    }
}
