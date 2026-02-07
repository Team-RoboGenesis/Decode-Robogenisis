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

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@Autonomous(name = "NineCloseBlue")
public class AutoCloseNineBLUE extends LinearOpMode {
    private DcMotor flywheel1 = null;
    private DcMotor flywheel2 = null;
    private DcMotor turret = null;
    private DcMotor intake = null;
    private CRServo transfer2 = null;
    private CRServo transfer1 = null;

    // Constants
    private static final double LOW_POWER = 0.53;
    private static final double INTAKE_SPEED = 1;
    private static final double OFF = 0;
    private static final double FAR_SPEED = 2800;
    private static final int FIRST_SHOOT_POSE = -180;
    private static final int CENTER_POSE = 0;
    private static final double TICKS_PER_ROTATION = 25.5;

    // Non-static variables
    private double RPM = 0;

    // Functions:

    private void spinUp()
    {
        flywheel1.setPower(LOW_POWER);
        flywheel2.setPower(LOW_POWER);
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

    private void turretFirstPos()
    {
        turret.setTargetPosition(FIRST_SHOOT_POSE);
    }

    private void turretCenterPos()
    {
        turret.setTargetPosition(CENTER_POSE);
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
            RPM = (ticks / TICKS_PER_ROTATION) * 600;
            telemetry.addData("RPM", RPM);
            telemetry.update();
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

        // Motor configuration
        turret = hardwareMap.get(DcMotor.class, "turret");
        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer1 = hardwareMap.get(CRServo.class, "servo");
        transfer2 = hardwareMap.get(CRServo.class, "servo1");

        // Motor mode changes
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

        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Drive constraints
        Pose2d beginPose = new Pose2d(-54, -46, Math.toRadians(-127));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        // Auto pathing
        TrajectoryActionBuilder shootThree = drive.actionBuilder(beginPose)
                .stopAndAdd(this::spinUp)
                .stopAndAdd(this::turretFirstPos)
                // Drive back to shoot
                .strafeToLinearHeading(new Vector2d(-11, -14), Math.toRadians(180))
                .stopAndAdd(this::spinUp)
                // Score three preloaded balls
                .stopAndAdd(this::shootThreeBalls)
                // Turn towards spike mark artifacts
                .turn(Math.toRadians(-80))
                .stopAndAdd(this::spinIntake)
                // Intake three balls
                .strafeToLinearHeading(new Vector2d(-8, -48), Math.toRadians(90))
                .waitSeconds(0.3)
                .stopAndAdd(this::stopIntake)
                // Drive to shooting position
                .strafeToLinearHeading(new Vector2d(-5, -14), Math.toRadians(190))
                .stopAndAdd(this::spinIntake)
                // Shoot three balls
                .stopAndAdd(this::shootThreeBalls)
                // Move to next three balls
                .strafeToLinearHeading(new Vector2d(20, -14), Math.toRadians(90))
                .stopAndAdd(this::spinIntake)
                // Intake three balls
                .strafeToLinearHeading(new Vector2d(20, -55), Math.toRadians(90))
                .waitSeconds(0.3)
                .stopAndAdd(this::stopIntake)
                // Move back to shooting position
                .strafeToLinearHeading(new Vector2d(-5, -14), Math.toRadians(185))
                .stopAndAdd(this::spinIntake)
                // Shoot three balls
                .stopAndAdd(this::shootThreeBalls)
                // Prepare the robot for TeleOp by stopping the shooter and resetting the turret position
                .stopAndAdd(this::spinDown)
                .stopAndAdd(this::turretCenterPos)
                .strafeToLinearHeading(new Vector2d(3, -14), Math.toRadians(185))
                .waitSeconds(5);

        // Build the auto to use on play
        Action auto = shootThree.build();

        waitForStart();

        // Run the path declared above
        Actions.runBlocking(auto);
    }
}
