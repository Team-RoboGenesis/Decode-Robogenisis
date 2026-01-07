package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous  (name = "PlayAuto")
public class FunAutoPlay extends LinearOpMode
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
        actuator = hardwareMap.get(Servo.class, "gate");
        LED1 = hardwareMap.get(Servo.class,"led1");
        LED2 = hardwareMap.get(Servo.class,"led2");
        LED3 = hardwareMap.get(Servo.class,"led3");

        Pose2d beginPose = new Pose2d(-0, -63, Math.PI/2);
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Actions.runBlocking(
                drive.actionBuilder(beginPose)
                        .splineTo(new Vector2d(-3.55, 43.36), Math.toRadians(69.74))
                        .splineTo(new Vector2d(34.02, -34.63), Math.toRadians(-64.28))
                        .splineTo(new Vector2d(-52.30, 20.01), Math.toRadians(147.67))
                        .splineTo(new Vector2d(-27.32, 22.65), Math.toRadians(-8.77))
                        .splineTo(new Vector2d(51.08, 15.33), Math.toRadians(-2.59))
                        .splineTo(new Vector2d(42.96, -2.94), Math.toRadians(208.82))
                        .splineTo(new Vector2d(-37.88, -47.42), Math.toRadians(218.71))
                        .build());

//        spinUp();
//        sleep(1000);
//        launchBall();
    }
}
