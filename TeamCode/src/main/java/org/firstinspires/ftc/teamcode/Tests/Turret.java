package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Turret {
    private DcMotorEx turretMotor;

    public Turret(HardwareMap hw) {
        turretMotor = hw.get(DcMotorEx.class, "turret");
        turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void aimToAngle(double targetAngleRad) {
        // Convert angle to encoder ticks — adjust scale for your gear ratio
        double ticks = Math.toDegrees(targetAngleRad) * 10;
        turretMotor.setTargetPosition((int) ticks);
        turretMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretMotor.setPower(0.5);
    }
}
