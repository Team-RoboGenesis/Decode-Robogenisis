package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Tests.FileUtil;

@TeleOp(name = "MotorInfoGrabber")
public class IntakeInfoGrabber extends LinearOpMode {

    private DcMotor intake;
    private VoltageSensor voltageSensor;

    private boolean dataCollection = false;

    private String voltageList = "";
    private String speedList = "";

    private double ticksPerRotation = 25.5;
    private double maxRPM = 0;

    // ✅ Helper function to keep code clean
    private void log(String text) {
        FileUtil.writeToSDCard(hardwareMap.appContext, text, true);
    }

    @Override
    public void runOpMode() throws InterruptedException {

        intake = hardwareMap.get(DcMotor.class, "intake");
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addLine("Ready to start");
        telemetry.update();

        waitForStart();

        // ✅ Confirm file writing works
        log("=== START ===");

        while (opModeIsActive()) {

            if (gamepad1.a) {
                telemetry.addLine("Logging marker...");
                log("START");
            }

            if (gamepad1.b) {
                intake.setPower(1);
                dataCollection = true;
            }

            if (gamepad1.x) {
                intake.setPower(0);
            }

            if (gamepad1.y) {
                dataCollection = false;
                intake.setPower(0);

                log("max RPM: " + maxRPM);
                log("");

                log("speed list:");
                log(speedList);

                log("");
                log("voltage list:");
                log(voltageList);

                telemetry.addLine("Data saved!");
            }

            if (dataCollection) {
                int previousTicks = intake.getCurrentPosition();

                sleep(100);

                int ticks = intake.getCurrentPosition() - previousTicks;
                double RPM = (ticks / ticksPerRotation) * 600;

                if (RPM > maxRPM) maxRPM = RPM;

                // Build speed list
                if (speedList.isEmpty()) speedList = "" + RPM;
                else speedList += ", " + RPM;

                double voltage = voltageSensor.getVoltage();

                // Build voltage list
                if (voltageList.isEmpty()) voltageList = "" + voltage;
                else voltageList += ", " + voltage;
            }

            telemetry.addData("RPM (max)", maxRPM);
            telemetry.addData("Voltage", voltageSensor.getVoltage());
            telemetry.update();
        }
    }
}