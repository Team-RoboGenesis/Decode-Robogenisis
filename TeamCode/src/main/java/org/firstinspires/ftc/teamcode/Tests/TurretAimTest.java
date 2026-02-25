package org.firstinspires.ftc.teamcode.Tests;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;

@TeleOp(name="TurretAim")
public class TurretAimTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        Turret turret = new Turret(hardwareMap);


        waitForStart();

        while (opModeIsActive()) {

            // Update Road Runner localization

// inside loop:
            drive.updatePoseEstimate();              // or whatever your update method is called
            Pose2d pose = drive.localizer.getPose();    // or drive.getPose()
            double robotX = pose.position.x;
            double robotY = pose.position.y;

            // NE corner of FTC field
            double targetX = 72;
            double targetY = 72;

            // Angle from robot to corner
            double dx = targetX - robotX;
            double dy = targetY - robotY;
            double angleToCorner = Math.atan2(dy, dx);

            // Button press to aim turret
            if (gamepad1.a) {
                turret.aimToAngle(angleToCorner);
            }

            telemetry.addData("X", robotX);
            telemetry.addData("Y", robotY);
            telemetry.addData("AngleToCornerDeg", Math.toDegrees(angleToCorner));
            telemetry.update();
        }
    }
}