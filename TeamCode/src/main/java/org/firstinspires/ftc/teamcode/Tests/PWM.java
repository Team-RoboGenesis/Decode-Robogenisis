package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
    private double delete = 0;

    @Override
    public void init() {
        led1 = hardwareMap.get(Servo.class, "led1");
        led2 = hardwareMap.get(Servo.class, "led2");
        led3 = hardwareMap.get(Servo.class, "led3");
    }

    @Override
    public void loop() {
        if(gamepad1.a) {
            while(true) {
                led1.setPosition(0.1 + (Math.random() * (0.8 - 0.1)));
                led2.setPosition(0.1 + (Math.random() * (0.8 - 0.1)));
                led3.setPosition(0.1 + (Math.random() * (0.8 - 0.1)));
            }
        }
        if(gamepad1.y){
            //scroll
            while(true){
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
        }
        if(gamepad1.b) {
            while(true) {
                led1.setPosition(delete);
                led2.setPosition(delete);
                led3.setPosition(delete);
                delete += 0.01;
                if (delete > 0.7) delete = 0;
                if (delete < 0.3) delete = 0.3;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                led1.setPosition(purple);
                led2.setPosition(purple);
                led3.setPosition(purple);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
