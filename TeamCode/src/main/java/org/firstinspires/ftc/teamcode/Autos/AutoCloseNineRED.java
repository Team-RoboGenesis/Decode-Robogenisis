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
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Autonomous(name = "NineCloseRed")
public class AutoCloseNineRED extends LinearOpMode {
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
    private static final double HIGH_POWER = 0.66;
    private static final double LOW_POWER = 0.57;
    private static final double INTAKE_SPEED = 1;
    private static final double OFF = 0;
    private static final double FAR_SPEED = 4100;
    private static final int FIRST_SHOOT_POSE = 0;
    private static final int SECOND_SHOOT_POSE = 0;
    private static final double ticksPerRotation = 25.5;
    private double RPM = 0;

    private void spinUp()
    {
        flywheel1.setPower(HIGH_POWER);
    }
    private boolean shootBall()
    {
        if (RPM <= FAR_SPEED)
        {
            return false;
        }
        transfer1.setPower(1);
        transfer2.setPower(1);
        intake.setPower(INTAKE_SPEED);
        sleep(300);
        transfer1.setPower(0);
        transfer1.setPower(0);
        intake.setPower(OFF);
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

        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setTargetPosition(0);
        turret.setPower(0.5);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Pose2d beginPose = new Pose2d(-54, 46, Math.toRadians(-127));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        TrajectoryActionBuilder shootThree = drive.actionBuilder(beginPose)
                .stopAndAdd(this::turretFirstPos)
                .stopAndAdd(this::spinUp)
                .strafeToLinearHeading(new Vector2d(-11, 14), Math.toRadians(0))
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)
                .turn(Math.toRadians(80))
                .strafeToLinearHeading(new Vector2d(-11, 54), Math.toRadians(90))
                .waitSeconds(0.5)
                .stopAndAdd(this::stopIntake)
                .strafeToLinearHeading(new Vector2d(-11, 14), Math.toRadians(0))
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls)
                .strafeToLinearHeading(new Vector2d(13, 23), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(16, 60), Math.toRadians(90))
                .waitSeconds(0.5)
                .stopAndAdd(this::stopIntake)
                .strafeToLinearHeading(new Vector2d(-11, 14), Math.toRadians(0))
                .stopAndAdd(this::spinIntake)
                .stopAndAdd(this::shootThreeBalls);

        Action auto = shootThree.build();

        waitForStart();

        Actions.runBlocking(auto);
    }
}
