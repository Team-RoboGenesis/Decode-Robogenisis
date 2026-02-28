package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Tests.Turret;

@TeleOp(name = "TeleOpRED")
public class AutoAimTeleOpRED extends LinearOpMode {

    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private DcMotorEx flywheel1;
    private DcMotorEx flywheel2;
    private DcMotor intake = null;
    private CRServo actuator1 = null;
    private CRServo actuator2 = null;
    private final Servo led1 = null;
    private final Servo led2 = null;
    private final Servo led3 = null;
    Limelight3A limelight = null;

    private static final double GREEN = 0.456;
    private static final double PURPLE = 0.721;
    private static final double OFF = 0;
    private static final double FAR_SPEED = 3300;
    private static final int SHOOT_POSE = 0;
    private final static int CONVERT_TO_MINUTE = 600;
    private static final double ticksPerRotation = 25.5;
    private static final int APRIL_TAG_PIPELINE = 0;
    double P = 82;
    double F = 12.3474;
    private final double highVelocity = 1550;
    private final double lowVelocity = 1230;
    double curTargetVelocity = lowVelocity;
    private double RPM = 0;
    private final double pos = 0;
    private double distanceInches = 0;
    boolean manual = true;
    double goalY = 72;
    double goalX = -72;
    double startY = -62;
    double startX = 62;
    double offset = 0;

    private boolean shootBall()
    {
        if (RPM <= FAR_SPEED)
        {
            return false;
        }
        intake.setPower(1);
        actuator1.setPower(-1);
        actuator2.setPower(1);
        sleep(1000);
        actuator1.setPower(0);
        actuator2.setPower(0);
        intake.setPower(0);
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
            previousTicks = flywheel1.getCurrentPosition();
            sleep(100);
            ticks = flywheel1.getCurrentPosition() - previousTicks;
            RPM = (ticks / ticksPerRotation) * CONVERT_TO_MINUTE;
            isSuccessful = shootBall();
            if (isSuccessful)
            {
                shootCount += 1;
            }
            telemetry.addData("RPM", RPM);
            telemetry.update();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");

        limelight = hardwareMap.get(Limelight3A.class, "Benny");

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(3, -14, Math.toRadians(0)));
        Turret turret = new Turret(hardwareMap);

        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(APRIL_TAG_PIPELINE);

        // Sometimes we have to reverse the motors because they aren't
        // rotating correctly. So we do that here

        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);

        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        telemetry.addLine("Init done");

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        actuator1.setDirection(DcMotorSimple.Direction.REVERSE);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);
        imu.resetYaw();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive())
        {
            // Roadrunner pos tracking
            drive.updatePoseEstimate();
            Pose2d pose = drive.localizer.getPose();

            double robotX = pose.position.x;
            double robotY = pose.position.y;
            double robotHeading = pose.heading.toDouble();

            // Goal pos
            double targetX = goalX;
            double targetY = goalY;

            // Angle from robot to corner (field frame)
            double dx = targetX - robotX;
            double dy = targetY - robotY;
            double angleToCorner = Math.atan2(dy, dx);

            // Convert to robot-relative turret angle
            double turretAngle = angleToCorner - robotHeading;
            turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle)) + offset;

            // Limits to restrict turret to 180 degrees in either direction
            double maxAngle = Math.toRadians(180);
            double minAngle = Math.toRadians(-180);

            if (turretAngle > maxAngle) turretAngle = maxAngle;
            if (turretAngle < minAngle) turretAngle = minAngle;

            int turretPos = (int) (turret.getCurrentPosition() - gamepad2.left_stick_x*40);

            // Automatic turret control
            if (!manual)
            {
                turret.aimToAngle(turretAngle);
            }

            // Manual turret control
            if (manual)
            {
                turret.setTargetPosition(turretPos );
            }

            if (gamepad2.leftStickButtonWasPressed())
            {
                offset += 0.05;
            }

            if (gamepad2.rightStickButtonWasPressed())
            {
                offset -= 0.05;
            }

            // Switch between auto and manual aim
            if (gamepad2.leftBumperWasPressed()) {
                manual = !manual;
            }

            telemetry.addData("X", robotX);
            telemetry.addData("Y", robotY);
            telemetry.addData("RobotHeadingDeg", Math.toDegrees(robotHeading));
            telemetry.addData("AngleToCornerDeg", Math.toDegrees(angleToCorner));
            telemetry.addData("TurretAngleDeg", Math.toDegrees(turretAngle));

            // Drive variables
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            if (gamepad1.options)
            {
                imu.resetYaw();
                drive = new MecanumDrive(hardwareMap, new Pose2d(startX, startY, Math.toRadians(90)));
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            leftFront.setPower(frontLeftPower);
            leftBack.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightBack.setPower(backRightPower);

            limelight.start();
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                for (LLResultTypes.FiducialResult fid : result.getFiducialResults()) {
                    Pose3D camToTag = fid.getCameraPoseTargetSpace();
                    double xTarget = camToTag.getPosition().x;
                    double yTarget = camToTag.getPosition().y;
                    double zTarget = camToTag.getPosition().z;
                    double distanceMeters = Math.sqrt(xTarget * xTarget + yTarget * yTarget + zTarget * zTarget);
                    distanceInches = DistanceUnit.INCH.fromMeters(distanceMeters);
                }
            }

            double curVelocity = flywheel1.getVelocity();
            double error = curTargetVelocity - curVelocity;

            telemetry.addData("offset: ", offset);
            telemetry.addData("Inches: ", distanceInches);
            telemetry.addData("YAW: ", imu.getRobotYawPitchRollAngles().getYaw());
            telemetry.addData("Target X: ", result.getTx());
            telemetry.addData("RPM", RPM);
            telemetry.addData("Pos: ", pos);
            telemetry.addData("Target Velocity: ", "%,4f", curTargetVelocity);
            telemetry.addData("Current Velocity: ", "%,4f", curVelocity);
            telemetry.addData("Error: ", "%,2f", error);
            telemetry.addLine("========================================");
            telemetry.update();

            flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

            flywheel1.setVelocity(curTargetVelocity);
            flywheel2.setVelocity(curTargetVelocity);

            if (gamepad2.dpad_down)
            {
                actuator1.setPower(-1);
                actuator2.setPower(-1);
            }
            else if (gamepad2.dpad_up)
            {
                actuator1.setPower(1);
                actuator2.setPower(1);
            }
            else if (!gamepad2.dpad_down && !gamepad2.dpad_up)
            {
                actuator1.setPower(0);
                actuator2.setPower(0);
            }

            if (gamepad2.x)
            {
                curTargetVelocity = OFF;
            }

            if (gamepad2.right_trigger > 0.1)
            {
                intake.setPower(1);
            }
            else if (gamepad2.left_trigger > 0.1)
            {
                intake.setPower(-1);
            }
            else if (gamepad2.right_trigger < 0.1 && gamepad2.left_trigger < 0.1)
            {
                intake.setPower(0);
            }
            if (gamepad2.bWasPressed()) {
                if (curTargetVelocity == highVelocity) {
                    curTargetVelocity = lowVelocity;
                } else curTargetVelocity = highVelocity;
            }
        }
    }
}