package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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

    @Override
    public void runOpMode() throws InterruptedException
    {
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        actuator = hardwareMap.get(Servo.class, "gate");
        LED1 = hardwareMap.get(Servo.class,"led1");
        LED2 = hardwareMap.get(Servo.class,"led2");
        LED3 = hardwareMap.get(Servo.class,"led3");

        actuator.setPosition(0.1);
        actuator.setPosition(0.65);

        Pose2d beginPose = new Pose2d(0, -63, 90);
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Actions.runBlocking(
                drive.actionBuilder(beginPose)
                        .splineToLinearHeading(new Pose2d(-3.96, -52.30, Math.toRadians(140.00)), Math.toRadians(140.97))
                        .build());



        flywheel.setPower(0.7);
        sleep(4000);
        actuator.setPosition(0.1);
        flywheel.setPower(0);
//        sleep(300);
//        actuator.setPosition(0.1);
//        spinUp();
//        sleep(1000);
//        launchBall();
    }
}
