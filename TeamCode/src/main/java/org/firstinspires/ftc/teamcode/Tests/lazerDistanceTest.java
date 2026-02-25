package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "lazer distance test")
public class lazerDistanceTest extends OpMode {
    private DistanceSensor distSensor;
    private Servo LED1;
    private double baseDist = 0;
    private double dist = 0;
    private double threshold = 5;
    private boolean last = false;
    private boolean broken = false;
    private int count = -1; // because it starts at 1 for some reason
    @Override
    public void init() {
        distSensor = hardwareMap.get(DistanceSensor.class, "dist");
        LED1 = hardwareMap.get(Servo.class, "LED1");
        baseDist = distSensor.getDistance(DistanceUnit.CM);
        LED1.setPosition(0.28);
    }

    @Override
    public void loop() {
        dist = distSensor.getDistance(DistanceUnit.CM);
        broken = baseDist >= dist - threshold && baseDist <= dist + threshold;
        if (broken && !last) {
            last = true;
            count++;
        } else if(!broken){
            last = false;
        }
        if(count >= 3) {
            LED1.setPosition(0.4444);
        }
        if(gamepad1.a) reset();
        sleep(10);
    }
    public void reset() {
        count = 0;
        LED1.setPosition(0.28);
    }
    public void sleep(double ms) {
        try {
            Thread.sleep((long) ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
