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
                .turn(Math.toRadians(-80))
                .strafeToLinearHeading(new Vector2d(-8, -50), Math.toRadians(90))
                .waitSeconds(0.3)
                .splineTo(new Vector2d(-5, -14), Math.toRadians(190))
                .setReversed(true)
                .splineTo(new Vector2d(17, -20), Math.toRadians(-60))
                .splineTo(new Vector2d(20, -55), Math.toRadians(-90))
                .waitSeconds(0.3)
                .splineTo(new Vector2d(-5, -14), Math.toRadians(185))
                .waitSeconds(0.3)
                .turn(Math.toRadians(-120))
//                        .setTangent(Math.toRadians(0))
                        .splineToConstantHeading(new Vector2d(14, -56), Math.toRadians(60))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}