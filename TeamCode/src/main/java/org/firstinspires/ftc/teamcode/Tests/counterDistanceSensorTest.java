package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

//@TeleOp(name = "counterDistanceSensorTest")
public class counterDistanceSensorTest extends LinearOpMode {
    private DcMotorEx flywheel1;
    private DcMotorEx flywheel2;
    private final double flywheelP = 82;
    private final double flywheelF = 12.3474;
    private final double highVelocity = 1550;
    private final double lowVelocity = 1230;
    private double curTargetVelocity = lowVelocity;
    private CRServo actuator1;
    private CRServo actuator2;
    private DistanceSensor distanceSensor = null;
    private DistanceSensor distSensor = null;
    private Intake intake = null;
    private double thresholdCmA = 14.0;
    private double thresholdCmB = 10.0;
    private boolean lastBrokenA = false;
    private boolean lastBrokenB = false;
    private int count = 0;
    private double distA = 0;
    private double distB = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "distance");
        distSensor = hardwareMap.get(DistanceSensor.class, "dist");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");
        actuator1.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(flywheelP, 0, 0, flywheelF);
        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        // Flywheel error tracking
        double curVelocity = flywheel1.getVelocity();
        double velocityError = curTargetVelocity - curVelocity;
        intake = new Intake(hardwareMap);
        waitForStart();
        while(opModeIsActive()){
            flywheel1.setVelocity(curTargetVelocity);
            flywheel2.setVelocity(curTargetVelocity);
            // Flywheel speed control
            if (gamepad2.x) {
                curTargetVelocity = 0;
            }

            if (gamepad2.b) {
                while (opModeIsActive() && gamepad2.b) {
                    idle();
                }
                if (curTargetVelocity == highVelocity) {
                    curTargetVelocity = lowVelocity;
                } else {
                    curTargetVelocity = highVelocity;
                }
            }
            distA = distanceSensor.getDistance(DistanceUnit.CM);
            telemetry.addData("dist", distanceSensor.getDistance(DistanceUnit.CM));
            // Intake control
            if (gamepad2.right_trigger > 0.1) {
                intake.setPower(1);
            } else if (gamepad2.left_trigger > 0.1) {
                intake.setPower(-1);
            } else {
                intake.setPower(0);
            }
            // Launching control
            if (gamepad2.dpad_down) {
                actuator1.setPower(-1);
                actuator2.setPower(-1);
            } else if (gamepad2.dpad_up) {
                actuator1.setPower(1);
                actuator2.setPower(1);
            } else {
                actuator1.setPower(0);
                actuator2.setPower(0);
            }
            distB = distSensor.getDistance(DistanceUnit.CM);
            boolean brokenA = distA < thresholdCmA;
            boolean brokenB = distB < thresholdCmB;

            if (brokenA && !lastBrokenA) {
                count++;
                lastBrokenA = true;
            } else if (!brokenA && lastBrokenA) {
                lastBrokenA = false;
            }

            if (brokenB && !lastBrokenB) {
                lastBrokenB = true;
            } else if (!brokenB && lastBrokenB) {
                count--;
                lastBrokenB = false;
            }
            if(count>3) count = 3;
            if(count<0)count = 0;
            if (gamepad1.a) {
                count = 0;
            }
            telemetry.addData("count", count);
            telemetry.addData("dist", distA);
            telemetry.update();
        }
    }
}
