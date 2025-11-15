package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
 @TeleOp (name = "MechanumDirectionDebugger")
public class MechanumDirectionDebugger extends OpMode

{
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private DcMotor potatoCannon = null;
    private DcMotor potatoCannonTwo = null;
    //    private DcMotor leftFlywheel = null;
//    private DcMotor rightFlywheel = null;
//    private Servo stopper = null;
//    private Servo led = null;
    private Limelight3A limelight;
    //    private Servo limeAlign = null;
    private Servo led1 = null;
    private Servo led2 = null;
    private Servo led3 = null;
    private Servo actuator = null;
    private double servoPos = 0;
    private double targetPos = 0;
    private double GREEN = 0.456;
    private double PURPLE = 0.721;

        // Declare our motors
        // Make sure your ID's match your configuration


    @Override
    public void init()
    {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");

        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop()
    {
        if (gamepad1.y)
        {
            leftFront.setPower(0.5);
        }

        else if (gamepad1.b)
        {
            leftBack.setPower(0.5);
        }
        else if (gamepad1.a)
        {
            rightBack.setPower(0.5);
        }
        else if (gamepad1.x)
        {
            rightFront.setPower(0.5);
        }

    }
}
