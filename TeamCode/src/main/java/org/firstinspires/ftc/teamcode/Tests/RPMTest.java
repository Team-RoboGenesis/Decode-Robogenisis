package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

public class RPMTest extends OpMode
{

    private DcMotor motor = null;
    double ticksPerRotation = 25.5;

    @Override
    public void init()
    {

        motor = hardwareMap.get(DcMotor.class, "flywheel");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        motor.setPower(1);
//        motor.setTargetPosition(0);
//        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    @Override
    public void loop()
    {
        if (gamepad1.triangle)
        {
            motor.setPower(1);
        }


        int previousTicks = motor.getCurrentPosition();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int ticks = motor.getCurrentPosition() - previousTicks;
        double RPM = (ticks/ticksPerRotation) * 500;
        telemetry.addData("RPM: ", RPM);
        telemetry.addData("Encoder ticks: ", motor.getCurrentPosition());
        telemetry.update();
    }
}
