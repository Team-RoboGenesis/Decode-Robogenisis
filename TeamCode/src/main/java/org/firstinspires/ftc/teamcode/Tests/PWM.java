package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "led")
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
        if(gamepad1.a) {
            //random
                led1.setPosition(0.1 + (Math.random() * (0.8 - 0.1)));
                led2.setPosition(0.1 + (Math.random() * (0.8 - 0.1)));
                led3.setPosition(0.1 + (Math.random() * (0.8 - 0.1)));
        }
        if(gamepad1.x){
            //scroll
                led1.setPosition(0.5);
                led2.setPosition(0.8);
                led3.setPosition(0.8);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                led1.setPosition(0.8);
                led2.setPosition(0.5);
                led3.setPosition(0.8);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                led1.setPosition(0.8);
                led2.setPosition(0.8);
                led3.setPosition(0.5);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

        }
        if(gamepad1.b) {
            //RGB lights
                led1.setPosition(delete);
                led2.setPosition(delete);
                led3.setPosition(delete);
                delete += 0.01;
                if (delete >= 0.7) {
                    while(delete >= 0.3){
                        led1.setPosition(delete);
                        led2.setPosition(delete);
                        led3.setPosition(delete);
                        delete += -0.01;
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        }
        if(gamepad1.y){
            while(true){
                //RGB game
                telemetry.addData("A?:", gamepad1.a);
                telemetry.addData("B?:", gamepad1.b);
                telemetry.addData("X?:", gamepad1.x);
                telemetry.update();

//                if (delete >= 0.7) num = -0.01;
//                if (delete <= 0.3) num = 0.01;
                if (!led1Done) led1.setPosition(led1col);
                if (led1Done && !led2Done) led2.setPosition(led3col);
                if (!led3Done && led2Done) led2.setPosition(led2col);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (led1col >= 0.3) led1col += 0.01;
                if (led2col >= 0.3) led2col += 0.01;
                if (led3col >= 0.3) led3col += 0.01;

                if (led1col >= 0.7) led1col = 0.3;
                if (led2col >= 0.7) led2col = 0.3;
                if (led3col >= 0.7) led3col = 0.3;

                if (gamepad1.x) led1Done = true;
                if (gamepad1.a) led2Done = true;
                if (gamepad1.b) led3Done = true;

                if (led1Done && led2Done && led3Done)
                {
                    diff12 = Math.abs(led1col - led2col);
                    diff13 = Math.abs(led1col - led3col);
                    diff23 = Math.abs(led2col - led3col);
                }
                if (diff23 != 0)
                {
                    
                }



//                if(!(ledDis >= 1)) led1.setPosition(delete);
//                if(!(ledDis >= 2)) led2.setPosition(delete);
//                if(!(ledDis >= 3)) led3.setPosition(delete);
//                delete += num;
//                if (delete >= 0.7) num = -0.01;
//                if (delete <= 0.3) num = 0.01;
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                if(gamepad1.x && !(ledDis > 0)) ledDis = 1;
//                if(gamepad1.a && ledDis == 1) ledDis = 2;
//                if(gamepad1.b && ledDis == 2) ledDis = 3;
            }
        }
        telemetry.addData("A?:", gamepad1.a);
        telemetry.addData("B?:", gamepad1.b);
        telemetry.addData("X?:", gamepad1.x);
        telemetry.update();
    }
}
