package org.firstinspires.ftc.teamcode.Tests;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "storm color sensor test")
public class stormColorSensorTest extends OpMode{
    ColorSensor CS1;
    ColorSensor CS2;
    private double red1 = 0;
    private double blue1 = 0;
    private double green1 = 0;
    private double red2 = 0;
    private double blue2 = 0;
    private double green2 = 0;
    private String ball1 = null;
    private String ball2 = null;
    private boolean full = false;
    private boolean readyToFire = false;

    @Override
    public void init() {
        CS1 = hardwareMap.get(ColorSensor.class, "colorSensor1");
        CS2 = hardwareMap.get(ColorSensor.class, "colorSensor2");
    }

    @Override
    public void loop() {
        // ball logic
        if (green1 > red1 + 100) {
            ball1 = "green";
        } else if (blue1 >= green1 + 100) {
            ball1 = "purple";
        } else {
            ball1 = "no ball detected";
        }

        if (green2 > red2 + 100) {
            ball2 = "green";
        } else if (blue2 >= green2 + 100) {
            ball2 = "purple";
        } else {
            ball2 = "no ball detected";
        }
        //telemetry logic
        if(!(ball1.equals("no ball detected")) && !(ball2.equals("no ball detected"))) {
            full = true;
            readyToFire = true;
        } else {
            if(!(ball2.equals("no ball detected"))) {
                readyToFire = true;
            } else {
                readyToFire = false;
            }
            full = false;
        }




        telemetry.addData("position 1:", ball1);
        telemetry.addData("position 3:", ball2);
        telemetry.addData("ready to fire:", readyToFire);
        telemetry.addData("full:", full);
        telemetry.update();

        red1 = CS1.red();
        blue1 = CS1.blue();
        green1 = CS1.green();
        red2 = CS2.red();
        blue2 = CS2.blue();
        green2 = CS2.green();
    }
}
