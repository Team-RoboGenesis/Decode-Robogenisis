package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;

//@TeleOp(name = "RPM")
public class EncoderTest extends LinearOpMode
{
    private DcMotor flywheel1 = null;
    private DcMotorEx flywheel2;
    private DcMotor intake = null;
    private CRServo actuator1 = null;
    private CRServo actuator2 = null;

    double ticksPerRotation = 25.5;
    private double HIGH_POWER = 0.95;
    private double LOW_POWER = 0.8;
    private double MEDIUM_POWER = 0.9;
    private double OFF = 0;

    private double OPEN = 0.65;
    private double CLOSED = 0.1;

    @Override
    public void runOpMode() throws InterruptedException
    {
        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");

        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);
        actuator1.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad2.cross)
            {
                flywheel1.setPower(LOW_POWER);
            }
            else if (gamepad2.circle)
            {
                flywheel1.setPower(MEDIUM_POWER);
            }
            else if (gamepad2.triangle)
            {
                flywheel1.setPower(HIGH_POWER);
            }
            else if (gamepad2.square)
            {
                flywheel1.setPower(OFF);
            }

            int previousTicks = flywheel1.getCurrentPosition();
            Thread.sleep(100);
            int ticks = flywheel1.getCurrentPosition() - previousTicks;
            double RPM = (ticks/ticksPerRotation) * 600;
            telemetry.addData("RPM: ", RPM);
            telemetry.addData("Encoder ticks: ", flywheel2.getCurrentPosition());
            telemetry.update();
        }
    }

    @TeleOp(name = "MotorInfoGrabber")
    public static class IntakeInfoGrabber extends LinearOpMode {
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
        private String desmosFuncSpeed = null;
        private String desmosFuncVoltage = null;
        private String desmosFuncFlywheelSpeed = null;
        private String voltageList = "";
        private String speedList = "";
        private String flywheelSpeedList = "";

        private double ticksPerRotation = 25.5;
        private double maxRPM = 0;

        double P = 82;
        double F = 12.3474;
        private double RPMFlywheel = 0;
        private double RPM = 0;


        private String funcMaker(String list){
            String[] values = list.split(",");
            StringBuilder desmosTable = new StringBuilder();
            int time = 0; // start at 0 ms
            for (int i = 0; i < values.length; i++) {
                desmosTable.append(time)
                        .append(",")
                        .append(values[i].trim())
                        .append("\n"); // new row for Desmos

                time += 100; // increment by 100 ms
            }
            return desmosTable.toString();
        }
        private void log(String text) {
            FileUtil.appendLine(text);
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
            FileUtil.init(hardwareMap.appContext, "test log");
            // Confirm file writing works
            log("=== START ===");

            while (opModeIsActive()) {

                if (gamepad1.a) { // empty/format the log file
                    FileUtil.overwriteFromIndex("", 0);
                    telemetry.addLine("overwrote file!");

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

                    desmosFuncFlywheelSpeed = funcMaker(flywheelSpeedList);
                    desmosFuncSpeed = funcMaker(speedList);
                    desmosFuncVoltage = funcMaker(voltageList);

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
                    RPMFlywheel = (flywheelTicks / ticksPerRotation) * 600;
                    RPMFlywheel = (Math.floor(RPMFlywheel * 1000)) / 1000;
                    if (RPMFlywheel > maxRPM) maxRPM = RPMFlywheel;
    //                RPMFlywheel /= 500;

                    int ticks = intake.getCurrentPosition() - previousTicks;
                    RPM = (ticks / ticksPerRotation) * 600;
                    RPM = (Math.floor(RPM * 1000)) / 1000;
    //                if (RPM > maxRPM) maxRPM = RPM;
    //                RPM /= 500;

                    // Build speed list
                    if (speedList.isEmpty()) speedList = "" + RPM;
                    else speedList = ", " + RPM;

                    double voltage = voltageSensor.getVoltage();
                    voltage = (Math.floor(voltage * 1000)) / 1000;

                    // Build voltage list
                    if (voltageList.isEmpty()) voltageList = "" + voltage;
                    else voltageList = ", " + voltage;

                    // you get the idea
                    if (flywheelSpeedList.isEmpty()) flywheelSpeedList = "" + RPMFlywheel;
                    else flywheelSpeedList = ", " + RPMFlywheel;
                }

                telemetry.addData("RPM (max)", maxRPM);
                telemetry.addData("Voltage", voltageSensor.getVoltage());
                telemetry.update();
            }
        }
    }
}
