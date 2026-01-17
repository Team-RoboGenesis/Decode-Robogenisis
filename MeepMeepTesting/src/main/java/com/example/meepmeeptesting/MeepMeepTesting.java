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
                .setConstraints(80, 80, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-54, -46, Math.toRadians(-127)))
                        .strafeToLinearHeading(new Vector2d(-11, -14), Math.toRadians(-90))
                        .waitSeconds(2)
                        .strafeToLinearHeading(new Vector2d(-11, -54), Math.toRadians(-90))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(-11, -14), Math.toRadians(0))
                        .waitSeconds(2)
                        .splineToLinearHeading(new Pose2d(13, -26, Math.toRadians(90)), Math.toRadians(90))
                        .strafeToLinearHeading(new Vector2d(13, 60), Math.toRadians(90))
                        .waitSeconds(0.5)
                        .setTangent(270)
                        .splineToConstantHeading(new Vector2d(13, 40), Math.toRadians(90))
                        .strafeToLinearHeading(new Vector2d(-11, 14), Math.toRadians(90))
                        .waitSeconds(2)
                        .setTangent(0)
                        .splineToLinearHeading(new Pose2d(13, 59, Math.toRadians(120)), Math.toRadians(100))
                        .setReversed(true)
                        .splineToLinearHeading(new Pose2d(-11, 14, Math.toRadians(90)), Math.toRadians(190))
                 .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}