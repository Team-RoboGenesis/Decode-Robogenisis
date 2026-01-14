package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "RPM") //don't ask about the name, please PLEASE
public class EncoderTest extends LinearOpMode
{
    private DcMotor motor = null;
    private Servo actuator = null;

    double ticksPerRotation = 25.5;
    private double HIGH_POWER = 0.7;
    private double LOW_POWER = 0.6;
    private double MEDIUM_POWER = 0.65;
    private double OFF = 0;

    private double OPEN = 0.65;
    private double CLOSED = 0.1;

    @Override
    public void runOpMode() throws InterruptedException
    {
        motor = hardwareMap.get(DcMotor.class, "flywheel");
        actuator = hardwareMap.get(Servo.class, "gate");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();


        while (opModeIsActive()) {
            if (gamepad2.cross)
            {
                motor.setPower(LOW_POWER);
            }
            else if (gamepad2.circle)
            {
                motor.setPower(MEDIUM_POWER);
            }
            else if (gamepad2.triangle)
            {
                motor.setPower(HIGH_POWER);
            }
            else if (gamepad2.square)
            {
                motor.setPower(OFF);
            }

            else if (gamepad2.dpad_down) {
                actuator.setPosition(OPEN);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                actuator.setPosition(CLOSED);
            }

            int previousTicks = motor.getCurrentPosition();
            Thread.sleep(100);
            int ticks = motor.getCurrentPosition() - previousTicks;
            double RPM = (ticks/ticksPerRotation) * 560;
            telemetry.addData("RPM: ", RPM);
            telemetry.addData("Encoder ticks: ", motor.getCurrentPosition());
            telemetry.update();
        }
    }
}
