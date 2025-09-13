package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class colorDetectionLogic extends OpMode  {
    private double red = 0;
    private double blue = 0;
    private double green = 0;
    private boolean isGreenBall = false;
    private boolean isPurpleBall = false;
    private String motif = null;
    private int id = 22;
    private int purpleInSlot = 0;
    private int greenInSlot = 0;
    ColorSensor sort;
    private Servo turnTable;
    @Override
    public void init() {
        sort = hardwareMap.get(ColorSensor.class, "sort");

        turnTable = hardwareMap.get(Servo.class, "turnTable");

        turnTable.setPosition(0.3333333);
    }

    @Override
    public void loop() {
        for(int run = 0; run < 3; run++) {
            if (green > red + 100) {
                if (run == 1) {
                    greenInSlot = 1;
                }
                else if (run == 2) {
                    greenInSlot = 2;
                }
                else {
                    greenInSlot = 3;
                }
            }


            if (blue > 900 && blue < 2000 && green < 1000) {
                if (run == 1) {
                    purpleInSlot++;
                }
                else if (run == 2) {
                    purpleInSlot = purpleInSlot + 2;
                }
                else {
                    purpleInSlot = 3 + purpleInSlot;
                }
            }

            if (purpleInSlot < 10) purpleInSlot = purpleInSlot * 10;
            if (run == 1) {
                turnTable.setPosition(0.5);
            } else if (run == 2) {
                turnTable.setPosition(0.88888);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        if(id == 21) {
            motif = "GPP";
        } else if(id == 22) {
            motif = "PGP";
        } else if(id == 23) {
            motif = "PPG";
        }


        telemetry.addData("red: ", sort.red());
        telemetry.addData("blue: ", sort.blue());
        telemetry.addData("green: ", sort.green());
        telemetry.addData("Green slot ", greenInSlot);
        telemetry.addData("purple slots ", purpleInSlot);
        telemetry.addData("motif pattern:", motif);
        telemetry.update();

        red = sort.red();
        blue = sort.blue();
        green = sort.green();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        requestOpModeStop();
    }
}
