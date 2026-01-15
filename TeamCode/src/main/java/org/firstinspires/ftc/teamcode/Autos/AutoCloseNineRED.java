package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Autonomous(name = "NineCloseRed")
public class AutoCloseNineRED extends LinearOpMode {
    private DcMotor flywheel = null;
    private DcMotor turret = null;
    private DcMotor intake = null;
    private DcMotor transfer = null;
    private Servo actuator = null;
    private Servo LED1 = null;
    private Servo LED2 = null;
    private Servo LED3 = null;

    private static final double GREEN = 0.456;
    private static final double PURPLE = 0.721;
    private static final double OPEN = 0.65;
    private static final double CLOSED = 0.1;
    private static final double HIGH_POWER = 0.66;
    private static final double LOW_POWER = 0.57;
    private static final double INTAKE_SPEED = 1;
    private static final double FAR_SPEED = 4100;
    private static final int FIRST_SHOOT_POSE = 0;
    private static final int SECOND_SHOOT_POSE = 0;
    private double RPM = 0;

    double ticksPerRotation = 25.5;

    private void spinUp()
    {
        flywheel.setPower(HIGH_POWER);
    }
    private boolean shootBall()
    {
        if (RPM <= FAR_SPEED)
        {
            return false;
        }
        actuator.setPosition(OPEN);
        sleep(300);
        actuator.setPosition(CLOSED);
        sleep(300);
        return true;
    }

    private void spinIntake()
    {
        intake.setPower(1);
    }

    private void shootThreeBalls()
    {
        int shootCount = 0;
        int ticks = 0;
        int previousTicks = 0;
        boolean isSuccessful = false;
        while (shootCount < 3)
        {
            previousTicks = flywheel.getCurrentPosition();
            sleep(100);
            ticks = flywheel.getCurrentPosition() - previousTicks;
            RPM = (ticks / ticksPerRotation) * 600;
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
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        actuator = hardwareMap.get(Servo.class, "gate");
        LED1 = hardwareMap.get(Servo.class,"led1");
        LED2 = hardwareMap.get(Servo.class,"led2");
        LED3 = hardwareMap.get(Servo.class,"led3");

        flywheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setTargetPosition(0);
        turret.setPower(0.5);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Pose2d beginPose = new Pose2d(62, 15, Math.PI);
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        TrajectoryActionBuilder shootThree = drive.actionBuilder(beginPose)
                .stopAndAdd(this::turretFirstPos)
                .stopAndAdd(this::spinUp)
                .strafeToLinearHeading(new Vector2d(-11, 14), Math.toRadians(90))
                .stopAndAdd(this::shootThreeBalls)
                .stopAndAdd(this::spinIntake)
                .strafeToLinearHeading(new Vector2d(-11, 54), Math.toRadians(90))
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(-11, 14), Math.toRadians(0))
                .stopAndAdd(this::shootThreeBalls)
                .splineToLinearHeading(new Pose2d(13, 26, Math.toRadians(90)), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(13, 60), Math.toRadians(90))
                .waitSeconds(0.5)
                .setTangent(270)
                .splineToConstantHeading(new Vector2d(13, 40), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(-11, 14), Math.toRadians(90))
                .stopAndAdd(this::shootThreeBalls);
    }
}
