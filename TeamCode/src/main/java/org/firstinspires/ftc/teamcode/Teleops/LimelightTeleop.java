package org.firstinspires.ftc.teamcode.Teleops;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@TeleOp(name = "limelightTeleop")

public class LimelightTeleop extends LinearOpMode {


    //    private Servo limeAlign = null;
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
    private Limelight3A limelight;
    //    private Servo limeAlign = null;
    private Servo led1 = null;
    private Servo led2 = null;
    private Servo led3 = null;
    private Servo actuator = null;

    private double GREEN = 0.456;
    private double PURPLE = 0.721;
    private double led1Color = 0;
    private double led2Color = 0;
    private double led3Color = 0;

    private void turn (double power)
    {
        rightFront.setPower(-power);
        leftFront.setPower(power);
        rightBack.setPower(-power);
        leftBack.setPower(power);
    }

    @Override
    public void runOpMode() throws InterruptedException {


        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        potatoCannon = hardwareMap.get(DcMotor.class, "flywheel");
        actuator = hardwareMap.get(Servo.class, "gate");
        led1 = hardwareMap.get(Servo.class, "led1");
        led2 = hardwareMap.get(Servo.class, "led2");
        led3 = hardwareMap.get(Servo.class, "led3");
        limelight = hardwareMap.get(Limelight3A.class, "Benny");
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        imu.resetYaw();



        telemetry.setMsTransmissionInterval(11);
//        limeAlign = hardwareMap.get(Servo.class, "align");
        limelight.pipelineSwitch(0);

//        limeAlign.setPosition(0.5);

        /*
         * Starts polling for data.
         */
        limelight.start();


        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

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

//            if (result != null) {
                double tx = result.getTx();
                double ty = result.getTy();

//                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();

//                int tagId = fiducials.get(0).getFiducialId();

//                if (tagId == 21) {
//                    led1Color = GREEN;
//                    led2Color = PURPLE;
//                    led3Color = PURPLE;
//                }
//                else if (tagId == 22) {
//                    led1Color = PURPLE;
//                    led2Color = GREEN;
//                    led3Color = PURPLE;
//                }
//                else if (tagId == 23) {
//                    led1Color = PURPLE;
//                    led2Color = PURPLE;
//                    led3Color = GREEN;
//                }
//            }

            led1.setPosition(led1Color);
            led2.setPosition(led2Color);
            led3.setPosition(led3Color);
//                if (result.isValid()) {
//
//
//
//                    Pose3D botpose = result.getBotpose();
//                    telemetry.addData("tx", result.getTx());
//                    telemetry.addData("ty", result.getTy());
//                    telemetry.addData("Botpose", botpose.toString());
//                    telemetry.addData("april Id", result.getBarcodeResults());
//                    telemetry.addData(">", "Robot Ready.  Press Play.");




                    while (opModeIsActive()) {

                        boolean if1 = result.getTx() >= 6;
                        boolean if2 = result.getTx() <= 0;

                        if (gamepad1.right_bumper)
                        {
                            if (if1) {
                                turn(0.3);
                                Thread.sleep(20);
                                if (if2 == false && if1 == false) {
                                    turn(0);
                                }
                            }
                            else if (if2) {
                                turn(-0.3);
                                Thread.sleep(20);
                                if (if2 == false && if1 == false) {
                                    turn(0);
                                }
                            }
                            else {
                                turn(0);
                            }
                        }

                        if(x != 0 || y != 0 || rx != 0)
                        {
                            leftFront.setPower(frontLeftPower);
                            leftBack.setPower(backLeftPower);
                            rightFront.setPower(frontRightPower);
                            rightBack.setPower(backRightPower);
                        }
                        else if(!gamepad1.right_bumper)
                        {
                            leftFront.setPower(0);
                            leftBack.setPower(0);
                            rightFront.setPower(0);
                            rightBack.setPower(0);
                        }

                        if (gamepad2.dpad_down) {
                            actuator.setPosition(0.65);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            actuator.setPosition(0.1);
                        } else if (gamepad2.b) {
                            potatoCannon.setPower(0.6);

                        } else if (gamepad2.x) {
                            potatoCannon.setPower(0);

                        } else if (gamepad2.y) {
                            potatoCannon.setPower(0.7);
                        }
                        else if (gamepad2.dpad_up)
                        {
                            actuator.setPosition(0.65);
                        } else if (gamepad2.a) {
                            potatoCannon.setPower(0.63);
                        }

                        LLStatus status = limelight.getStatus();

                        result = limelight.getLatestResult();
                        if (result != null) {
                            // Access general information
//                            botpose = result.getBotpose();

                            if (result.isValid()) {
                                telemetry.addData("tx", result.getTx());
                                telemetry.addData("txnc", result.getTxNC());
                                telemetry.addData("ty", result.getTy());
                                telemetry.addData("tync", result.getTyNC());

//                                telemetry.addData("Botpose", botpose.toString());

                                List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                                for (LLResultTypes.FiducialResult fr : fiducialResults) {
                                    telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
                                }
                            }
                        } else {
                            telemetry.addData("Limelight", "No data available");
                        }
                        telemetry.update();
                    }
                    limelight.stop();
                }
            }
        }
//    }

