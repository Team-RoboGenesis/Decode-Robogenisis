package org.firstinspires.ftc.teamcode.TeleOps;

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
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Tests.Turret;

@TeleOp(name = "TeleOpBLUE")
public class AutoAimTeleOpBLUE extends LinearOpMode {

    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private DcMotorEx flywheel1;
    private DcMotorEx flywheel2;
    private DcMotor intake = null;
    private CRServo actuator1 = null;
    private CRServo actuator2 = null;
    private DistanceSensor distSensor;
    private Servo led1;
    private Servo led2;
    private Servo led3;
    private Servo led4;
    Limelight3A limelight = null;

    private static final double WHITE = 0.9;
    private static final double GREEN = 0.456;
    private static final double PURPLE = 0.721;
    private static final double OFF = 0;
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
    double goalX = 72;
    double startY = -62;
    double startX = -62;
    double offset = 0;
    private double baseDist = 0;
    private double dist = 0;
    private final double threshold = 5;
    private boolean last = false;
    private boolean broken = false;
    private boolean isGreenBall = false;
    private boolean isPurpleBall = false;
    private boolean isBall = false;
    private boolean lastCheck = false;
    private int count = -1; // because it starts at 1 for some reason

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

    public void reset() {
        count = 0;
    }


    @Override
    public void runOpMode() throws InterruptedException {

        // We declare all of our motors, sensors, and servos here
        // using the hardwareMap class
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

        // Our camera is named Benny because... he just looks like a Benny
        limelight = hardwareMap.get(Limelight3A.class, "Benny");

        // We have to define a Roadrunner drive because we want to track
        // the robot's position on the field to actively track the position
        // of the goal
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(3, -14, Math.toRadians(185)));

        // Our turret gets special treatment because there is much more math
        // behind how it works, and all of it is declared in a different file
        Turret turret = new Turret(hardwareMap);

        telemetry.setMsTransmissionInterval(11);

        // We have different pipelines on our camera to look for different
        // things. Here, we switch to the one that targets April tags
        limelight.pipelineSwitch(APRIL_TAG_PIPELINE);

        // Sometimes we have to reverse the motors because they aren't
        // rotating in the correct direction. So we do that here
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);
        actuator1.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        // We want our motors to hold position if they don't have power
        // So we use setZeroPowerBehavior to make the motors brake
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // We want to access the encoder for our flywheel to
        // track RPM, we call RUN_USING_ENCODER to do so
        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // In order to shoot faster without losing RPM we create
        // a PIDF and activate it here
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        // We want to use the IMU to track heading so we declare it
        // and identify the orientation here
        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                // Note: if these values are incorrect, then the IMU
                // will return an inaccurate heading
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);
        imu.resetYaw();

        // Initialize our camera for distance tracking
        limelight.start();

        telemetry.addLine("Init done");

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

            // Telemetry for identifying error
            telemetry.addData("X", robotX);
            telemetry.addData("Y", robotY);
            telemetry.addData("RobotHeadingDeg", Math.toDegrees(robotHeading));
            telemetry.addData("AngleToCornerDeg", Math.toDegrees(angleToCorner));
            telemetry.addData("TurretAngleDeg", Math.toDegrees(turretAngle));

            // Drive variables
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // This is how we reset the robot's position at the start of a match
            if (gamepad1.options)
            {
                imu.resetYaw();
                drive = new MecanumDrive(hardwareMap, new Pose2d(startX, startY, Math.toRadians(90)));
            }

            // Get the IMU angle to counteract it
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

            // Start the drive
            leftFront.setPower(frontLeftPower);
            leftBack.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightBack.setPower(backRightPower);

            // Start polling for data
            LLResult result = limelight.getLatestResult();

            // Chack that there is a target
            if (result != null && result.isValid()) {
                // List all 3D tracking results
                for (LLResultTypes.FiducialResult fid : result.getFiducialResults()) {
                    // Return 3D tracking results
                    Pose3D camToTag = fid.getCameraPoseTargetSpace();
                    double xTarget = camToTag.getPosition().x;
                    double yTarget = camToTag.getPosition().y;
                    double zTarget = camToTag.getPosition().z;

                    // Turn 3D results into distance
                    double distanceMeters = Math.sqrt(xTarget * xTarget + yTarget * yTarget + zTarget * zTarget);

                    // Translate to freedom units
                    distanceInches = DistanceUnit.INCH.fromMeters(distanceMeters);
                }
            }

            // Variables for flywheel PIDF
            double curVelocity = flywheel1.getVelocity();
            double error = curTargetVelocity - curVelocity;

            if (Math.abs(error) < 60)
            {
                led4.setPosition(GREEN);
            }
            else
            {
                led4.setPosition(0.277);
            }

            dist = distSensor.getDistance(DistanceUnit.CM);
            broken = dist < 14;
            isBall = isGreenBall || isPurpleBall;

            if (broken && !last) {
                count++;
                last = true;
            } else if (!broken && last) {
                last = false;
            }

            if (isBall && !lastCheck) {
                lastCheck = true;
                count--;
            } else if (!isBall) {
                lastCheck = false;
            }

            if (count == 0) {
                zero();
            } else if (count == 1) {
                one();
            } else if (count == 2) {
                two();
            } else if (count == 3) {
                three();
            } else if (count < 0) {
                count = 0;
            } else if (count > 3) {
                count = 3;
            }
            if (gamepad1.a) reset();

            // MORE TELEMETRY
            telemetry.addData("Distance: ", dist);
            telemetry.addData("Count: ", count);
            telemetry.addData("MS interval: ", telemetry.getMsTransmissionInterval());
            telemetry.addData("Green ball? ", isGreenBall);
            telemetry.addData("Purple ball?", isPurpleBall);
            telemetry.addData("offset: ", offset);
            telemetry.addData("Inches: ", distanceInches);
            telemetry.addData("YAW: ", imu.getRobotYawPitchRollAngles().getYaw());
            telemetry.addData("Target X: ", result.getTx());
            telemetry.addData("RPM", RPM);
            telemetry.addData("Pos: ", pos);
            telemetry.addData("Target Velocity: ", "%,4f", curTargetVelocity);
            telemetry.addData("Current Velocity: ", "%,4f", curVelocity);
            telemetry.addData("Error: ", "%,2f", error);
            telemetry.update();

            // Initialize flywheel motors with tuned PIDF values
            flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

            // Spin up flywheel
            flywheel1.setVelocity(curTargetVelocity);
            flywheel2.setVelocity(curTargetVelocity);

            // Controls for last stage of transfer
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

            if (gamepad2.right_trigger > 0.1 && gamepad2.dpad_up)
            {
                count = 0;
            }

            // Controls for changing flywheel velocity
            if (gamepad2.x)
            {
                curTargetVelocity = OFF;
            }

            if (gamepad2.bWasPressed()) {
                if (curTargetVelocity == highVelocity) {
                    curTargetVelocity = lowVelocity;
                } else curTargetVelocity = highVelocity;
            }

            // Controls for intake
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
        }
    }
}