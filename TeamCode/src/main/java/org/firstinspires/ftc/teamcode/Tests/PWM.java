package org.firstinspires.ftc.teamcode.Tests;

import static java.lang.Math.floor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Lets go gambling")
public class PWM extends OpMode
{

    private Servo led1 = null;
    private Servo led2 = null;
    private Servo led3 = null;
    private double green = 0;
    private double purple = 0.71;
    private double delete = 0.3;
    private int ledDis = 0;
    private double num = 0;

    private double led1col = 0.3;
    private double led2col = 0.3;
    private double led3col = 0.3;
    private boolean led1Done = false;
    private boolean led2Done = false;
    private boolean led3Done = false;
    private double diff12 = 0;
    private double diff13 = 0;
    private double diff23 = 0;
    private boolean isWIn = false;


    @Override
    public void init() {
        led1 = hardwareMap.get(Servo.class, "led1");
        led2 = hardwareMap.get(Servo.class, "led2");
        led3 = hardwareMap.get(Servo.class, "led3");
    }

    @Override
    public void loop() {

        double rigVal = (((gamepad2.touchpad_finger_1_x) + 1) / 2) + 0.01; // less val = more rigged
        int speed = (int) Math.abs(Math.floor(((gamepad2.touchpad_finger_1_y) + 1) * 50)); // less val = more fast
        if(gamepad2.x) rigVal = 0.5; speed = 50;
        if(gamepad2.a) rigVal = 0.1; speed = 1;
        if(gamepad2.b) rigVal = 1; speed = 70;

        //RGB game
        telemetry.addData("led1", led1col);
        telemetry.addData("led2", led2col);
        telemetry.addData("led3", led3col);
        telemetry.addData("speed", speed);
        telemetry.addData("rig value", rigVal);
        telemetry.update();

//                if (delete >= 0.7) num = -0.01;
//                if (delete <= 0.3) num = 0.01;
        led1.setPosition(led1col);
        led3.setPosition(led3col);
        led2.setPosition(led2col);
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!led1Done) led1col += num;
        if (!led2Done) led2col += num;
        if (!led3Done) led3col += num;

        if (led1col >= 0.7 || led2col >= 0.7 || led3col >= 0.7) num = -0.01;
        if (led1col <= 0.3 || led2col <= 0.3 || led3col <= 0.3) num = 0.01;

        if (gamepad1.x) led1Done = true;
        if (gamepad1.a) led2Done = true;
        if (gamepad1.b) led3Done = true;

        if (led1Done && led2Done && led3Done)
        { //math
            diff12 = Math.abs(led1col - led2col);
            diff13 = Math.abs(led1col - led3col);
            diff23 = Math.abs(led2col - led3col);
            //fake loading animation
            led1.setPosition(0);
            led3.setPosition(0);
            led2.setPosition(0);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            led1.setPosition(1);
            led3.setPosition(0);
            led2.setPosition(0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            led1.setPosition(0);
            led3.setPosition(1);
            led2.setPosition(0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            led1.setPosition(0);
            led3.setPosition(0);
            led2.setPosition(1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (diff23 != 0 && diff12 + diff13 + diff23 < rigVal)
            { // winning animation
                led1.setPosition(0.5);
                led3.setPosition(0.5);
                led2.setPosition(0.5);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                led1.setPosition(0);
                led3.setPosition(0);
                led2.setPosition(0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                led1.setPosition(0.5);
                led3.setPosition(0.5);
                led2.setPosition(0.5);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                led1Done = false;
                led2Done = false;
                led3Done = false;
                led1col = 0.3;
                led2col = led1col;
                led3col = led1col;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(!(diff23 != 0 && diff12 + diff13 + diff23 < rigVal))
            { //losing animation
                led1.setPosition(0.3);
                led3.setPosition(0.3);
                led2.setPosition(0.3);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                led1.setPosition(0);
                led3.setPosition(0);
                led2.setPosition(0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                led1.setPosition(0.3);
                led3.setPosition(0.3);
                led2.setPosition(0.3);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                led1Done = false;
                led2Done = false;
                led3Done = false;
                led1col = 0.3;
                led2col = led1col;
                led3col = led1col;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        telemetry.update();
    }
}
