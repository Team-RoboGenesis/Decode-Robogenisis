package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Turret
{

    private DcMotor motor;

    // GoBilda 5203 motor encoder
    private static final double TICKS_PER_REV = 1576;
    private static final double TICKS_PER_RAD = TICKS_PER_REV / (2 * Math.PI);

    // Soft limits in radians
    private static final double LEFT_LIMIT_RAD = Math.toRadians(-180);
    private static final double RIGHT_LIMIT_RAD = Math.toRadians(180);

    // Soft limits in ticks
    private static final int LEFT_LIMIT_TICKS = (int)(LEFT_LIMIT_RAD * TICKS_PER_RAD);
    private static final int RIGHT_LIMIT_TICKS = (int)(RIGHT_LIMIT_RAD * TICKS_PER_RAD);

    public Turret(HardwareMap hardwareMap)
    {
        motor = hardwareMap.get(DcMotor.class, "turret");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setPower(double power)
    {
        motor.setPower(power);
    }

    public void aimToAngle(double angleRad)
    {
        // Clamp angle to soft limits
        if (angleRad < LEFT_LIMIT_RAD) angleRad = LEFT_LIMIT_RAD;
        if (angleRad > RIGHT_LIMIT_RAD) angleRad = RIGHT_LIMIT_RAD;

        // Convert radians → encoder ticks
        int targetTicks = (int)(angleRad * TICKS_PER_RAD);

        // Clamp ticks to soft limits
        if (targetTicks < LEFT_LIMIT_TICKS) targetTicks = LEFT_LIMIT_TICKS;
        if (targetTicks > RIGHT_LIMIT_TICKS) targetTicks = RIGHT_LIMIT_TICKS;

        // Command motor
        motor.setTargetPosition(targetTicks);
        motor.setPower(0.8);  // adjust as needed
    }

    public void setTargetPosition(int pos)
    {
        motor.setTargetPosition(pos);
    }

    public int getCurrentPosition()
    {
        return motor.getCurrentPosition();
    }
}