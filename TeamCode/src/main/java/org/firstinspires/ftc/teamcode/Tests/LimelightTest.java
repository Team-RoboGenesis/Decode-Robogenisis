package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@TeleOp(name = "limelightTest")
public class LimelightTest extends LinearOpMode {

    private Limelight3A limelight;
    private DcMotor turret = null;

    @Override
    public void runOpMode() throws InterruptedException {


        limelight = hardwareMap.get(Limelight3A.class, "Benny");
        turret = hardwareMap.get(DcMotor.class, "turret");

        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setPower(1);
        turret.setTargetPosition(0);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.setMsTransmissionInterval(5);
        limelight.pipelineSwitch(0);

        /*
         * Starts polling for data.
         */
        limelight.start();


        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            if (result != null) {
                if (result.isValid()) {

                    double distance = getDistanceFromTag(result.getTa());
                    Pose3D botpose = result.getBotpose();
//                    telemetry.addData("Distance in CM", distance);
                    turret.setTargetPosition((int) Math.floor(result.getTx() * 2.7));
                    telemetry.addData("Turret: ", turret.getCurrentPosition());
                    telemetry.addData("tx", result.getTx());
                    telemetry.addData("ty", result.getTy());
                    telemetry.addData("Botpose", botpose.toString());
                    telemetry.addData("april Id", result.getBarcodeResults());
                    telemetry.addData(">", "Robot Ready.  Press Play.");
                    telemetry.addData("Target area: ", result.getTa());
                    telemetry.update();

                }
            }
        }
    }

    public double getDistanceFromTag(double ta)
    {
        double scale = 240.425;
        double distance = (scale / ta);
        return distance;
    }
}

