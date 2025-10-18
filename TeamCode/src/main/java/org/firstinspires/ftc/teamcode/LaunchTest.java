package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "LaunchTest")
public class LaunchTest extends OpMode {

    private DcMotor potatoCannon = null;
    private DcMotor potatoCannonTwo = null;
    private int motor1Direction = 1;
    private int motor2Direction = 0 * motor1Direction * -1;
    private Servo actuator = null;

    @Override
    public void init() {

        potatoCannon = hardwareMap.get(DcMotor.class, "flywheel");
        potatoCannonTwo = hardwareMap.get(DcMotor.class, "flywheelTwo");
        actuator = hardwareMap.get(Servo.class, "gate");

    }

    @Override
    public void loop() {

        if (gamepad1.y)
        { // full power
            potatoCannon.setPower(motor1Direction);
            potatoCannonTwo.setPower(motor2Direction);
        }
        else if (gamepad1.b)
        { // 2/3 power
            potatoCannon.setPower(0.6666 * motor1Direction);
            potatoCannonTwo.setPower(0.6666 * motor2Direction);
        }
        else if (gamepad1.a)
        { // 1/3 power
            potatoCannon.setPower(0.3333 * motor1Direction);
            potatoCannonTwo.setPower(0.3333 * motor2Direction);
        }
        else if (gamepad1.options)
        { //kill power
            potatoCannon.setPower(0 * motor1Direction);
            potatoCannonTwo.setPower(0 * motor2Direction);
        }
        else if (gamepad1.dpad_up) {
            actuator.setPosition(0.2);
        }
        else if (gamepad1.dpad_down) {
            actuator.setPosition(0.75);

        }

    }
}