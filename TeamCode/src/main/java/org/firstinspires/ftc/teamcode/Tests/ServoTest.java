package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp (name = "ServoTest")
public class ServoTest extends LinearOpMode
{
    private CRServo maxJr = null;
    private CRServo maxJrJr = null;

    @Override
    public void runOpMode() throws InterruptedException
    {
        maxJr = hardwareMap.get(CRServo.class, "servo");
        maxJrJr = hardwareMap.get(CRServo.class, "servo1");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.cross)
            {
                maxJr.setPower(-1);
                maxJrJr.setPower(-1);
            }
            else if (gamepad1.triangle)
            {
                maxJr.setPower(1);
                maxJrJr.setPower(1);
            }
            else if (gamepad1.circle)
            {
                maxJr.setPower(0);
                maxJrJr.setPower(0);
            }
        }
    }
}
