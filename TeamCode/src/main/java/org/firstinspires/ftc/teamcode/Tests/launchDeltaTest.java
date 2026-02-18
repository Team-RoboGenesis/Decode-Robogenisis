package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;
import java.util.ArrayList;

@TeleOp(name = "LaunchDelta")
public class launchDeltaTest extends OpMode {
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private DcMotor flywheel1 = null;
    private DcMotor flywheel2 = null;
    private DcMotor intake = null;
    private DcMotor turret = null;
    private CRServo actuator1 = null;
    private CRServo actuator2 = null;
    private Servo led1 = null;
    private Servo led2 = null;
    private Servo led3 = null;
    Limelight3A limelight = null;
    private String label = null;
    private int launch = 0;
    private static final double GREEN = 0.456;
    private static final double PURPLE = 0.721;
    private static final double HIGH_POWER = 0.65;
    private static final double LOW_POWER = 0.52;
    private static final double MEDIUM_POWER = 0.57;
    private static final double OFF = 0;
    private static final double FAR_SPEED = 3000;
    private static final int SHOOT_POSE = 0;
    private final static int CONVERT_TO_MINUTE = 600;
    private static final double ticksPerRotation = 25.5;
    private double RPM = 0;
    private double twoRPM = 0;
    private double Bdelta = 0;
    private double Adelta = 0;

    List<Double> BRPMList = new ArrayList<>();
    List<Double> ARPMList = new ArrayList<>();

    private void sleep(double milis){
        try {
            Thread.sleep((long) milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean shootBall()
    {
        if (RPM <= FAR_SPEED)
        {
            return false;
        }
        intake.setPower(1);
        actuator1.setPower(-1);
        actuator2.setPower(-1);
        sleep(800);
        actuator1.setPower(0);
        actuator2.setPower(0);
        intake.setPower(0);
        return true;
    }

    private void shootThreeBalls()
    {
        int shootCount = 0;
        int ticks = 0;
        int previousTicks = 0;
        int twoTicks = 0;
        int twoPreviousTicks = 0;
        boolean isSuccessful = false;
        while (shootCount <= 3)
        {
            previousTicks = flywheel1.getCurrentPosition();
            sleep(100);
            ticks = flywheel1.getCurrentPosition() - previousTicks;
            RPM = (ticks / ticksPerRotation) * CONVERT_TO_MINUTE;
            isSuccessful = shootBall();
            twoPreviousTicks = flywheel1.getCurrentPosition();
            sleep(100);
            twoTicks = flywheel1.getCurrentPosition() - twoPreviousTicks;
            twoRPM = (twoTicks / ticksPerRotation) * CONVERT_TO_MINUTE;
            if (isSuccessful)
            {
                shootCount += 1;
                sleep(500);
                ARPMList.add((twoTicks / ticksPerRotation) * CONVERT_TO_MINUTE);
                BRPMList.add((ticks / ticksPerRotation) * CONVERT_TO_MINUTE);
                for(int i = 0; i < launch; i++) {
                    telemetry.addData("RPM of launch " + (i + 1), BRPMList.get(i));
                    telemetry.addData("RPM of launch " + (i + 1), ARPMList.get(i));
                }
                launch++;
                telemetry.update();
            }
        }
    }

    @Override
    public void init() {
        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        turret = hardwareMap.get(DcMotor.class, "turret");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        flywheel1.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    @Override
    public void loop() {
        if(gamepad1.a) {
            shootThreeBalls();
        }
        if (gamepad1.circle)
        {
            flywheel1.setPower(MEDIUM_POWER);
            flywheel2.setPower(MEDIUM_POWER);
        }
        else if (gamepad1.y)
        {
            flywheel1.setPower(HIGH_POWER);
            flywheel2.setPower(HIGH_POWER);
        }
        else if (gamepad1.square)
        {
            flywheel1.setPower(OFF);
            flywheel2.setPower(OFF);
            for(int i = 0; i == BRPMList.size(); i++){
                Bdelta = Bdelta + (BRPMList.get(i - 1) - BRPMList.get(i));
            }
            Bdelta = Bdelta / BRPMList.size();
            telemetry.addData("before delta", Bdelta);
            for(int i = 0; i == ARPMList.size(); i++){
                Adelta = Adelta + (ARPMList.get(i - 1) - ARPMList.get(i));
            }
            Adelta = Adelta / ARPMList.size();
            telemetry.addData("after delta", Adelta);
            telemetry.update();
        }
    }
}
