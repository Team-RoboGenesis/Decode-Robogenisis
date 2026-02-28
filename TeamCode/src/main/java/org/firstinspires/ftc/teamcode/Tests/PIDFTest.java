package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

//@TeleOp
public class PIDFTest extends OpMode {

    private DcMotorEx flywheel1;
    private DcMotorEx flywheel2;

    private DcMotor intake = null;
    private CRServo actuator1 = null;
    private CRServo actuator2 = null;


    private double highVelocity = 1500;
    private double lowVelocity = 900;
    double curTargetVelocity = highVelocity;
    double P = 82.5;
    double F = 12.3474;
    double[] stepSizes = {10.0, 1.0, 0.1, 0.01, 0.001, 0.0001};
    int stepIndex = 1;

    @Override
    public void init() {
        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");

        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);

        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        telemetry.addLine("Init done");
    }

    @Override
    public void loop() {
        if (gamepad1.yWasPressed()) {
            if (curTargetVelocity == highVelocity) {
                curTargetVelocity = lowVelocity;
            } else curTargetVelocity = highVelocity;
        }

        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (gamepad1.dpadLeftWasPressed()) {
            F += stepSizes[stepIndex];
        }

        if (gamepad1.dpadRightWasPressed()) {
            F -= stepSizes[stepIndex];
        }

        if (gamepad1.dpadDownWasPressed()) {
            P -= stepSizes[stepIndex];
        }

        if (gamepad1.dpadUpWasPressed()) {
            P += stepSizes[stepIndex];
        }

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        flywheel1.setVelocity(curTargetVelocity);
        flywheel2.setVelocity(curTargetVelocity);

        double curVelocity = flywheel1.getVelocity();
        double error = curTargetVelocity - curVelocity;

        if (gamepad1.right_trigger > 0.1)
        {
            intake.setPower(1);
            actuator1.setPower(-1);
            actuator2.setPower(1);
        }
        else if (gamepad1.left_trigger > 0.1)
        {
            intake.setPower(-1);
            actuator1.setPower(1);
            actuator2.setPower(-1);
        }
        else if (gamepad1.right_trigger < 0.1 && gamepad1.left_trigger < 0.1)
        {
            intake.setPower(0);
            actuator1.setPower(0);
            actuator2.setPower(0);
        }

        telemetry.addData("Target Velocity: ", "%,4f", curTargetVelocity);
        telemetry.addData("Current Velocity: ", "%,4f", curVelocity);
        telemetry.addData("Error: ", "%,2f", error);
        telemetry.addLine("========================================");
        telemetry.addData("Tuning P: ", "%,4f (D_Pad U/D)", P);
        telemetry.addData("Tuning F: ", "%,4f (D_Pad L/R)", F);
        telemetry.addData("Step size: ", "%,4f (B Button)", stepSizes[stepIndex]);

    }
}
