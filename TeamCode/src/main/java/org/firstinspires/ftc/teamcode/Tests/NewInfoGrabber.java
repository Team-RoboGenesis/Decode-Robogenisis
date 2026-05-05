package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@TeleOp(name = "NewInfoGrabber")
public class NewInfoGrabber extends LinearOpMode {
    private final double highVelocity = 1550;
    private final double lowVelocity = 1230;
    private double curTargetVelocity = 0;
    private DcMotorEx flywheel1;
    private DcMotorEx flywheel2;
    private DcMotor intake;

    private CRServo actuator1 = null;
    private CRServo actuator2 = null;
    private VoltageSensor voltageSensor;

    private boolean dataCollection = false;

    private String voltageList = "";
    private String speedList = "";
    private String flywheelSpeedList = "";

    private double ticksPerRotation = 25.5;
    private double maxRPM = 0;
    private int dataPoints = 0;

    double P = 82;
    double F = 12.3474;

    String desmosFuncSpeed = null;
    String desmosFuncVoltage = null;
    String desmosFuncFlywheelSpeed = null;



    private void log(String text) {
        NewFileUtil.appendLine(text);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");

        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        actuator1.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addLine("Ready to start");
        telemetry.update();

        waitForStart();
        NewFileUtil.init(hardwareMap.appContext, "test log");
        // Confirm file writing works
        log("=== START ===");

        while (opModeIsActive()) {

            if (gamepad1.a) { // empty/format the log file
                NewFileUtil.overwriteFromIndex("", 0);
                telemetry.addLine("overote file");

            }

            if (gamepad1.b) { // spin up and init data collection
//                intake.setPower(1);
                dataCollection = true;
            }

            if (gamepad1.x) { // spin down
                intake.setPower(0);
            }

            flywheel1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

            // Spin up flywheel
            flywheel1.setVelocity(curTargetVelocity);
            flywheel2.setVelocity(curTargetVelocity);

            // Controls for last stage of transfer
            if (gamepad1.dpad_down)
            {
                actuator1.setPower(-1);
                actuator2.setPower(-1);
            }
            else if (gamepad1.dpad_up)
            {
                actuator1.setPower(1);
                actuator2.setPower(1);
            }
            else
            {
                actuator1.setPower(0);
                actuator2.setPower(0);
            }

            if (gamepad1.left_bumper)
            {
                curTargetVelocity = 0;
            }

            if (gamepad1.rightBumperWasPressed()) {
                if (curTargetVelocity == highVelocity) {
                    curTargetVelocity = lowVelocity;
                } else curTargetVelocity = highVelocity;
            }

            // Controls for intake
            if (gamepad1.right_trigger > 0.1)
            {
                intake.setPower(1);
            }
            else if (gamepad1.left_trigger > 0.1)
            {
                intake.setPower(-1);
            }
            else if (gamepad1.right_trigger < 0.1 && gamepad1.left_trigger < 0.1)
            {
                intake.setPower(0);
            }

            if (gamepad1.y) { // spin down, format, and log all data
                dataCollection = false;
                intake.setPower(0);
                String[] values = flywheelSpeedList.split(",");
                StringBuilder desmosTable = new StringBuilder();
                int time = 0;
                for (int i = 0; i < values.length; i++) {
                    desmosTable.append(time)
                            .append(", ")
                            .append(values[i].trim())
                            .append("\n");
                    time += 100;
                }
                desmosFuncFlywheelSpeed = desmosTable.toString();

                values = speedList.split(",");
                desmosTable = new StringBuilder();
                time = 0; // start at 0 ms
                for (int i = 0; i < values.length; i++) {
                    desmosTable.append(time)
                            .append(", ")
                            .append(values[i].trim())
                            .append("\n"); // new row for Desmos

                    time += 100; // increment by 100 ms
                }
                desmosFuncSpeed = desmosTable.toString();

                // make speed list a desmos table
                values = voltageList.split(",");
                desmosTable = new StringBuilder();
                time = 0; // start at 0 ms
                for (int i = 0; i < values.length; i++) {
                    desmosTable.append(time)
                            .append(", ")
                            .append(values[i].trim())
                            .append("\n"); // new row for Desmos

                    time += 100; // increment by 100 ms
                }
                desmosFuncVoltage = desmosTable.toString();
                log("max RPM: " + maxRPM);
                log("");

                log("speed list:");
                log(desmosFuncSpeed);

                log("");
                log("voltage list:");
                log(desmosFuncVoltage);

                log("");
                log("flywheel RPM list:");
                log(desmosFuncFlywheelSpeed);

                telemetry.addLine("Data saved!");
                telemetry.update();
                sleep(50000);
            }

            if (dataCollection) {
                int previousTicks = intake.getCurrentPosition();
                int previousTicksFlywheel = flywheel1.getCurrentPosition();

                sleep(100);

                int flywheelTicks = flywheel1.getCurrentPosition() - previousTicksFlywheel;
                double RPMFlywheel = (flywheelTicks / ticksPerRotation) * 600;
                RPMFlywheel = (Math.floor(RPMFlywheel * 1000)) / 1000;
                if (RPMFlywheel > maxRPM) maxRPM = RPMFlywheel;
//                RPMFlywheel /= 500;

                int ticks = intake.getCurrentPosition() - previousTicks;
                double RPM = (ticks / ticksPerRotation) * 600;
                RPM = (Math.floor(RPM * 1000)) / 1000;
//                if (RPM > maxRPM) maxRPM = RPM;
//                RPM /= 500;

                // Build speed list
                if (speedList.isEmpty()) speedList = "" + RPM;
                else {
                    speedList += ", " + RPM;

                }

                double voltage = voltageSensor.getVoltage();
                voltage = (Math.floor(voltage * 1000)) / 1000;

                // Build voltage list
                if (voltageList.isEmpty()) voltageList = "" + voltage;
                else voltageList += ", " + voltage;

                if (flywheelSpeedList.isEmpty()) flywheelSpeedList = "" + RPMFlywheel;
                else {
                    flywheelSpeedList += ", " + RPMFlywheel;
                    dataPoints += 1;
                    telemetry.addData("Data points", dataPoints);
                    telemetry.addData("Flywheel RPM", RPMFlywheel);
                }
            }

            telemetry.addData("RPM (max)", maxRPM);
            telemetry.addData("Voltage", voltageSensor.getVoltage());
            telemetry.update();
        }
    }
}