package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

//@TeleOp(name = "RotationTest")
public class RotationTest extends OpMode {
    private DcMotor leftDrive = null;

    @Override
    public void init() {
        leftDrive = hardwareMap.get(DcMotor.class, "flywheel");
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            leftDrive.setPower(1);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            leftDrive.setPower(0);
        }
    }
}
