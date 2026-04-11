package org.firstinspires.ftc.teamcode.Tests;

import android.content.Context;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
/*
This is a SD card manipulator for FTC vibe coded by team #26235 robogenisis

how to use:

reading: FileUtil.readRange(context, starting byte, ending byte);
appending: FileUtil.appendLine(context, String);
writing from an index: FileUtil.writeStringAtIndex(context, String, starting byte);
truncating writing from an index: FileUtil.overwriteFromIndex(context, String, starting byte);

note: context is a variable you declare to ensure that the log file is getting written to the correct place
      declaration: Context context = hardwareMap.appContext;

 */
    public class FileUtil {

        private static Context context;
        private static final String FILE_NAME = "log.txt";

        // ✅ Call this ONCE in your OpMode
        public static void init(Context ctx) {
            context = ctx;
        }

        private static File getOrCreateFile() throws IOException {
            if (context == null) {
                throw new IllegalStateException("FileUtil not initialized. Call init() first.");
            }

            File dir = context.getExternalFilesDir(null);
            File file = new File(dir, FILE_NAME);

            if (!file.exists()) {
                file.createNewFile();
            }

            return file;
        }

        public static void appendLine(String line) {
            try (RandomAccessFile raf = new RandomAccessFile(getOrCreateFile(), "rw")) {
                raf.seek(raf.length());
                raf.write((line + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }