package org.firstinspires.ftc.teamcode.Tests;/*
This is a SD card manipulator for FTC vibe coded by team #26235 robogenisis

how to use:

reading: FileUtil.readRange(starting byte, ending byte);
appending: FileUtil.appendLine(String);
writing from an index: FileUtil.writeStringAtIndex(String, starting byte);
truncating writing from an index: FileUtil.overwriteFromIndex(String, starting byte);

IMPORTANT:
You MUST call:
FileUtil.init(hardwareMap.appContext);

before using any functions
 */
import android.content.Context;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void writeToSDCard(Context context, String text, boolean append) {
        try {
            // Get all external storage directories
            File[] dirs = context.getExternalFilesDirs(null);

            File targetDir;

            // Use SD card if available
            if (dirs.length > 1 && dirs[1] != null) {
                targetDir = dirs[1];
            } else {
                targetDir = dirs[0]; // fallback
            }

            // Create file
            File file = new File(targetDir, "sd_log.txt");

            // Write to file (append or overwrite)
            FileWriter writer = new FileWriter(file, append);
            writer.write(text + "\n");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}