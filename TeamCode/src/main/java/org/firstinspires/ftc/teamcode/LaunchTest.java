package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name = "LaunchTest")
public class LaunchTest extends OpMode {

    private DcMotor potatoCannon = null;

    @Override
    public void init() {

        potatoCannon = hardwareMap.get(DcMotor.class, "flywheel");

    }

    @Override
    public void loop() {

        if (gamepad1.y)
        {
            potatoCannon.setPower(-1);
        }
        else if (gamepad1.b)
        {
            potatoCannon.setPower(-0.6666666666666666666666666666666666666666666666);
        }
        else if (gamepad1.a)
        {
            potatoCannon.setPower(-0.3333333333333333333333333333333333333333333333);
        }

    }
}