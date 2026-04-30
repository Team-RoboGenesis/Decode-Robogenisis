package org.firstinspires.ftc.teamcode.TeleOps;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
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
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Tests.Turret;

@TeleOp(name = "VeloTeleOpBLUE")
public class VelocityAimTeleOpBLUE extends LinearOpMode {

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotorEx flywheel1;
    private DcMotorEx flywheel2;
    private DcMotor intake;
    private CRServo actuator1;
    private CRServo actuator2;
    private DistanceSensor distSensor;
    private Servo led1;
    private Servo led2;
    private Servo led3;
    private Servo led4;
    private Limelight3A limelight;

    // LED control
    private static final double WHITE = 0.9;
    private static final double GREEN = 0.456;
    private static final double OFF = 0.0;
    private static final double ORANGE = 0.28;

    private static final int APRIL_TAG_PIPELINE = 0;

    // Flywheel PIDf
    private final double flywheelP = 82;
    private final double flywheelF = 12.3474;

    // Flywheel speed control
    private final double highVelocity = 1550;
    private final double lowVelocity = 1230;
    private double curTargetVelocity = lowVelocity;

    //
    private double distanceInches = 0.0;

    private boolean manual = true;

    // Aiming constants
    double goalY = 72;
    double goalX = 72;
    double startY = -62;
    double startX = -62;
    private double offset = 0.0;

    // Counting logic
    double thresholdCm = 14.0;
    private boolean lastBroken = false;
    private boolean lastCheck = false;
    private int count = -1;

    // Turret PID
    private double turretKp = 4.0;
    private double turretKi = 0.0;
    private double turretKd = 0.15;
    private double turretIntegral = 0.0;
    private double turretLastError = 0.0;
    private long turretLastTimeNanos = 0L;

    // Vision correction
    private double kVision = 0;

    // Lead-shot tuning
    private double projectileSpeed = 360;
    private double releaseDelay = 0.35;

    // Aim smoothing
    private double filteredTurretTarget = 0.0;
    private double aimAlpha = 0.4;

    // PID guards
    private double turretMaxPower = 0.75;
    private double integralClamp = 0.5;

    public void zero() {
        led1.setPosition(OFF);
        led2.setPosition(OFF);
        led3.setPosition(OFF);
    }

    public void one() {
        led1.setPosition(WHITE);
        led2.setPosition(OFF);
        led3.setPosition(OFF);
    }

    public void two() {
        led1.setPosition(WHITE);
        led2.setPosition(WHITE);
        led3.setPosition(OFF);
    }


    public void three() {
        led1.setPosition(WHITE);
        led2.setPosition(WHITE);
        led3.setPosition(WHITE);
    }

    public void resetCount() {
        count = 0;
    }

    private double wrapAngle(double angle) {
        return Math.atan2(Math.sin(angle), Math.cos(angle));
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");
        distSensor = hardwareMap.get(DistanceSensor.class, "dist");
        led1 = hardwareMap.get(Servo.class, "led1");
        led2 = hardwareMap.get(Servo.class, "led2");
        led3 = hardwareMap.get(Servo.class, "led3");
        led4 = hardwareMap.get(Servo.class, "led4");
        limelight = hardwareMap.get(Limelight3A.class, "Benny");

        MecanumDrive drive = new MecanumDrive(
                hardwareMap,
                new Pose2d(3, -14, Math.toRadians(185))
        );

        Turret turret = new Turret(hardwareMap);
        turret.useRawPowerMode();
        turretLastTimeNanos = System.nanoTime();

        telemetry.setMsTransmissionInterval(11);

        limelight.pipelineSwitch(APRIL_TAG_PIPELINE);
        limelight.start();

        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);
        actuator1.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(flywheelP, 0, 0, flywheelF);
        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        ));
        imu.initialize(parameters);
        imu.resetYaw();

        telemetry.addLine("Init done");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // ---------------- DRIVE POSE + VELOCITY ----------------
            PoseVelocity2d robotVel = drive.updatePoseEstimate();
            Pose2d pose = drive.localizer.getPose();

            double robotX = pose.position.x;
            double robotY = pose.position.y;
            double robotHeading = pose.heading.toDouble();

            double vxRobot = robotVel.linearVel.x;
            double vyRobot = robotVel.linearVel.y;
            double omegaRobot = robotVel.angVel;

            double cosH = Math.cos(robotHeading);
            double sinH = Math.sin(robotHeading);

            double vxField = vxRobot * cosH - vyRobot * sinH;
            double vyField = vxRobot * sinH + vyRobot * cosH;

            // ---------------- TARGETING ----------------
            double targetX = goalX;
            double targetY = goalY;

            double dx = targetX - robotX;
            double dy = targetY - robotY;
            double distance = Math.hypot(dx, dy);

            double timeToTarget = (distance / projectileSpeed) + releaseDelay;

            double leadX = targetX - vxField * timeToTarget;
            double leadY = targetY - vyField * timeToTarget;

            double leadDx = leadX - robotX;
            double leadDy = leadY - robotY;
            double angleToTarget = Math.atan2(leadDy, leadDx);

            double turretTarget = angleToTarget - robotHeading;
            turretTarget = wrapAngle(turretTarget);

            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                double txDeg = result.getTx();
                turretTarget -= Math.toRadians(txDeg) * kVision;
            }

            turretTarget += offset;
            turretTarget = clamp(turretTarget, turret.getLeftLimitRad(), turret.getRightLimitRad());

            filteredTurretTarget = aimAlpha * turretTarget + (1.0 - aimAlpha) * filteredTurretTarget;

            // ---------------- TURRET CONTROL ----------------
            if (gamepad2.left_bumper) {
                // handled below with edge detect style not available in standard SDK
            }

            if (gamepad2.leftStickButtonWasPressed()) {
                offset += 0.05;
            }

            if (gamepad2.rightStickButtonWasPressed()) {
                offset -= 0.05;
            }

            if (gamepad1.leftStickButtonWasPressed()) {
                projectileSpeed += 5;
            }

            if (gamepad1.rightStickButtonWasPressed()) {
                projectileSpeed -= 5;
            }

            // Toggle auto/manual
            if (gamepad2.left_bumper) {
                // basic debounce
                while (opModeIsActive() && gamepad2.left_bumper) {
                    idle();
                }
                manual = !manual;
                turretIntegral = 0.0;
                turretLastError = 0.0;
                filteredTurretTarget = turret.getCurrentAngle();
                turretLastTimeNanos = System.nanoTime();
            }

            if (manual) {
                double manualPower = -gamepad2.left_stick_x * 0.5;
                turret.setPower(manualPower);

                filteredTurretTarget = turret.getCurrentAngle();
                turretIntegral = 0.0;
                turretLastError = 0.0;
                turretLastTimeNanos = System.nanoTime();
            } else {
                double currentAngle = turret.getCurrentAngle();
                double error = wrapAngle(filteredTurretTarget - currentAngle);

                long now = System.nanoTime();
                double dt = (now - turretLastTimeNanos) / 1e9;
                turretLastTimeNanos = now;

                if (dt > 0.0001 && dt < 0.1) {
                    turretIntegral += error * dt;
                    turretIntegral = clamp(turretIntegral, -integralClamp, integralClamp);

                    double derivative = (error - turretLastError) / dt;
                    turretLastError = error;

                    double output = turretKp * error + turretKi * turretIntegral + turretKd * derivative;
                    output = clamp(output, -turretMaxPower, turretMaxPower);

                    turret.setPower(output);
                } else {
                    turret.setPower(0.0);
                }
            }

            // Field-centric drive

            // Joystick variables
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // Reset yaw and Roadrunner pose in corner of field
            if (gamepad1.options) {
                imu.resetYaw();
                drive = new MecanumDrive(hardwareMap, new Pose2d(startX, startY, Math.toRadians(90)));
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX *= 1.1;

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            leftFront.setPower(frontLeftPower);
            leftBack.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightBack.setPower(backRightPower);

            // Limelight distance
            // We use the Limelight's 3D pose estimate feature to get distance from it
            if (result != null && result.isValid()) {
                for (LLResultTypes.FiducialResult fid : result.getFiducialResults()) {
                    Pose3D camToTag = fid.getCameraPoseTargetSpace();
                    double xTarget = camToTag.getPosition().x;
                    double yTarget = camToTag.getPosition().y;
                    double zTarget = camToTag.getPosition().z;

                    double distanceMeters = Math.sqrt(
                            xTarget * xTarget +
                                    yTarget * yTarget +
                                    zTarget * zTarget
                    );

                    // We like freedom units
                    distanceInches = DistanceUnit.INCH.fromMeters(distanceMeters);
                }
            }

            // Flywheel error tracking
            double curVelocity = flywheel1.getVelocity();
            double velocityError = curTargetVelocity - curVelocity;

            if (Math.abs(velocityError) < 60) {
                led4.setPosition(GREEN);
            } else {
                led4.setPosition(ORANGE);
            }

            flywheel1.setVelocity(curTargetVelocity);
            flywheel2.setVelocity(curTargetVelocity);

            // Ball counting logic
            // We use a distance sensor to track how many balls
            // enter the robot, and we display the result on
            // three LED lights on the robot
            double dist = distSensor.getDistance(DistanceUnit.CM);
            boolean broken = dist < thresholdCm;

            if (broken && !lastBroken) {
                count++;
                lastBroken = true;
            } else if (!broken && lastBroken) {
                lastBroken = false;
            }

            if (count <= 0) {
                count = 0;
                zero();
            } else if (count == 1) {
                one();
            } else if (count == 2) {
                two();
            } else {
                count = 3;
                three();
            }

            if (gamepad1.a) {
                resetCount();
            }

            // Launching control
            if (gamepad2.dpad_down) {
                actuator1.setPower(-1);
                actuator2.setPower(-1);
            } else if (gamepad2.dpad_up) {
                actuator1.setPower(1);
                actuator2.setPower(1);
            } else {
                actuator1.setPower(0);
                actuator2.setPower(0);
            }

            if (gamepad2.right_trigger > 0.1 && gamepad2.dpad_up) {
                count = 0;
            }

            // Flywheel speed control
            if (gamepad2.x) {
                curTargetVelocity = OFF;
            }

            if (gamepad2.b) {
                while (opModeIsActive() && gamepad2.b) {
                    idle();
                }
                if (curTargetVelocity == highVelocity) {
                    curTargetVelocity = lowVelocity;
                } else {
                    curTargetVelocity = highVelocity;
                }
            }

            // Intake control
            if (gamepad2.right_trigger > 0.1) {
                intake.setPower(1);
            } else if (gamepad2.left_trigger > 0.1) {
                intake.setPower(-1);
            } else {
                intake.setPower(0);
            }

            // TELEMETRY
            telemetry.addData("RobotHeadingDeg", Math.toDegrees(robotHeading));
            telemetry.addData("AngleToTargetDeg", Math.toDegrees(angleToTarget));
            telemetry.addData("TurretTargetDeg", Math.toDegrees(filteredTurretTarget));
            telemetry.addData("TurretCurrentDeg", Math.toDegrees(turret.getCurrentAngle()));

            telemetry.addData("vxRobot", vxRobot);
            telemetry.addData("vyRobot", vyRobot);
            telemetry.addData("vxField", vxField);
            telemetry.addData("vyField", vyField);
            telemetry.addData("omegaRobot", omegaRobot);

            telemetry.addData("distanceToGoal", distance);
            telemetry.addData("timeToTarget", timeToTarget);
            telemetry.addData("leadX", leadX);
            telemetry.addData("leadY", leadY);

            telemetry.addData("DistanceSensorCM", dist);
            telemetry.addData("Count", count);
            telemetry.addData("Offset", offset);
            telemetry.addData("TagDistanceInches", distanceInches);

            telemetry.addData("Target X", (result != null && result.isValid()) ? result.getTx() : "No target");
            telemetry.addData("Target Velocity", curTargetVelocity);
            telemetry.addData("Current Velocity", curVelocity);
            telemetry.addData("Velocity Error", velocityError);

            telemetry.addData("Manual", manual);
            telemetry.update();
        }
    }
}