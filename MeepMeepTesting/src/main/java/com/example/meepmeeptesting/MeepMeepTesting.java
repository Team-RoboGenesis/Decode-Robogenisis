package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 50, Math.toRadians(180), Math.toRadians(180), 14.75)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-58, -43, Math.toRadians(-127)))
                .strafeToLinearHeading(new Vector2d(-11, -14), Math.toRadians(180))
//                .stopAndAdd(this::spinUp)
//                // Score three preloaded balls
//                .stopAndAdd(this::shootThreeBalls)
//                // Turn towards spike mark artifacts
//                .stopAndAdd(this::spinIntake)
//                // Intake three balls
                .turn(Math.toRadians(-80))
                .strafeToLinearHeading(new Vector2d(-8, -50), Math.toRadians(90))
//                .waitSeconds(0.3)
//                .stopAndAdd(this::stopIntake)
//                // Drive to shooting position
                .splineTo(new Vector2d(-5, -14), Math.toRadians(190))
//                .stopAndAdd(this::spinIntake)
                // Shoot three balls
//                .stopAndAdd(this::shootThreeBalls)
                // Move to next three balls
//                .stopAndAdd(this::spinIntake)
                // Intake three balls
                .setReversed(true)
                .splineTo(new Vector2d(17, -20), Math.toRadians(-60))
                .splineTo(new Vector2d(17, -55), Math.toRadians(-90))
//                .stopAndAdd(this::stopIntake)
                // Move back to shooting position
                .splineTo(new Vector2d(5, -50), Math.toRadians(90))
                .splineTo(new Vector2d(-5, -14), Math.toRadians(185))
                                .waitSeconds(0.1)
                .setReversed(false)
                .splineToLinearHeading(new Pose2d(35, -20, Math.toRadians(90)), Math.toRadians(45))
//                .setReversed(true)
                .strafeToLinearHeading(new Vector2d(40, -50), Math.toRadians(90))
                                .waitSeconds(0.1)
                .splineTo(new Vector2d(-11, -14), Math.toRadians(185))
//                .stopAndAdd(this::spinIntake)
                // Shoot three balls
//                .stopAndAdd(this::shootThreeBalls)
                // Prepare the robot for TeleOp by stopping the shooter and resetting the turret position
//                .stopAndAdd(this::spinDown)
//                .stopAndAdd(this::turretCenterPos)
//                .strafeToLinearHeading(new Vector2d(3, -14), Math.toRadians(185))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}