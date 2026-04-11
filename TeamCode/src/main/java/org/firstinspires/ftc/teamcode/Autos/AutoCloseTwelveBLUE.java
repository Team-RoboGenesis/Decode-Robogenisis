package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Autonomous(name = "TwelveCloseBLUE")
public class AutoCloseTwelveBLUE extends LinearOpMode
{
    // Motors
    private DcMotorEx flywheel1 = null;
    private DcMotorEx flywheel2 = null;
    private DcMotor turret = null;
    private DcMotor intake = null;

    // Servos
    private CRServo transfer2 = null;
    private CRServo transfer1 = null;

    // Sensors
    private DigitalChannel limiter = null;

    // Constants
    private static final double INTAKE_SPEED = 1;
    private static final double OFF = 0;
    private static final double FAR_SPEED = 2650;
    private static final int FIRST_SHOOT_POSE = -195;
    private static final int SECOND_SHOOT_POSE = -590;
    private static final int CENTER_POSE = 0;
    private static final double TICKS_PER_ROTATION = 25.5;
    double P = 82;
    double F = 12.3474;
    private double lowVelocity = 1200;

    // Non-static variables
    private double RPM = 0;
    private double LOW_POWER = 0.55;

    // Functions:

    private void spinUp()
    {
        flywheel1.setVelocity(lowVelocity);
        flywheel2.setVelocity(lowVelocity);
    }

    public void spinDown()
    {
        flywheel1.setVelocity(OFF);
        flywheel2.setVelocity(OFF);
    }

    public boolean shootBall()
    {
        if (RPM <= FAR_SPEED)
        {
            return false;
        }
        transfer1.setPower(1);
        transfer2.setPower(1);
        intake.setPower(1);
        sleep(500);
        transfer1.setPower(0);
        transfer2.setPower(0);
        intake.setPower(0);
        return true;
    }

    private void shoot()
    {
        transfer1.setPower(1);
        transfer2.setPower(1);
        intake.setPower(1);
        sleep(2500);
        transfer1.setPower(0);
        transfer2.setPower(0);
        intake.setPower(0);
    }

    private void spinIntake()
    {
        intake.setPower(INTAKE_SPEED);
    }

    private void stopIntake()
    {
        intake.setPower(OFF);
    }

    private void turretFirstPos()
    {
        turret.setTargetPosition(FIRST_SHOOT_POSE);
    }

    private void turretSecondPos()
    {
        turret.setTargetPosition(SECOND_SHOOT_POSE);
    }

    private void turretCenterPos()
    {
        turret.setTargetPosition(CENTER_POSE);
    }

    private void shootThreeBalls()
    {
        int shootCount = 0;
        int ticks = 0;
        int previousTicks = 0;
        boolean isSuccessful = false;
        while (shootCount < 3)
        {
            previousTicks = flywheel1.getCurrentPosition();
            sleep(100);
            ticks = flywheel1.getCurrentPosition() - previousTicks;
            RPM = (ticks / TICKS_PER_ROTATION) * 600;
            telemetry.addData("RPM", RPM);
            telemetry.update();
            isSuccessful = shootBall();
            if (isSuccessful)
            {
                shootCount += 1;
            }
        }
    }

    @Override
    public void runOpMode() throws InterruptedException
    {

        // Motor configuration
        turret = hardwareMap.get(DcMotor.class, "turret");
        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer1 = hardwareMap.get(CRServo.class, "servo");
        transfer2 = hardwareMap.get(CRServo.class, "servo1");

        // Motor mode changes
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setTargetPosition(0);
        turret.setPower(0.5);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setDirection(DcMotorSimple.Direction.REVERSE);

        transfer1.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);

        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Drive constraints
        Pose2d beginPose = new Pose2d(-54, -46, Math.toRadians(-127));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        // Auto pathing
        TrajectoryActionBuilder shootThree = drive.actionBuilder(beginPose)
                .stopAndAdd(this::spinUp)
                .stopAndAdd(this::turretFirstPos)
                // First cycle
                .strafeToLinearHeading(new Vector2d(-11, -14), Math.toRadians(185))
                .stopAndAdd(this::spinUp)
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)
                .turn(Math.toRadians(-80))
                .stopAndAdd(this::spinIntake)
                // First intake
                .strafeToLinearHeading(new Vector2d(-7, -50), Math.toRadians(90))
                .waitSeconds(0.3)
                .stopAndAdd(this::stopIntake)
                .stopAndAdd(this::turretFirstPos)
                // Second cycle
                .splineTo(new Vector2d(-5, -14), Math.toRadians(182))
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)
                .stopAndAdd(this::spinIntake)
                // Second intake
                .setReversed(true)
                .splineTo(new Vector2d(16, -20), Math.toRadians(-60))
                .splineTo(new Vector2d(16, -52), Math.toRadians(-90))
                .stopAndAdd(this::stopIntake)
                // Third cycle
                .splineTo(new Vector2d(5, -50), Math.toRadians(90))
                .splineTo(new Vector2d(-5, -14), Math.toRadians(180))
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)
                .stopAndAdd(this::spinIntake)
                //Third intake
                .strafeToLinearHeading(new Vector2d(43, -20), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(43, -53), Math.toRadians(90))
                .stopAndAdd(this::stopIntake)
                // Fourth cycle
                .strafeToLinearHeading(new Vector2d(-11, -14), Math.toRadians(-185))
                .stopAndAdd(this::shootThreeBalls)
                // Prepare the robot for TeleOp by stopping the shooter and resetting the turret position
                .stopAndAdd(this::turretCenterPos)
                .stopAndAdd(this::spinDown)
                .strafeToLinearHeading(new Vector2d(6, -15), Math.toRadians(180))
                .waitSeconds(5);

        // Build the auto to use on play
        Action auto = shootThree.build();

        waitForStart();

        // Run the path declared above
        Actions.runBlocking(auto);
    }
}
