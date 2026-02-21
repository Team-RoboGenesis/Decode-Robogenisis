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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Autonomous(name = "SixFarBlue")
public class AutoFarSixBLUE extends LinearOpMode {
    private DcMotor flywheel1 = null;
    private DcMotor flywheel2 = null;
    private DcMotor turret = null;
    private DcMotor intake = null;
    private CRServo transfer2 = null;
    private CRServo transfer1 = null;

    private static final double GREEN = 0.456;
    private static final double PURPLE = 0.721;
    private static final double OPEN = 0.65;
    private static final double CLOSED = 0.1;
    private static final double HIGH_POWER = 0.62;
    private static final double LOW_POWER = 0.57;
    private static final double INTAKE_SPEED = 1;
    private static final double OFF = 0;
    private static final double FAR_SPEED = 3000;
    private static final int FIRST_SHOOT_POSE = -90;
    private static final int SECOND_SHOOT_POSE = 0;
    private double RPM = 0;

    double ticksPerRotation = 25.5;

    private void spinUp()
    {
        flywheel1.setPower(HIGH_POWER);
        flywheel2.setPower(HIGH_POWER);
    }

    private void spinDown()
    {
        flywheel1.setPower(OFF);
        flywheel2.setPower(OFF);
    }
    private boolean shootBall()
    {
        if (RPM <= FAR_SPEED)
        {
            return false;
        }
        transfer1.setPower(-1);
        transfer2.setPower(-1);
        intake.setPower(1);
        sleep(1000);
        transfer1.setPower(0);
        transfer1.setPower(0);
        intake.setPower(0);
        sleep(300);
        return true;
    }

    private void spinIntake()
    {
        intake.setPower(INTAKE_SPEED);
    }

    private void stopIntake()
    {
        intake.setPower(OFF);
    }

    private void slowDown()
    {
        flywheel1.setPower(0.6);
        flywheel2.setPower(0.6);
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
            RPM = (ticks / ticksPerRotation) * 600;
            telemetry.addData("RPM", RPM);
            telemetry.update();
            isSuccessful = shootBall();
            if (isSuccessful)
            {
                shootCount += 1;
            }
        }
    }

    private void turretFirstPos()
    {
        turret.setTargetPosition(FIRST_SHOOT_POSE);
    }

    @Override
    public void runOpMode() throws InterruptedException
    {
        turret = hardwareMap.get(DcMotor.class, "turret");
        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer1 = hardwareMap.get(CRServo.class, "servo");
        transfer2 = hardwareMap.get(CRServo.class, "servo1");

        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setTargetPosition(0);
        turret.setPower(0.5);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setDirection(DcMotorSimple.Direction.REVERSE);

        transfer1.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        Pose2d beginPose = new Pose2d(62, -15, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        TrajectoryActionBuilder shootThree = drive.actionBuilder(beginPose)
                .strafeToLinearHeading(new Vector2d(55, -15), Math.toRadians(180))
                .stopAndAdd(this::spinUp)
                .stopAndAdd(this::turretFirstPos)
                .stopAndAdd(this::shootThreeBalls)
                .stopAndAdd(this::slowDown)
                .strafeToLinearHeading(new Vector2d(63, -30), Math.toRadians(90))
                .stopAndAdd(this::spinIntake)
                .strafeToLinearHeading(new Vector2d(64, -63), Math.toRadians(100))
                .waitSeconds(1)
                .stopAndAdd(this::stopIntake)
                .strafeToLinearHeading(new Vector2d(58, -30), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(59, -13), Math.toRadians(-175))
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)
                .stopAndAdd(this::spinDown)
                .strafeToLinearHeading(new Vector2d(64, -63), Math.toRadians(90))
                .waitSeconds(5);


        Action auto = shootThree.build();

        waitForStart();

        Actions.runBlocking(auto);
    }
}
