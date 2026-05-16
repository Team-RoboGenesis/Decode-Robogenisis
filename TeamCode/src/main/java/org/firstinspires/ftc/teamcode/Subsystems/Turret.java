package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Turret {
    private DcMotor motor;

    // GoBilda 5203 motor encoder
    private static final double TICKS_PER_REVOLUTION = 1576.0;
    private static final double TICKS_PER_RAD = TICKS_PER_REVOLUTION / (2.0 * Math.PI);
    private static final double RAD_PER_TICK = 1.0 / TICKS_PER_RAD;

    // Soft limits in radians
    private static final double LEFT_LIMIT_RAD = Math.toRadians(-160);
    private static final double RIGHT_LIMIT_RAD = Math.toRadians(170);

    // Soft limits in ticks
    private static final int LEFT_LIMIT_TICKS = (int) (LEFT_LIMIT_RAD * TICKS_PER_RAD);
    private static final int RIGHT_LIMIT_TICKS = (int) (RIGHT_LIMIT_RAD * TICKS_PER_RAD);

    public Turret(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, "turret");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // better for custom PID power control
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setPower(double power) {
        motor.setPower(power);
    }

    public void aimToAngle(double angleRad) {
        angleRad = clamp(angleRad, LEFT_LIMIT_RAD, RIGHT_LIMIT_RAD);

        int targetTicks = (int) (angleRad * TICKS_PER_RAD);
        targetTicks = clamp(targetTicks, LEFT_LIMIT_TICKS, RIGHT_LIMIT_TICKS);

        motor.setTargetPosition(targetTicks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(1);
    }

    public double getCurrentAngle() {
        return motor.getCurrentPosition() * RAD_PER_TICK;
    }

    public int angleToTicks(double angleRad) {
        angleRad = clamp(angleRad, LEFT_LIMIT_RAD, RIGHT_LIMIT_RAD);
        return (int) (angleRad * TICKS_PER_RAD);
    }

    public void setTargetPosition(int pos) {
        pos = clamp(pos, LEFT_LIMIT_TICKS, RIGHT_LIMIT_TICKS);
        motor.setTargetPosition(pos);
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    public void useRawPowerMode() {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getLeftLimitRad() {
        return LEFT_LIMIT_RAD;
    }

    public double getRightLimitRad() {
        return RIGHT_LIMIT_RAD;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}