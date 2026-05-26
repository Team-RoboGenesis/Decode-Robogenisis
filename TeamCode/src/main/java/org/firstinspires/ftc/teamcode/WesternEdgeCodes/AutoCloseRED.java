package org.firstinspires.ftc.teamcode.WesternEdgeCodes;

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
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;

@Autonomous(name = "AutoCloseRED")
public class AutoCloseRED extends LinearOpMode
{
    // Motors
    private DcMotorEx flywheel1 = null;
    private DcMotorEx flywheel2 = null;
    private Intake intake;
    private Turret turret;
    // Servos
    private CRServo transfer2 = null;
    private CRServo transfer1 = null;

    // Roadrunner drive
    private MecanumDrive drive = null;

    // Constants
    private static final double INTAKE_SPEED = 1;
    private static final double OFF = 0;
    private static final double FAR_SPEED = 2650;
    private static final double TICKS_PER_ROTATION = 25.5;

    private static final double GOAL_X = -65;
    private static final double GOAL_Y = 72;

    double turretAngle = 0;

    double P = 82;
    double F = 12.3474;

    private double lowVelocity = 1170;
    private double lowerVelocity = 1150;
    private double RPM = 0;
    private double offset = 0;

    private void spinUp()
    {
        flywheel1.setVelocity(lowVelocity);
        flywheel2.setVelocity(lowVelocity);
    }

    private void slowDown()
    {
        flywheel1.setVelocity(lowerVelocity);
        flywheel2.setVelocity(lowerVelocity);
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
        sleep(1750);
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
        intake = new Intake(hardwareMap);

        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");

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

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);

        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        // Drive constraints
        Pose2d beginPose = new Pose2d(-54, 46, Math.toRadians(127));
        drive = new MecanumDrive(hardwareMap, beginPose);

        // Auto pathing
        TrajectoryActionBuilder shootThree = drive.actionBuilder(beginPose)
                .stopAndAdd(this::spinUp)
//                .stopAndAdd(this::turretFirstPos)

                // First cycle
                .strafeToLinearHeading(new Vector2d(-11, 16), Math.toRadians(-150))
                .stopAndAdd(this::spinUp)
                .stopAndAdd(this::aimTurretAtGoal)
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)

                // First intake
                .stopAndAdd(this::spinIntake)
                .setReversed(true)
                .splineTo(new Vector2d(16, 20), Math.toRadians(80))
                .splineTo(new Vector2d(16, 54), Math.toRadians(90))
                .waitSeconds(0.3)
                .stopAndAdd(this::stopIntake)

                // Second cycle
                .splineTo(new Vector2d(-11, 18), Math.toRadians(-185))
                .stopAndAdd(this::aimTurretAtGoal)
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::aimTurretAtGoal)
                .stopAndAdd(this::shootThreeBalls)
                .stopAndAdd(this::spinIntake)

                // Second intake
                .setReversed(true)
                .stopAndAdd(this::spinIntake)
                .strafeToLinearHeading(new Vector2d(16.5, 51), Math.toRadians(-73))
                .waitSeconds(1.5)
                .stopAndAdd(this::stopIntake)

                // Third cycle
                .strafeToLinearHeading(new Vector2d(-11, 18), Math.toRadians(-180))
                .stopAndAdd(this::aimTurretAtGoal)
                .stopAndAdd(this::shootThreeBalls)

                // Third intake
                .setReversed(true)
                .stopAndAdd(this::spinIntake)
                .strafeToLinearHeading(new Vector2d(16.5, 52), Math.toRadians(-73))
                .waitSeconds(1.5)
                .stopAndAdd(this::stopIntake)

                // Fourth cycle
                .strafeToLinearHeading(new Vector2d(-11, 18), Math.toRadians(-150))
                .stopAndAdd(this::aimTurretAtGoal)
                .stopAndAdd(this::shootThreeBalls)

                // Fourth intake
                .stopAndAdd(this::spinIntake)
                .strafeToLinearHeading(new Vector2d(-10, 47), Math.toRadians(-90))
                .waitSeconds(0.4)
                .stopAndAdd(this::stopIntake)

                // Fifth cycle
                .strafeToLinearHeading(new Vector2d(-11, 18), Math.toRadians(-130))
                .stopAndAdd(this::aimTurretAtGoal)
                .stopAndAdd(this::shootThreeBalls)

//                // Prepare the robot for TeleOp
                .stopAndAdd(this::turretCenterPos)
                .stopAndAdd(this::spinDown)
                .strafeToLinearHeading(new Vector2d(6, 15), Math.toRadians(-180))
                .waitSeconds(5);

        Action auto = shootThree.build();

        telemetry.addLine("Init done");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(auto);
    }
}