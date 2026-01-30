package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.R;

@TeleOp(name = "Spintake text")
public class intakeTests extends OpMode {

    private DcMotor spintake = null;
    private CRServo maxJr = null;
    private CRServo maxJrJr = null;

    private DcMotor flywheel1 = null;
    private DcMotor flywheel2 = null;
    private DcMotor turret = null;

    private static final double HIGH_POWER = 0.7;
    private static final double LOW_POWER = 0.5;
    private static final double MEDIUM_POWER = 0.6;
    private static final double OFF = 0;

    @Override
    public void init() {

        spintake = hardwareMap.get(DcMotor.class, "intake");
        maxJr = hardwareMap.get(CRServo.class, "servo");
        maxJrJr = hardwareMap.get(CRServo.class, "servo1");

        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        turret = hardwareMap.get(DcMotor.class, "turret");

        spintake.setDirection(DcMotorSimple.Direction.REVERSE);

        maxJr.setDirection(DcMotorSimple.Direction.REVERSE);
        maxJrJr.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        turret.setPower(0.7);
//        turret.setTargetPosition(0);
//        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void loop() {
        if (gamepad1.right_trigger > 0.001)
        {
            spintake.setPower(1);
        }
        else if(!(gamepad1.right_trigger > 0.001) && !(gamepad1.left_trigger > 0.001))
        {
            spintake.setPower(0);
        }
        else if (gamepad1.left_trigger > 0.001)
        {
            spintake.setPower(-1);
        }

        if (gamepad1.dpad_down)
        {
            maxJr.setPower(-1);
            maxJrJr.setPower(-1);
        }
        else if (gamepad1.dpad_up)
        {
            maxJr.setPower(1);
            maxJrJr.setPower(1);
        }
        else if (!gamepad1.dpad_down && !gamepad1.dpad_up)
        {
            maxJr.setPower(0);
            maxJrJr.setPower(0);
        }
        if (gamepad2.cross)
        {
            flywheel1.setPower(LOW_POWER);
            flywheel2.setPower(LOW_POWER);
        }
        else if (gamepad1.circle)
        {
            flywheel1.setPower(MEDIUM_POWER);
            flywheel2.setPower(MEDIUM_POWER);
        }
        else if (gamepad1.triangle)
        {
            flywheel1.setPower(HIGH_POWER);
            flywheel2.setPower(HIGH_POWER);
        }
        else if (gamepad1.square)
        {
            flywheel1.setPower(OFF);
            flywheel2.setPower(OFF);
        }

        telemetry.addData("Turret", turret.getCurrentPosition());
        telemetry.update();
    }
}
