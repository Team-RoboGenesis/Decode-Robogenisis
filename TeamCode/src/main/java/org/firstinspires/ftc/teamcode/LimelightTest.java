package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@Autonomous(name = "limelightTest")
public class LimelightTest extends LinearOpMode {

    public DcMotor frontLeftWheel = null;
    public DcMotor frontRightWheel = null;
    public DcMotor backLeftWheel = null;
    public DcMotor backRightWheel = null;
    public Servo mainIntake = null;
    public DcMotor slides = null;
    public Servo pivot = null;
    public DcMotor leftIntakeArm = null;
    public DcMotor rightIntakeArm = null;
    private Servo limeAlign = null;
    private double servoPos = 0;

    private Limelight3A limelight;

    @Override
    public void runOpMode() throws InterruptedException {


        limelight = hardwareMap.get(Limelight3A.class, "Benny");

        telemetry.setMsTransmissionInterval(11);
        limeAlign = hardwareMap.get(Servo.class, "align");
        limelight.pipelineSwitch(0);

        limeAlign.setPosition(0.5);

        /*
         * Starts polling for data.
         */
        limelight.start();
        servoPos = limeAlign.getPosition();

        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            if (result != null) {
                if (result.isValid()) {

                    Pose3D botpose = result.getBotpose();
                    telemetry.addData("tx", result.getTx());
                    telemetry.addData("ty", result.getTy());
                    telemetry.addData("Botpose", botpose.toString());

                    telemetry.addData(">", "Robot Ready.  Press Play.");
                    telemetry.update();

                    while (opModeIsActive()) {
                        if(result.getTx()<-6) {
                            limeAlign.setPosition(servoPos-0.025);
                        }

                        if(result.getTx()>6) {
                            limeAlign.setPosition(servoPos+0.025);
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

