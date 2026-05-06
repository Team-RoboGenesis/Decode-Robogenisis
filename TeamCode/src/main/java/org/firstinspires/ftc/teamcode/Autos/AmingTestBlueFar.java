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
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Tests.Turret;

@Autonomous(name = "AutoAutoAimFarBLUE")
public class AmingTestBlueFar extends LinearOpMode{
    // Motors
    private DcMotorEx flywheel1 = null;
    private DcMotorEx flywheel2 = null;
    private DcMotor intake = null;

    // Turret
    private Turret turret = null;

    // Servos
    private CRServo transfer2 = null;
    private CRServo transfer1 = null;

    // Roadrunner drive
    private MecanumDrive drive = null;

    // Constants
    private static final double INTAKE_SPEED = 1;
    private static final double OFF = 0;
    private static final double FAR_SPEED = 2660;
    private static final double TICKS_PER_ROTATION = 25.5;

    private static final double GOAL_X = -72;
    private static final double GOAL_Y = -68;

    double turretAngle = 0;

    double P = 82;
    double F = 12.3474;

    private double lowVelocity = 1450;
    private double RPM = 0;
    private double offset = 0;

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

    public void shootThreeBalls()
    {
        transfer1.setPower(1);
        transfer2.setPower(1);
        intake.setPower(1);
        sleep(2000);
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

    private void aimTurretAtGoal()
    {
        drive.updatePoseEstimate();
        Pose2d pose = drive.localizer.getPose();

        double robotX = pose.position.x;
        double robotY = pose.position.y;
        double robotHeading = pose.heading.toDouble();

        double dx = GOAL_X - robotX;
        double dy = GOAL_Y - robotY;

        double angleToGoal = Math.atan2(dy, dx);

        turretAngle = angleToGoal - robotHeading;
        turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle)) + offset;

        double maxAngle = Math.toRadians(170);
        double minAngle = Math.toRadians(-170);

        if (turretAngle > maxAngle)
        {
            turretAngle = maxAngle;
        }

        if (turretAngle < minAngle)
        {
            turretAngle = minAngle;
        }

        turret.aimToAngle(turretAngle);

        telemetry.addData("Robot X", robotX);
        telemetry.addData("Robot Y", robotY);
        telemetry.addData("Robot Heading Deg", Math.toDegrees(robotHeading));
        telemetry.addData("Angle To Goal Deg", Math.toDegrees(angleToGoal));
        telemetry.addData("Turret Angle Deg", Math.toDegrees(turretAngle));
        telemetry.update();
    }

    private void turretCenterPos()
    {
        turret.aimToAngle(0);
    }

    private void turretFirstPos()
    {
        turret.aimToAngle(Math.toDegrees(100));
    }


    @Override
    public void runOpMode() throws InterruptedException
    {
        // Motor configuration
        turret = new Turret(hardwareMap);

        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");

        transfer1 = hardwareMap.get(CRServo.class, "servo");
        transfer2 = hardwareMap.get(CRServo.class, "servo1");

        // Motor direction
        transfer1.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        // Flywheel setup
        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);

        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        // Drive constraints
        Pose2d beginPose = new Pose2d(62, -15, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, beginPose);

        // Auto pathing
        TrajectoryActionBuilder shootThree = drive.actionBuilder(beginPose)
                // first shoot
                .stopAndAdd(this::spinUp)
                .stopAndAdd(this::aimTurretAtGoal)
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)
                .stopAndAdd(this::spinIntake)
                .waitSeconds(0.8)

                // first intake
                .strafeToLinearHeading(new Vector2d(63, -62), Math.toRadians(93))
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(62, -15), Math.toRadians(90))

                // second shoot
                .stopAndAdd(this::aimTurretAtGoal)
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)

                // second intake
                .setReversed(true)
                .stopAndAdd(this::spinIntake)
                .splineTo(new Vector2d(39.8, -30), Math.toRadians(-119.83))
                .splineTo(new Vector2d(37, -55), Math.toRadians(-98))
                .strafeToLinearHeading(new Vector2d(62, -15), Math.toRadians(90))

                // third shoot
                .stopAndAdd(this::aimTurretAtGoal)
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)

                // third intake
                .strafeToLinearHeading(new Vector2d(63, -62), Math.toRadians(93))
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(62, -15), Math.toRadians(90))

                // fourth shoot
                .stopAndAdd(this::aimTurretAtGoal)
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)
                ;


        Action auto = shootThree.build();

        telemetry.addLine("Init done");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(auto);
    }
}