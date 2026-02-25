package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "advanced counter test")
public class advancedCounterTest extends OpMode {
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
    private DigitalChannel beamBreak1 = null;
    Limelight3A limelight = null;
    private boolean beam1 = false;

    @Override
    public void init() {
        /*
        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        turret = hardwareMap.get(DcMotor.class, "turret");
        actuator1 = hardwareMap.get(CRServo.class, "servo");
        actuator2 = hardwareMap.get(CRServo.class, "servo1");
        */
        beamBreak1 = hardwareMap.get(DigitalChannel.class, "beamBreak1");

        /*
        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        */
        beamBreak1.setMode(DigitalChannel.Mode.INPUT);
//        flywheel1.setDirection(DcMotorSimple.Direction.REVERSE);

        //flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        beam1 = beamBreak1.getState(); // true when beam broken
        telemetry.addData("beam 1", beam1);
        telemetry.update();
    }
}
