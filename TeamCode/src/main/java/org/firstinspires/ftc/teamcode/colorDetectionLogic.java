package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "clor detect thingy")
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
    private double SLOT_1 = 0;
    private double SLOT_2 = 0.33333;
    private double SLOT_3 = 0.54;
    private char slot1Val = 'n';
    private char slot2Val = 'n';
    private char slot3Val = 'n';
    @Override
    public void init() {
        sort = hardwareMap.get(ColorSensor.class, "sort");

        turnTable = hardwareMap.get(Servo.class, "turnTable");

        turnTable.setPosition(SLOT_1);
    }

    @Override
    public void loop()
    {
        if (green > red + 100)
        {
            isGreenBall = true;
            isPurpleBall = false;
            if (turnTable.getPosition() == SLOT_1)
            {
               slot1Val = 'g';
            }
            else if (turnTable.getPosition() == SLOT_2)
            {
                slot2Val = 'g';
            }
            else if (turnTable.getPosition() == SLOT_3)
            {
                slot3Val = 'g';
            }
        }
        if(blue >= green + 100)
        {
            isPurpleBall = true;
            isGreenBall = false;
            if (turnTable.getPosition() == SLOT_1)
            {
                slot1Val = 'p';
            }
            else if (turnTable.getPosition() == SLOT_2)
            {
                slot2Val = 'p';
            }
            else if (turnTable.getPosition() == SLOT_3)
            {
                slot3Val = 'p';
            }
        }
        else
        {
            isPurpleBall = false;
            isGreenBall = false;
        }

        if(id == 21) {
            motif = "GPP";
        } else if(id == 22) {
            motif = "PGP";
        } else if(id == 23) {
            motif = "PPG";
        }

        if(gamepad1.a)
        {
            turnTable.setPosition(SLOT_3);
        }
        else if (gamepad1.b)
        {
            turnTable.setPosition(SLOT_1);
        }

        telemetry.addData("red value: ", sort.red());
        telemetry.addData("blue value: ", sort.blue());
        telemetry.addData("green value: ", sort.green());
        telemetry.addData("Green slot ", greenInSlot);
        telemetry.addData("purple slots ", purpleInSlot);
        telemetry.addData("motif pattern:", motif);
        telemetry.addData("first slot: ", slot1Val);
        telemetry.addData("second slot: ", slot2Val);
        telemetry.addData("third slot: ", slot3Val);
        telemetry.update();

        red = sort.red();
        blue = sort.blue();
        green = sort.green();
    }
}
