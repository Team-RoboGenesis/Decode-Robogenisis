package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "Potato")
public class StormTeleop extends OpMode
{
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private Servo mainIntake = null;
    private DcMotor slides = null;
    private  Servo pivot = null;
    private DcMotor leftIntakeArm = null;
    private DcMotor rightIntakeArm = null;
    private DcMotor hangArm = null;
    ColorRangeSensor color;

    int MAX_COLOR = 500;
    int HIGH_SPEED = 400;
    int ROUND_TO_INT = 100;
    int HANG_EXTEND = 2830;
    int SLIDES_EXTEND = 1700;
    int ARM_LIMIT = 3400;
    int SLIDES_RETRACT = 0;

    @Override
    public void init()
    {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        mainIntake = hardwareMap.get(Servo.class, "mainIntake");
        slides = hardwareMap.get(DcMotor.class, "slides");
        pivot = hardwareMap.get(Servo.class, "goBildaPivot");
        leftIntakeArm = hardwareMap.get(DcMotor.class, "leftIntakeArm");
        rightIntakeArm  = hardwareMap.get(DcMotor.class, "rightIntakeArm");
        hangArm = hardwareMap.get(DcMotor.class, "hangArm");
        color = hardwareMap.get(ColorRangeSensor.class, "color");

        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftIntakeArm.setDirection(DcMotorSimple.Direction.REVERSE);
        rightIntakeArm.setDirection(DcMotorSimple.Direction.REVERSE);
        slides.setDirection(DcMotorSimple.Direction.REVERSE);

        leftIntakeArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightIntakeArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftIntakeArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightIntakeArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftIntakeArm.setTargetPosition(0);
        rightIntakeArm.setTargetPosition(0);
        rightIntakeArm.setPower(1);
        leftIntakeArm.setPower(1);
        leftIntakeArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightIntakeArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slides.setTargetPosition(0);
        slides.setPower(0.5);
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hangArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hangArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hangArm.setTargetPosition(0);
        hangArm.setPower(0.5);
        hangArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void setArmPos(int position)
    {
        if (position < 0)
        {
            rightIntakeArm.setTargetPosition(0);
            leftIntakeArm.setTargetPosition(0);
        } else if (position > ARM_LIMIT)
        {
            rightIntakeArm.setTargetPosition(ARM_LIMIT);
            leftIntakeArm.setTargetPosition(ARM_LIMIT);
        }
        else
        {
          leftIntakeArm.setTargetPosition(position);
          rightIntakeArm.setTargetPosition(position);
        }
    }

    public void setSlidesPos(int position)
    {
        if (position < 0)
        {
            slides.setTargetPosition(0);
        }
        else if (position > SLIDES_EXTEND)
        {
            slides.setTargetPosition(SLIDES_EXTEND);
        }
        else
        {
            slides.setTargetPosition(position);
        }
    }

    @Override
    public void loop()
    {
        double y = gamepad1.left_stick_y;
        double x = -gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;
        int slidePos = (int) (slides.getCurrentPosition() + (-gamepad2.right_stick_y * ROUND_TO_INT));
        int armPos = (int) (rightIntakeArm.getCurrentPosition() + (-gamepad2.left_stick_y * ROUND_TO_INT * 2));
        int hangPos = (int) (hangArm.getCurrentPosition() + (-gamepad2.left_trigger * HIGH_SPEED + gamepad2.right_trigger * HIGH_SPEED));

        //mecanuum drive
        leftFront.setPower(y + x + rx);
        leftBack.setPower(y - x + rx);
        rightFront.setPower(y - x - rx);
        rightBack.setPower(y + x - rx);

        setArmPos(armPos);
        setSlidesPos(slidePos);
        hangArm.setTargetPosition(hangPos);

        telemetry.addData("hamPizza", slides.getTargetPosition());
        telemetry.update();
    }
}
