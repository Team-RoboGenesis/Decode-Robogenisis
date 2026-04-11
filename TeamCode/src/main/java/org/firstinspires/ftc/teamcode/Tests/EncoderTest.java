package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "RPM")
public class EncoderTest extends LinearOpMode
{
    private DcMotor flywheel1 = null;
    private DcMotorEx flywheel2;
    private DcMotor intake = null;
    private CRServo actuator1 = null;
    private CRServo actuator2 = null;

    double ticksPerRotation = 25.5;
    private double HIGH_POWER = 0.95;
    private double LOW_POWER = 0.8;
    private double MEDIUM_POWER = 0.9;
    private double OFF = 0;

    private double OPEN = 0.65;
    private double CLOSED = 0.1;

    @Override
    public void runOpMode() throws InterruptedException
    {
        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");

        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);
        actuator1.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad2.cross)
            {
                flywheel1.setPower(LOW_POWER);
            }
            else if (gamepad2.circle)
            {
                flywheel1.setPower(MEDIUM_POWER);
            }
            else if (gamepad2.triangle)
            {
                flywheel1.setPower(HIGH_POWER);
            }
            else if (gamepad2.square)
            {
                flywheel1.setPower(OFF);
            }

            int previousTicks = flywheel1.getCurrentPosition();
            Thread.sleep(100);
            int ticks = flywheel1.getCurrentPosition() - previousTicks;
            double RPM = (ticks/ticksPerRotation) * 600;
            telemetry.addData("RPM: ", RPM);
            telemetry.addData("Encoder ticks: ", flywheel1.getCurrentPosition());
            telemetry.update();
        }
    }
}
