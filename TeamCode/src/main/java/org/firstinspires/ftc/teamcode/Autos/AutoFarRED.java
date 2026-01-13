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

@Autonomous  (name = "AutoFarBLUE")
public class AutoFarRED extends LinearOpMode
{
    private DcMotor flywheel = null;
    private DcMotor turret = null;
    private DcMotor intake = null;
    private Servo actuator = null;
    private Servo LED1 = null;
    private Servo LED3 = null;
    private Servo LED2;

    private static final double GREEN = 0.456;
    private static final double PURPLE = 0.721;
    private static final double OPEN = 0.65;
    private static final double CLOSED = 0.1;
    private static final double HIGH_POWER = 0.66;
    private static final double LOW_POWER = 0.57;
    private static final double INTAKE_SPEED = 1;
    private static final int SHOOT_POSE = 0;

    private void spinUp()
    {
        flywheel.setPower(HIGH_POWER);
    }
    private void shootThreeBalls()
    {
        actuator.setPosition(OPEN);
        sleep(300);
        actuator.setPosition(CLOSED);
        sleep(300);
        actuator.setPosition(OPEN);
        sleep(300);
        actuator.setPosition(CLOSED);
        sleep(300);
        actuator.setPosition(OPEN);
        sleep(300);
        actuator.setPosition(CLOSED);
    }

    @Override
    public void runOpMode() throws InterruptedException
    {
        turret = hardwareMap.get(DcMotor.class, "turret");
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        actuator = hardwareMap.get(Servo.class, "gate");
        LED1 = hardwareMap.get(Servo.class,"led1");
        LED2 = hardwareMap.get(Servo.class,"led2");
        LED3 = hardwareMap.get(Servo.class,"led3");

        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setTargetPosition(0);
        turret.setPower(0.5);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Pose2d beginPose = new Pose2d(62, 15, Math.PI);
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        TrajectoryActionBuilder shootThree = drive.actionBuilder(beginPose)
                        .lineToX(55);

        TrajectoryActionBuilder intakeThree = drive.actionBuilder(beginPose)
                .splineToLinearHeading(new Pose2d(36, 20, Math.toRadians(90)), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(36, 60, Math.toRadians(90)), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(55, 15), Math.toRadians(180.00));

        Action firstScore = shootThree.build();
        Action firstGrab = intakeThree.build();

        waitForStart();
        spinUp();
        turret.setTargetPosition(SHOOT_POSE);
        sleep(4000);
        Actions.runBlocking(firstScore);
        shootThreeBalls();
        intake.setPower(INTAKE_SPEED);
        Actions.runBlocking(firstGrab);
        shootThreeBalls();



    }
}
