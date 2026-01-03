package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@Autonomous  (name = "AutoFarBLUE")
public class AutoFarBLUE extends LinearOpMode
{
    private DcMotor flywheel = null;
    private Servo actuator = null;
    private double GREEN = 0.456;
    private double PUPLE = 0.721;
    private Servo LED1 = null;
    private Servo LED3 = null;
    private Servo LED2;

    private void spinUp()
    {
        flywheel.setPower(0.6);
    }

    private void launchBall()
    {
        actuator.setPosition(0.65);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        actuator.setPosition(0.1);
    }

    @Override
    public void runOpMode() throws InterruptedException
    {
        DcMotor flywheel;
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        actuator = hardwareMap.get(Servo.class, "actuator");
        LED1 = hardwareMap.get(Servo.class,"LED1");
        LED2 = hardwareMap.get(Servo.class,"LED2");
        LED3 = hardwareMap.get(Servo.class,"LED3");

        Pose2d beginPose = new Pose2d(-11, -62, Math.PI/2);
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);
        TrajectoryActionBuilder shootThree = drive.actionBuilder(drive)
                .stopAndAdd(this::spinUp)
                .strafeToLinearHeading(new Vector2d(-16, -55), Math.toRadians(115));

        Action firstScore = shootThree.build();

        waitForStart();

        Actions.runBlocking(firstScore);
        sleep(1000);
        launchBall();
    }
}
