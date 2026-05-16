package org.firstinspires.ftc.teamcode.Tests;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;

//@TeleOp(name="TurretAim")
public class TurretAimTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, Math.toRadians(90)));
        Turret turret = new Turret(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {

            // Update Road Runner localization
            drive.updatePoseEstimate();
            Pose2d pose = drive.localizer.getPose();

            double robotX = pose.position.x;
            double robotY = pose.position.y;
            double robotHeading = pose.heading.toDouble();

            //Goal pos
            double targetX = -72;
            double targetY = 72;

            // Angle from robot to corner (field frame)
            double dx = targetX - robotX;
            double dy = targetY - robotY;
            double angleToCorner = Math.atan2(dy, dx);

            // Convert to robot-relative turret angle
            double turretAngle = angleToCorner - robotHeading;

            // Normalize to [-PI, PI]
            turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));

            // Limits to restrict turret to 180 degrees in either direction
            double maxAngle = Math.toRadians(180);
            double minAngle = Math.toRadians(-180);

            if (turretAngle > maxAngle) turretAngle = maxAngle;
            if (turretAngle < minAngle) turretAngle = minAngle;

            // Button press to aim turret
            if (gamepad1.a) {
                turret.aimToAngle(turretAngle);
            }


            telemetry.addData("X", robotX);
            telemetry.addData("Y", robotY);
            telemetry.addData("RobotHeadingDeg", Math.toDegrees(robotHeading));
            telemetry.addData("AngleToCornerDeg", Math.toDegrees(angleToCorner));
            telemetry.addData("TurretAngleDeg", Math.toDegrees(turretAngle));
            telemetry.update();
        }
    }
}