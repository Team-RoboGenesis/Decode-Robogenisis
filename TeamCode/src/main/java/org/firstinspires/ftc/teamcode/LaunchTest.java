package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "LaunchTest")
public class LaunchTest extends OpMode {

    private DcMotor potatoCannon = null;
    private DcMotor potatoCannonTwo = null;
    private int motor1Direction = 1;
    private int motor2Direction = 0 * motor1Direction * -1;
    private Servo actuator = null;
    private double speed = 0;
    @Override
    public void init() {

        potatoCannon = hardwareMap.get(DcMotor.class, "flywheel");
        potatoCannonTwo = hardwareMap.get(DcMotor.class, "flywheelTwo");
        actuator = hardwareMap.get(Servo.class, "gate");



    }

    @Override
    public void loop() {

//        if (gamepad1.y)
//        { // full power;
//            speed += 0.1;
//        }
//        else if (gamepad1.a)
//        { // 1/3 power
//            speed -= 0.1;
//        }
//        else if (gamepad1.options)
//        { //kill power
//            potatoCannon.setPower(0 * motor1Direction);
//            potatoCannonTwo.setPower(0 * motor2Direction);
//        }
        if (gamepad1.dpad_up) {
            actuator.setPosition(0.1);
        }
        else if (gamepad1.dpad_down) {
            actuator.setPosition(0.65);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            actuator.setPosition(0.1);
        }
        else if (gamepad1.b){
            potatoCannon.setPower(0.6);

        } else if (gamepad1.options) {
            potatoCannon.setPower(0);

        }  else if (gamepad1.y) {
            potatoCannon.setPower(0.7);


        }
        else if (gamepad1.dpad_right) {
            actuator.setPosition(0.1);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            actuator.setPosition(0.65);

        }
//        potatoCannon.setPower(gamepad1.left_stick_y);
        telemetry.addData("power:", potatoCannon.getPower());

    }
}