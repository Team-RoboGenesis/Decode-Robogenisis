package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "lazer distance test")
public class lazerDistanceTest extends LinearOpMode {

    private DistanceSensor distSensor;
    private Servo led1;
    private Servo led2;
    private Servo led3;
    private double baseDist = 0;
    private double dist = 0;
    private final double threshold = 5;
    private boolean last = false;
    private boolean broken = false;
    private boolean isGreenBall = false;
    private boolean isPurpleBall = false;
    private boolean isBall = false;
    private boolean lastCheck = false;
    private int count = -1; // because it starts at 1 for some reason
    private static final double OFF = 0;
    private static final double WHITE = 0.9;


    public void zero() {
        led1.setPosition(OFF);
        led2.setPosition(OFF);
        led3.setPosition(OFF);
    }

    public void one() {
        led1.setPosition(WHITE);
        led2.setPosition(OFF);
        led3.setPosition(OFF);
    }

    public void two() {
        led1.setPosition(WHITE);
        led2.setPosition(WHITE);
        led3.setPosition(OFF);
    }

    public void three() {
        led1.setPosition(WHITE);
        led2.setPosition(WHITE);
        led3.setPosition(WHITE);
    }

    public void reset() {
        count = 0;
    }

    public void sleep(double ms) {
        try {
            Thread.sleep((long) ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        distSensor = hardwareMap.get(DistanceSensor.class, "dist");
        led1 = hardwareMap.get(Servo.class, "led1");
        led2 = hardwareMap.get(Servo.class, "led2");
        led3 = hardwareMap.get(Servo.class, "led3");
        baseDist = distSensor.getDistance(DistanceUnit.CM);
        led1.setPosition(OFF);
        led2.setPosition(OFF);
        led3.setPosition(OFF);

        telemetry.setMsTransmissionInterval(11);

        waitForStart();

        while (opModeIsActive()) {
            dist = distSensor.getDistance(DistanceUnit.CM);
            broken = baseDist >= dist - threshold && baseDist <= dist + threshold;
            isBall = isGreenBall || isPurpleBall;

            if (broken && !last) {
                last = true;
            } else if (!broken && last) {
                count++;
                last = false;
            }

            if (isBall && !lastCheck) {
                lastCheck = true;
                count--;
            } else if (!isBall) {
                lastCheck = false;
            }

            if (count == 0) {
                zero();
            } else if (count == 1) {
                one();
            } else if (count == 2) {
                two();
            } else if (count == 3) {
                three();
            } else if (count < 0) {
                count = 0;
            } else if (count > 3) {
                count = 3;
            }
            if (gamepad1.a) reset();

            telemetry.addData("Distance: ", dist);
            telemetry.addData("Count: ", count);
            telemetry.addData("MS interval: ", telemetry.getMsTransmissionInterval());
            telemetry.addData("Green ball? ", isGreenBall);
            telemetry.addData("Purple ball?", isPurpleBall);
            telemetry.update();
//            sleep(10);
        }
    }
}
