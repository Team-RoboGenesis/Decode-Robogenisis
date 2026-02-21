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
import org.firstinspires.ftc.teamcode.Autos.AutoCloseTwelveBLUE;
import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@TeleOp(name = "New Teleop")
public class NewTeleOp extends LinearOpMode {

    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private DcMotorEx flywheel1;
    private DcMotorEx flywheel2;
    private DcMotor intake = null;
    private DcMotor turret = null;
    private CRServo actuator1 = null;
    private CRServo actuator2 = null;
    private Servo led1 = null;
    private Servo led2 = null;
    private Servo led3 = null;
    Limelight3A limelight = null;

    private static final double GREEN = 0.456;
    private static final double PURPLE = 0.721;
    private static final double HIGH_POWER = 0.65;
    private static final double LOW_POWER = 0.52;
    private static final double MEDIUM_POWER = 0.57;
    private static final double OFF = 0;
    private static final double FAR_SPEED = 3300;
    private static final int SHOOT_POSE = 0;
    private final static int CONVERT_TO_MINUTE = 600;
    private static final double ticksPerRotation = 25.5;
    private static final int APRIL_TAG_PIPELINE = 0;
    double P = 82;
    double F = 12.3474;
    private double highVelocity = 1500;
    private double lowVelocity = 1200;
    double curTargetVelocity = highVelocity;
    private double RPM = 0;
    private double pos = 0;
    private double distanceInches = 0;


    private void turretPos(int position)
    {
        if (turret.getCurrentPosition() > 210)
        {
            turret.setTargetPosition(210);
        }
        else if (turret.getCurrentPosition() < -221)
        {
            turret.setTargetPosition(-221);
        }
        else
        {
            turret.setTargetPosition(position);
        }
    }



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
        turret = hardwareMap.get(DcMotor.class, "turret");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");

        limelight = hardwareMap.get(Limelight3A.class, "Benny");

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

        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);

        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        telemetry.addLine("Init done");

        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setPower(0.7);
        turret.setTargetPosition(0);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setDirection(DcMotorSimple.Direction.REVERSE);


        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        actuator1.setDirection(DcMotorSimple.Direction.REVERSE);
        actuator2.setDirection(DcMotorSimple.Direction.REVERSE);

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        imu.resetYaw();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive())
        {

            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            if (gamepad1.options) {
                imu.resetYaw();
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

            int targetPos = (int) ( turret.getCurrentPosition() + (gamepad2.left_stick_x * 40));

            drive.updatePoseEstimate();

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

            telemetry.addData("Inches: ", distanceInches);

            Pose2d pose = drive.localizer.getPose();
            telemetry.addData("x", pose.position.x);
            telemetry.addData("y", pose.position.y);
            telemetry.addData("heading (deg)", Math.toDegrees(pose.heading.toDouble()));
            telemetry.addData("YAW: ", imu.getRobotYawPitchRollAngles().getYaw());
            telemetry.addData("Turret: ", targetPos);
            telemetry.addData("Target X: ", result.getTx());
            telemetry.addData("RPM", RPM);
            telemetry.addData("Pos: ", pos);
            telemetry.addData("Target Velocity: ", "%,4f", curTargetVelocity);
            telemetry.addData("Current Velocity: ", "%,4f", curVelocity);
            telemetry.addData("Error: ", "%,2f", error);
            telemetry.addLine("========================================");
            telemetry.addData("Tuning P: ", "%,4f (D_Pad U/D)", P);
            telemetry.addData("Tuning F: ", "%,4f (D_Pad L/R)", F);
            telemetry.update();

            turretPos(targetPos);

//            PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
            flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

            flywheel1.setVelocity(curTargetVelocity);
            flywheel2.setVelocity(curTargetVelocity);

            if (gamepad2.dpad_down)
            {
                actuator1.setPower(-1);
                actuator2.setPower(1);
            }
            else if (gamepad2.dpad_up)
            {
                actuator1.setPower(1);
                actuator2.setPower(-1);
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

            if (gamepad2.left_bumper)
            {
                turret.setTargetPosition(110);
            }
            else
            {
                turretPos(targetPos);
            }

//            if (gamepad2.cross)// Make these a function
//            {
//                flywheel1.setPower(LOW_POWER);
//                flywheel2.setPower(LOW_POWER);
//            }
//            else if (gamepad2.circle)
//            {
//                flywheel1.setPower(MEDIUM_POWER);
//                flywheel2.setPower(MEDIUM_POWER);
//            }
//            else if (gamepad2.triangle)
//            {
//                flywheel1.setPower(HIGH_POWER);
//                flywheel2.setPower(HIGH_POWER);
//            }
//            else if (gamepad2.square)
//            {
//                flywheel1.setPower(OFF);
//                flywheel2.setPower(OFF);
//            }
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
            if (result.isValid())
            {
                if (gamepad2.right_bumper)
                {
                    pos = (targetPos + (result.getTx()));
                }
            }
        }
        turret.setTargetPosition(0);
    }
}