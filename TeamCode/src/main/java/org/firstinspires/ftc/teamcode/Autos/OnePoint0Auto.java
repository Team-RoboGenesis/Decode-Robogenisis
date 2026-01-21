package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Autonomous  (name = "Relic Auton")
public class OnePoint0Auto extends LinearOpMode
{
    private DcMotor flywheel = null;
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
    private static final double FAR_SPEED = 3620;
    private static final int SHOOT_POSE = 0;
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

    private void shootThreeBalls()
    {
        int shootCount = 0;
        int ticks = 0;
        int previousTicks = 0;
        boolean isSuccessful = false;
        while (shootCount <= 3)
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

    @Override
    public void runOpMode() throws InterruptedException
    {
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        actuator = hardwareMap.get(Servo.class, "gate");
        LED1 = hardwareMap.get(Servo.class,"led1");
        LED2 = hardwareMap.get(Servo.class,"led2");
        LED3 = hardwareMap.get(Servo.class,"led3");

        flywheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Pose2d beginPose = new Pose2d(62, 15, Math.PI);
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        TrajectoryActionBuilder shootThree = drive.actionBuilder(beginPose)
                .strafeToLinearHeading(new Vector2d(55, 15), Math.toRadians(158.50));

        TrajectoryActionBuilder intakeThree = drive.actionBuilder(beginPose)
                .splineToLinearHeading(new Pose2d(30, 20, Math.toRadians(90)), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(30, 60, Math.toRadians(90)), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(55, 15), Math.toRadians(162.00));

        Action firstScore = shootThree.build();
        Action firstGrab = intakeThree.build();

        waitForStart();


        spinUp();
        Actions.runBlocking(firstScore);
        shootThreeBalls();
        Actions.runBlocking(firstGrab);
        shootThreeBalls();



    }
}
