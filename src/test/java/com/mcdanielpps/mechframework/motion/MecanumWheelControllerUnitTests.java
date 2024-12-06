package com.mcdanielpps.mechframework.motion;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MecanumWheelControllerUnitTests {
    final double allowedDelta = 0.00001;

    private void CompareWithTestedController(String testName, double delta, MecanumWheelController controller, TestedMecanumWheelController testedController) {
        double[] actuals = {
                ((DcMotorTester)controller.FL).Power,
                ((DcMotorTester)controller.FR).Power,
                ((DcMotorTester)controller.RL).Power,
                ((DcMotorTester)controller.RR).Power
        };

        double[] expecteds = {
                ((DcMotorTester)testedController.FL).Power,
                ((DcMotorTester)testedController.FR).Power,
                ((DcMotorTester)testedController.RL).Power,
                ((DcMotorTester)testedController.RR).Power
        };

        System.out.println(testName + " (FL, FR, RL, RR)");
        System.out.println(Arrays.toString(actuals));
        System.out.println(Arrays.toString(expecteds));
        System.out.println();

        Assert.assertArrayEquals("Failed to pass '" + testName + "'", expecteds, actuals, delta);
    }

    public MecanumWheelController GetInitializedWheelController() {
        MecanumWheelController wheelController = new MecanumWheelController();
        wheelController.FL = new DcMotorTester();
        wheelController.FR = new DcMotorTester();
        wheelController.RL = new DcMotorTester();
        wheelController.RR = new DcMotorTester();

        wheelController.ResetMotors();
        wheelController.InitMotors(false);

        return  wheelController;
    }

    @Test
    public void MecanumWheels_movementIsCorrect() {
        MecanumWheelController wheelController = GetInitializedWheelController();

        TestedMecanumWheelController testedController = new TestedMecanumWheelController();
        testedController.FL = new DcMotorTester();
        testedController.FR = new DcMotorTester();
        testedController.RL = new DcMotorTester();
        testedController.RR = new DcMotorTester();

        testedController.ResetMotors();
        testedController.InitMotors(false);

        // Left stick full forward
        wheelController.UpdateWheels(0.0, 1.0, 0.0, 1.0);
        testedController.UpdateWheels(0.0, 1.0, 0.0, 1.0);
        CompareWithTestedController("Move forward", allowedDelta, wheelController, testedController);

        // Left stick full backwards
        wheelController.UpdateWheels(0.0, -1.0, 0.0, 1.0);
        testedController.UpdateWheels(0.0, -1.0, 0.0, 1.0);
        CompareWithTestedController("Move backwards", allowedDelta, wheelController, testedController);

        // Left stick full left
        wheelController.UpdateWheels(1.0, 0.0, 0.0, 1.0);
        testedController.UpdateWheels(1.0, 0.0, 0.0, 1.0);
        CompareWithTestedController("Move left", allowedDelta, wheelController, testedController);

        // Left stick full right
        wheelController.UpdateWheels(-1.0, 0.0, 0.0, 1.0);
        testedController.UpdateWheels(-1.0, 0.0, 0.0, 1.0);
        CompareWithTestedController("Move right", allowedDelta, wheelController, testedController);

        // Right stick full left
        wheelController.UpdateWheels(0.0, 0.0, -1.0, 1.0);
        testedController.UpdateWheels(0.0, 0.0, -1.0, 1.0);
        CompareWithTestedController("Turn left", allowedDelta, wheelController, testedController);

        // Right stick full right
        wheelController.UpdateWheels(0.0, 0.0, 1.0, 1.0);
        testedController.UpdateWheels(0.0, 0.0, 1.0, 1.0);
        CompareWithTestedController("Turn right", allowedDelta, wheelController, testedController);

        // Random combined input tests
        // I literally just used a random number generator for these.

        // These currently cause the test to fail with an exact delta.
        // They don't need to be as exact because different implementations might output
        // different values that are in somewhat the right range. It's less important that they are
        // exactly the same and more that they are close.
        final double looseDelta = 0.1;

        wheelController.UpdateWheels(0.036, 0.492, 0.418, 1.0);
        testedController.UpdateWheels(0.036, 0.492, 0.418, 1.0);
        CompareWithTestedController("Combined input test #1", looseDelta, wheelController, testedController);

        wheelController.UpdateWheels(0.449, 0.573, 0.527, 1.0);
        testedController.UpdateWheels(0.449, 0.573, 0.527, 1.0);
        CompareWithTestedController("Combined input test #2", looseDelta, wheelController, testedController);

        wheelController.UpdateWheels(0.447, 0.090, 0.193, 1.0);
        testedController.UpdateWheels(0.447, 0.090, 0.193, 1.0);
        CompareWithTestedController("Combined input test #3", looseDelta, wheelController, testedController);

        wheelController.UpdateWheels(0.148, 0.133, 0.784, 1.0);
        testedController.UpdateWheels(0.148, 0.133, 0.784, 1.0);
        CompareWithTestedController("Combined input test #4", looseDelta, wheelController, testedController);
    }

    @Test
    public void MecanumWheels_speedCoefficientWorks()
    {
        MecanumWheelController wheelController = GetInitializedWheelController();

        wheelController.UpdateWheels(1.0, 0.0, 0.0, 1.0);
        Assert.assertEquals(((DcMotorTester)wheelController.FL).Power, 1.0, allowedDelta);

        wheelController.UpdateWheels(1.0, 0.0, 0.0, 0.5);
        Assert.assertEquals(((DcMotorTester)wheelController.FL).Power, 0.5, allowedDelta);
    }
}
