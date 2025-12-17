package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "TouchpadTest")
public class touchpadTest extends OpMode {

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        telemetry.addData("x", gamepad1.touchpad_finger_1_x);
        telemetry.addData("y", gamepad1.touchpad_finger_1_y);
        telemetry.update();
    }
}
