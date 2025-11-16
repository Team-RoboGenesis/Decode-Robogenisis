package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Spintake text")
public class intakeTests extends OpMode {

    private CRServo spintake = null;
    private DcMotor potatoCannon = null;

    @Override
    public void init() {

        spintake = hardwareMap.get(CRServo.class, "spintake");
        potatoCannon = hardwareMap.get(DcMotor.class, "flywheel");
    }

    @Override
    public void loop() {
        if (gamepad1.a)
        {
            spintake.setPower(1);
        }
        else if(gamepad1.b)
        {
            spintake.setPower(0);
        }
        else if (gamepad1.y)
        {
            spintake.setPower(-1);
        }
        if (gamepad1.left_bumper)
        {
            potatoCannon.setPower(0.01);
        }
        if (gamepad1.right_bumper)
        {
            potatoCannon.setPower(0);
        }
    }
}
