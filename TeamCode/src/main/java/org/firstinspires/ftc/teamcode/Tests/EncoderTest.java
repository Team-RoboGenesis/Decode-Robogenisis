package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (name = "HamHamBeefBeefChickenChickenTurkeyTurkeyDuckDuckVenisonVenisoneeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee") //don't ask about the name, please PLEASE
public class EncoderTest extends LinearOpMode
{
    private DcMotor potatoCannon = null;
    double ticksPerRotation = 25.5;

    @Override
    public void runOpMode() throws InterruptedException
    {
        potatoCannon = hardwareMap.get(DcMotor.class, "flywheel");
        potatoCannon.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        potatoCannon.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        waitForStart();

        
        while (opModeIsActive()) {
            if (gamepad1.y)
            {
                potatoCannon.setPower(1);
            }
            else if (gamepad1.x)
            {
                potatoCannon.setPower(0);
            }

            int previousTicks = potatoCannon.getCurrentPosition();
            Thread.sleep(250);
            int ticks = potatoCannon.getCurrentPosition() - previousTicks;
            double RPM = (ticks/ticksPerRotation) * 240;
            telemetry.addData("RPM: ", RPM);
            telemetry.update();
        }
    }
}
