package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Spintake text")
public class intakeTests extends OpMode {

    private DcMotor potatoCannon = null;

    @Override
    public void init() {
        potatoCannon = hardwareMap.get(DcMotor.class, "flywheel");
    }

    @Override
    public void loop() {
        if (gamepad1.triangle)
        {
            potatoCannon.setPower(0.5);
        }
        if (gamepad1.square)
        {
            potatoCannon.setPower(0);
        }
        if (gamepad1.cross)
        {
            potatoCannon.setPower(-0.5);
        }
    }
}
