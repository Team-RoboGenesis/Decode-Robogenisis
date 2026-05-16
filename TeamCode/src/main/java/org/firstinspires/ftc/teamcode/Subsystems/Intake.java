package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private DcMotor intakeMotor;

    public Intake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    // Run intake forward (collect)
    public void in() {
        intakeMotor.setPower(1);
    }

    // Run intake backward (eject)
    public void out() {
        intakeMotor.setPower(-1);
    }

    // Stop intake
    public void stop() {
        intakeMotor.setPower(0);
    }

    // Optional: set custom power
    public void setPower(double power) {
        intakeMotor.setPower(power);
    }
}
