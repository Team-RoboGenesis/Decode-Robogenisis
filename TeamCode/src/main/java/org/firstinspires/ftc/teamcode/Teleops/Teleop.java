package org.firstinspires.ftc.teamcode.Teleops;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@TeleOp(name = "Teleop")
public class Teleop extends LinearOpMode {

    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private DcMotor potatoCannon = null;
    private DcMotor potatoCannonTwo = null;
//    private DcMotor leftFlywheel = null;
//    private DcMotor rightFlywheel = null;
//    private Servo stopper = null;
//    private Servo led = null;
//    private Limelight3A limelight;
//    private Servo limeAlign = null;
    private Servo led1 = null;
    private Servo led2 = null;
    private Servo led3 = null;
    private Servo actuator = null;
    private double servoPos = 0;
    private double targetPos = 0;
    private double GREEN = 0.456;
    private double PURPLE = 0.721;
    private double led1Color = 0;
    private double led2Color = 0;
    private double led3Color = 0;
    private double OPEN = 0.65;
    private double CLOSED = 0.1;
    private double HIGH_POWER = 0.7;
    private double LOW_POWER = 0.57;
    private double MEDIUM_POWER = 0.65;
    private double OFF = 0;

//    private void turn (double power)
//    {
//        rightFront.setPower(power);
//        leftFront.setPower(-power);
//        rightBack.setPower(power);
//        leftBack.setPower(-power);
//    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        potatoCannon = hardwareMap.get(DcMotor.class, "flywheel");
        actuator = hardwareMap.get(Servo.class, "gate");
        led1 = hardwareMap.get(Servo.class, "led1");
        led2 = hardwareMap.get(Servo.class, "led2");
        led3 = hardwareMap.get(Servo.class, "led3");
//        limelight = hardwareMap.get(Limelight3A.class, "Benny");

        telemetry.setMsTransmissionInterval(11);
//        limelight.pipelineSwitch(0);


        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        potatoCannon.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        potatoCannon.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        potatoCannon.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        imu.resetYaw();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
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

//            limelight.start();
//            LLResult result = limelight.getLatestResult();
//            servoPos = limeAlign.getPosition();
            targetPos = servoPos;

//            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
//            for (LLResultTypes.FiducialResult fr : fiducialResults) {
//                if (fr.getFiducialId() == 21) {
//                    led1Color = GREEN;
//                    led2Color = PURPLE;
//                    led3Color = PURPLE;
//                } else if (fr.getFiducialId() == 22) {
//                    led1Color = PURPLE;
//                    led2Color = GREEN;
//                    led3Color = PURPLE;
//                } else if (fr.getFiducialId() == 23) {
//                    led1Color = PURPLE;
//                    led2Color = PURPLE;
//                    led3Color = GREEN;
//                }
//            }
//
//            led1.setPosition(led1Color);
//            led2.setPosition(led2Color);
//            led3.setPosition(led3Color);

//            boolean if1 = result.getTx() >= 5;
//            boolean if2 = result.getTx() <= -5;

//            if (gamepad1.right_bumper)
//            {
//                if (if1) {
//                    turn(0.4);
//                }
//                else if (if2) {
//                    turn(-0.4);
//                }
//                else {
//                    turn(0);
//                }
//            }
//            else if(!gamepad1.right_bumper)
//            {
//                leftFront.setPower(0);
//                leftBack.setPower(0);
//                rightFront.setPower(0);
//                rightBack.setPower(0);
//            }

            if (gamepad2.dpad_down) {
                actuator.setPosition(OPEN);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                actuator.setPosition(CLOSED);
            }
            else if (gamepad2.b)
            {
                potatoCannon.setPower(LOW_POWER);
            }
            else if (gamepad2.a)
            {
                potatoCannon.setPower(MEDIUM_POWER);
            }
            else if (gamepad2.y)
            {
                potatoCannon.setPower(HIGH_POWER);
            }
            else if (gamepad2.x)
            {
                potatoCannon.setPower(OFF);
            }
            else if (gamepad2.dpad_up)
            {
                actuator.setPosition(OPEN);
            }
            else if (gamepad1.dpad_left)
            {
                led1.setPosition(PURPLE);
                led2.setPosition(PURPLE);
                led3.setPosition(GREEN);
            }
            else if (gamepad1.dpad_up)
            {
                led1.setPosition(GREEN);
                led2.setPosition(PURPLE);
                led3.setPosition(PURPLE);
            }
            else if (gamepad1.dpad_right)
            {
                led1.setPosition(PURPLE);
                led2.setPosition(GREEN);
                led3.setPosition(PURPLE);
            } else if (gamepad1.dpad_down)
            {
                led1.setPosition(0);
                led2.setPosition(0);
                led3.setPosition(0);
            }
//            if (result != null)
//            {
//                telemetry.addData("tx", result.getTx());
//                telemetry.addData("ty", result.getTy());
//                telemetry.addData("april Id", result.getBarcodeResults());
//                telemetry.addData(">", "Robot Ready.  Press Play.");
//                telemetry.addData("color", led1Color);
//            }


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
////                                List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
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
        }
    }