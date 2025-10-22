package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@TeleOp(name = "limelightTest")

public class LimelightTest extends LinearOpMode {


//    private Servo limeAlign = null;
    private double servoPos = 0;
    private double targetPos = 0;
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;

    private void turn (double power)
    {
        rightFront.setPower(power);
        leftFront.setPower(-power);
        rightBack.setPower(power);
        leftBack.setPower(-power);
    }

    private Limelight3A limelight;

    @Override
    public void runOpMode() throws InterruptedException {


        limelight = hardwareMap.get(Limelight3A.class, "Benny");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
//        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
 //    leftBack.setDirection(DcMotorSimple.Direction.REVERSE);



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
//            servoPos = limeAlign.getPosition();
            targetPos = servoPos;

            if (result != null) {
                if (result.isValid()) {

                    Pose3D botpose = result.getBotpose();
                    telemetry.addData("tx", result.getTx());
                    telemetry.addData("ty", result.getTy());
                    telemetry.addData("Botpose", botpose.toString());
                    telemetry.addData("april Id", result.getBarcodeResults());
                    telemetry.addData(">", "Robot Ready.  Press Play.");




                    while (opModeIsActive()) {
//                          if(result.getTx() <= -3) {
//                              limeAlign.setPosition(targetPos+0.01);
//                              targetPos = targetPos+0.02;
//                              Thread.sleep(50);
//                          }

//                          if(result.getTx() >= 3) {
//                              limeAlign.setPosition(targetPos-0.01);
//                              targetPos = targetPos -0.02;
//                              Thread.sleep(50);
//                          }

                        double y = gamepad1.left_stick_y; // Remember, Y stick is reversed!
                        double x = -gamepad1.left_stick_x;
                        double rx = -gamepad1.right_stick_x;
                        boolean if1 = result.getTx() >= 5;
                        boolean if2 = result.getTx() <= -5;

                        if (gamepad1.right_bumper)
                        {
                            if (if1) {
                                turn(0.4);
                            }
                            else if (if2) {
                                turn(-0.4);
                            }
                            else {
                                turn(0);
                            }
                        }

                        if(x != 0 || y != 0 || rx != 0)
                        {
                            leftFront.setPower(y + x + rx);
                            leftBack.setPower(y - x + rx);
                            rightFront.setPower(y - x - rx);
                            rightBack.setPower(y + x - rx);
                        }
                        else if(!gamepad1.right_bumper)
                        {
                            leftFront.setPower(0);
                            leftBack.setPower(0);
                            rightFront.setPower(0);
                            rightBack.setPower(0);
                        }

                        LLStatus status = limelight.getStatus();
                        telemetry.addData("Name", "%s",
                                status.getName());
                        telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                                status.getTemp(), status.getCpu(),(int)status.getFps());
                        telemetry.addData("Pipeline", "Index: %d, Type: %s",
                                status.getPipelineIndex(), status.getPipelineType());

                        result = limelight.getLatestResult();
                        if (result != null) {
                            // Access general information
                            botpose = result.getBotpose();
                            double captureLatency = result.getCaptureLatency();
                            double targetingLatency = result.getTargetingLatency();
                            double parseLatency = result.getParseLatency();
                            telemetry.addData("LL Latency", captureLatency + targetingLatency);
                            telemetry.addData("Parse Latency", parseLatency);
                            telemetry.addData("PythonOutput", java.util.Arrays.toString(result.getPythonOutput()));

                            if (result.isValid()) {
                                telemetry.addData("tx", result.getTx());
                                telemetry.addData("txnc", result.getTxNC());
                                telemetry.addData("ty", result.getTy());
                                telemetry.addData("tync", result.getTyNC());

                                telemetry.addData("Botpose", botpose.toString());

                                // Access barcode results
                                List<LLResultTypes.BarcodeResult> barcodeResults = result.getBarcodeResults();
                                for (LLResultTypes.BarcodeResult br : barcodeResults) {
                                    telemetry.addData("Barcode", "Data: %s", br.getData());
                                }

                                // Access classifier results
                                List<LLResultTypes.ClassifierResult> classifierResults = result.getClassifierResults();
                                for (LLResultTypes.ClassifierResult cr : classifierResults) {
                                    telemetry.addData("Classifier", "Class: %s, Confidence: %.2f", cr.getClassName(), cr.getConfidence());
                                }

                                // Access detector results
                                List<LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();
                                for (LLResultTypes.DetectorResult dr : detectorResults) {
                                    telemetry.addData("Detector", "Class: %s, Area: %.2f", dr.getClassName(), dr.getTargetArea());
                                }

                                // Access fiducial results
                                List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                                for (LLResultTypes.FiducialResult fr : fiducialResults) {
                                    telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(),fr.getTargetXDegrees(), fr.getTargetYDegrees());
                                }

                                // Access color results
                                List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
                                for (LLResultTypes.ColorResult cr : colorResults) {
                                    telemetry.addData("Color", "X: %.2f, Y: %.2f", cr.getTargetXDegrees(), cr.getTargetYDegrees());
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
            }
        }

