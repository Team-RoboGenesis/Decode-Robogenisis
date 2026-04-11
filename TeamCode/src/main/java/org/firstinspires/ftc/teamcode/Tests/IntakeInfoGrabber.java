package org.firstinspires.ftc.teamcode.Tests;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;


@TeleOp(name = "MotorInfoGrabber")
public class IntakeInfoGrabber extends LinearOpMode {
    public int number = 0;
    public VoltageSensor voltageSensor = null;
    private DcMotor intake = null;
    private boolean dataCollection = false;
    private String desmosFuncVoltage = "";
    private String desmosFuncSpeed = "";
    private String voltageList = "";
    private String speedList = "";
    public double ticksPerRotation = 25.5;
    private double maxRPM = 0;



    @Override
    public void runOpMode() throws InterruptedException {
        intake = hardwareMap.get(DcMotor.class, "intake");
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        FileUtil.init(hardwareMap.appContext);

        waitForStart();




        FileUtil.appendLine("=== START ===");

        for (int i = 0; i < 10; i++) {
            FileUtil.appendLine("Line " + i);
            sleep(200);
        }



        FileUtil.appendLine("START");
        
        while (opModeIsActive()) {
            if(gamepad1.a){ // empty log file
                telemetry.addLine("STARTED");
                telemetry.update();
                FileUtil.appendLine("START");
            } else if (gamepad1.b) { // spin up motor and start info collection
                intake.setPower(1);
                dataCollection = true;
            } else if (gamepad1.x) {
                intake.setPower(0);
            } else if (gamepad1.y) { // spin down motor, finish file, and end program
                dataCollection = false;
                intake.setPower(0);
                // make voltage list a desmos table
                String[] values = voltageList.split(",");
                StringBuilder desmosTable = new StringBuilder();
                int time = 0; // start at 0 ms
                for (int i = 0; i < values.length; i++) {
                    desmosTable.append(time)
                            .append(",")
                            .append(values[i].trim())
                            .append("\n"); // new row for Desmos

                    time += 100; // increment by 100 ms
                }
                desmosFuncSpeed = desmosTable.toString();
                // make speed list a desmos table
                values = speedList.split(",");
                desmosTable = new StringBuilder();
                time = 0; // start at 0 ms
                for (int i = 0; i < values.length; i++) {
                    desmosTable.append(time)
                            .append(",")
                            .append(values[i].trim())
                            .append("\n"); // new row for Desmos

                    time += 100; // increment by 100 ms
                }
                desmosFuncVoltage = desmosTable.toString();
                // output to SD card
                FileUtil.appendLine("max RPM: " + maxRPM);
                FileUtil.appendLine("");
                FileUtil.appendLine("speed list: ");
                FileUtil.appendLine(desmosFuncSpeed);
                FileUtil.appendLine("");
                FileUtil.appendLine("");
                FileUtil.appendLine("");
                FileUtil.appendLine("voltage list: ");
                FileUtil.appendLine(desmosFuncVoltage);
            }
            if(dataCollection) {
                int previousTicks = intake.getCurrentPosition();
                sleep(100);
                int ticks = intake.getCurrentPosition() - previousTicks;
                double RPM = (ticks / ticksPerRotation) * 600;
                if (RPM > maxRPM) maxRPM = RPM;
                speedList = speedList + ", " + RPM;
                double voltage = voltageSensor.getVoltage();
                voltageList = voltageList + ", " + voltage;
            }
        }
    }
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
