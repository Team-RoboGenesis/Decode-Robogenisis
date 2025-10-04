package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name = "LaunchTest")
public class LaunchTest extends OpMode {

    private DcMotor potatoCannon = null;
    private DcMotor potatoCannonTwo = null;

    @Override
    public void init() {

        potatoCannon = hardwareMap.get(DcMotor.class, "flywheel");
        potatoCannonTwo = hardwareMap.get(DcMotor.class, "flywheelTwo");

    }

    @Override
    public void loop() {

        if (gamepad1.y)
        {
            potatoCannon.setPower(-1);
            potatoCannonTwo.setPower(1);
        }
        else if (gamepad1.b)
        {
            potatoCannon.setPower(-2/3);
            potatoCannonTwo.setPower(2/3);
        }
        else if (gamepad1.a)
        {
            potatoCannon.setPower(-1/3);
            potatoCannonTwo.setPower(1/3);
        }
        else if (gamepad1.options)
        {
            potatoCannon.setPower(-0);
            potatoCannonTwo.setPower(0);
        }

    }
}