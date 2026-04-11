package org.firstinspires.ftc.teamcode.Tests;

import android.content.Context;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Write to SD Card", group = "Examples")
public class WriteTextFileExample extends LinearOpMode {

    @Override
    public void runOpMode() {

        waitForStart();

        if (opModeIsActive()) {

            String textToWrite = "Saved on microSD card!";

            try {
                // Get all external storage locations
                File[] dirs = hardwareMap.appContext.getExternalFilesDirs(null);

                File sdCardDir = null;

                // dirs[0] = internal storage
                // dirs[1] (if exists) = SD card
                if (dirs.length > 1 && dirs[1] != null) {
                    sdCardDir = dirs[1];
                } else {
                    // Fallback if SD card not available
                    sdCardDir = dirs[0];
                }

                File file = new File(sdCardDir, "sd_example.txt");

                FileWriter writer = new FileWriter(file);
                writer.write(textToWrite);
                writer.close();

                telemetry.addData("Status", "File written!");
                telemetry.addData("Path", file.getAbsolutePath());

            } catch (IOException e) {
                telemetry.addData("Error", e.getMessage());
            }

            telemetry.update();
            sleep(5000);
        }
    }
}