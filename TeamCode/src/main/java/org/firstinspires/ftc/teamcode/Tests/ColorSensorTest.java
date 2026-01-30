package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//@TeleOp
public class ColorSensorTest extends OpMode {

    public boolean yellow() { // see if yellow is the color of sample in claw
        return color.getDistance(DistanceUnit.MM) < grabDistance & color.green() > color.blue() & color.green() > color.red() & color.green() > maxColor;
    } public boolean red() { // see if red is the color of sample in claw
        return color.getDistance(DistanceUnit.MM) < grabDistance & color.red() > color.green() & color.red() > color.blue() & color.red() > maxColor;
    } public boolean blue() { // see if blue is the color of sample in claw
        return color.getDistance(DistanceUnit.MM) < grabDistance & color.blue() > color.red() & color.blue() > color.green() & color.blue() > maxColor;
    } public boolean purple() { // run on purple
        return color.getDistance(DistanceUnit.MM) > grabDistance;
    }
    int grabDistance = 30;
    int maxColor = 500;
    ColorRangeSensor color;
    public Servo led1 = null;
    @Override
    public void init() {
        led1 = hardwareMap.get(Servo.class, "LED1");
        color = hardwareMap.get(ColorRangeSensor.class, "color");
    }

    @Override
    public void loop() {
        if (yellow()) {
            led1.setPosition(0.35);//yellow
        } else if (red()) {
            led1.setPosition(0.28);//red
        } else if (blue()) {
            led1.setPosition(0.6);//blue
        } else if (purple()) {
            led1.setPosition(0.71);//purple
        }
    }
}
