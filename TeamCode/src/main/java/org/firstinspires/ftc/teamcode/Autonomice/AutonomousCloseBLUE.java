package org.firstinspires.ftc.teamcode.Autonomice;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "AutonomousCloseBLUE")
public class AutonomousCloseBLUE extends LinearOpMode {

    private DcMotor flywheel = null;
    private Servo gate = null;
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;

    @Override
    public void runOpMode() throws InterruptedException {

        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        gate = hardwareMap.get(Servo.class, "gate");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");

        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);


        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        double power = 0.567;

        waitForStart();
        leftFront.setPower(-0.45);
        rightFront.setPower(-0.45);
        leftBack.setPower(-0.45);
        rightBack.setPower(-0.45);
        Thread.sleep(1250);
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

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
