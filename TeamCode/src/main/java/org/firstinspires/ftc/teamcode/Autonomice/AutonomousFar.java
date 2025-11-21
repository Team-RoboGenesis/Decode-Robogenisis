package org.firstinspires.ftc.teamcode.Autonomice;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "AutonomousFar")
public class AutonomousFar extends LinearOpMode {

    private DcMotor flywheel = null;
    private Servo gate = null;

    @Override
    public void runOpMode() throws InterruptedException {

        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        gate = hardwareMap.get(Servo.class, "gate");

        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        double power = 0.55;

        gate.setPosition(0.1);
        waitForStart();
        flywheel.setPower(power);
        Thread.sleep(7000);
        gate.setPosition(0.65);
        Thread.sleep(300);
        gate.setPosition(0.1);
        Thread.sleep(3000);
        gate.setPosition(0.65);
        Thread.sleep(300);
        gate.setPosition(0.1);
        Thread.sleep(3000);
        gate.setPosition(0.65);
        Thread.sleep(300);
        gate.setPosition(0.1);
        flywheel.setPower(-1);
        //gate.setPosition(0.65);
        Thread.sleep(500);
        flywheel.setPower(0);
    }
}
