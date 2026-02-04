package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "New Teleop")
public class NewTeleOp extends LinearOpMode {

    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private DcMotor flywheel1 = null;
    private DcMotor flywheel2 = null;
    private DcMotor intake = null;
    private DcMotor turret = null;
    private CRServo actuator1 = null;
    private CRServo actuator2 = null;
    private Servo led1 = null;
    private Servo led2 = null;
    private Servo led3 = null;
//    private DigitalChannel turretZero = null;
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
    private double RPM = 0;

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
        actuator1.setPower(1);
        actuator2.setPower(1);
        sleep(1000);
        actuator1.setPower(0);
        actuator2.setPower(0);
        intake.setPower(0);
        sleep(300);
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

    private void turn (double power)
    {
        rightFront.setPower(power);
        leftFront.setPower(-power);
        rightBack.setPower(power);
        leftBack.setPower(-power);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        turret = hardwareMap.get(DcMotor.class, "turret");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");
//        turretZero = hardwareMap.get(DigitalChannel.class, "Limiter");

        limelight = hardwareMap.get(Limelight3A.class, "Benny");

        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);// No more magic number

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

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        if (turretZero.getState())
//        {
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        }
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setPower(0.7);
        turret.setTargetPosition(0);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setDirection(DcMotorSimple.Direction.REVERSE);


        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        actuator1.setDirection(DcMotorSimple.Direction.REVERSE);
        actuator2.setDirection(DcMotorSimple.Direction.REVERSE);



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

            telemetry.addData("YAW: ", imu.getRobotYawPitchRollAngles().getYaw());
            telemetry.addData("Turret: ", targetPos);
            telemetry.addData("Target Area: ", result.getTa());
            telemetry.addData("Target X: ", result.getTx());
            telemetry.addData("RPM", RPM);
            telemetry.update();

            turretPos(targetPos);

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

            if (gamepad2.left_bumper)
            {
                turret.setTargetPosition(110);
            }
            else
            {
                turretPos(targetPos);
            }

            if (gamepad2.cross)// Make these a function
            {
                flywheel1.setPower(LOW_POWER);
                flywheel2.setPower(LOW_POWER);
            }
            else if (gamepad2.circle)
            {
                flywheel1.setPower(MEDIUM_POWER);
                flywheel2.setPower(MEDIUM_POWER);
            }
            else if (gamepad2.triangle)
            {
                flywheel1.setPower(HIGH_POWER);
                flywheel2.setPower(HIGH_POWER);
            }
            else if (gamepad2.square)
            {
                flywheel1.setPower(OFF);
                flywheel2.setPower(OFF);
            }
            else if (gamepad2.dpad_left)
            {
                shootThreeBalls();
            }
            else if (gamepad2.right_trigger > 0.1)
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

            if (result != null)
            {
                telemetry.addData("tx", result.getTx());
                telemetry.addData("ty", result.getTy());
                telemetry.addData("april Id", result.getBarcodeResults());
                telemetry.addData(">", "Robot Ready.  Press Play.");
            }


//            if (result != null) {
//                    if (result.isValid()) {
//
//                        Pose3D botpose = result.getBotpose();
//
//                        while (opModeIsActive()) {
//
//                            LLStatus status = limelight.getStatus();
//                            telemetry.addData("Name", "%s",
//                                    status.getName());
//                            telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
//                                    status.getTemp(), status.getCpu(), (int) status.getFps());
//                            telemetry.addData("Pipeline", "Index: %d, Type: %s",
//                                    status.getPipelineIndex(), status.getPipelineType());
//
//                            result = limelight.getLatestResult();
//                            if (result != null) {
//                                // Access general information
//                                botpose = result.getBotpose();
//                                double captureLatency = result.getCaptureLatency();
//                                double targetingLatency = result.getTargetingLatency();
//                                double parseLatency = result.getParseLatency();
//                                telemetry.addData("LL Latency", captureLatency + targetingLatency);
//                                telemetry.addData("Parse Latency", parseLatency);
//                                telemetry.addData("PythonOutput", java.util.Arrays.toString(result.getPythonOutput()));
//
//                                if (result.isValid()) {
//                                    telemetry.addData("tx", result.getTx());
//                                    telemetry.addData("txnc", result.getTxNC());
//                                    telemetry.addData("ty", result.getTy());
//                                    telemetry.addData("tync", result.getTyNC());
//
//                                    telemetry.addData("Botpose", botpose.toString());
//
//                                    // Access barcode results
//                                    List<LLResultTypes.BarcodeResult> barcodeResults = result.getBarcodeResults();
//                                    for (LLResultTypes.BarcodeResult br : barcodeResults) {
//                                        telemetry.addData("Barcode", "Data: %s", br.getData());
//                                    }
//
//                                    // Access classifier results
//                                    List<LLResultTypes.ClassifierResult> classifierResults = result.getClassifierResults();
//                                    for (LLResultTypes.ClassifierResult cr : classifierResults) {
//                                        telemetry.addData("Classifier", "Class: %s, Confidence: %.2f", cr.getClassName(), cr.getConfidence());
//                                    }
//
//                                    // Access detector results
//                                    List<LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();
//                                    for (LLResultTypes.DetectorResult dr : detectorResults) {
//                                        telemetry.addData("Detector", "Class: %s, Area: %.2f", dr.getClassName(), dr.getTargetArea());
//                                    }
//
//                                    // Access fiducial results
//                                List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
//                                    for (LLResultTypes.FiducialResult fr : fiducialResults) {
//                                        telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
//                                    }
//
//                                    // Access color results
//                                    List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
//                                    for (LLResultTypes.ColorResult cr : colorResults) {
//                                        telemetry.addData("Color", "X: %.2f, Y: %.2f", cr.getTargetXDegrees(), cr.getTargetYDegrees());
//                                    }
//                                }
//                            } else {
//                                telemetry.addData("Limelight", "No data available");
//                            }
//                            telemetry.update();
//                        }
//                        limelight.stop();
//                    }
//                }
        }
        turret.setTargetPosition(0);
    }
}