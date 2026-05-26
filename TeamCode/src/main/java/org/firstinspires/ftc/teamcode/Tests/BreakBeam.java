package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

//@TeleOp(name = "beamBreak")
public class BreakBeam extends LinearOpMode {

    private DigitalChannel beamBreak;

    @Override
    public void runOpMode() throws InterruptedException {

        beamBreak = hardwareMap.get(DigitalChannel.class, "turtle");
        beamBreak.setMode(DigitalChannel.Mode.OUTPUT);

        waitForStart();

        while (opModeIsActive()) {

            boolean state = beamBreak.getState();  // true = unbroken, false = broken

            telemetry.addData("Beam broken?", !state);
            telemetry.addData("Raw state", state);
            telemetry.update();
        }
    }
}

