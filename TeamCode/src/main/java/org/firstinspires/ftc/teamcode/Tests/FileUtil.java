package org.firstinspires.ftc.teamcode.Tests;

import android.content.Context;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;

public class FileUtil {
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

    private static final String FILE_NAME = "log.txt";

    // Get or create file using FTC-safe directory
    private static File getOrCreateFile(Context context) throws IOException {
        File dir = context.getExternalFilesDir(null);
        File file = new File(dir, FILE_NAME);

        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    // WRITE at specific index
    public static void writeStringAtIndex(Context context, String data, long index) {
        try (RandomAccessFile raf = new RandomAccessFile(getOrCreateFile(context), "rw")) {
            raf.seek(index);
            raf.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // READ a range
    public static String readRange(Context context, long startIndex, long endIndex) {
        try (RandomAccessFile raf = new RandomAccessFile(getOrCreateFile(context), "r")) {

            long fileLength = raf.length();

            if (startIndex >= fileLength) return "";
            if (endIndex > fileLength) endIndex = fileLength;
            if (endIndex < startIndex) return "";

            int length = (int)(endIndex - startIndex);
            byte[] buffer = new byte[length];

            raf.seek(startIndex);
            raf.readFully(buffer);

            return new String(buffer);

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    // APPEND line (best for logging)
    public static void appendLine(Context context, String line) {
        try (RandomAccessFile raf = new RandomAccessFile(getOrCreateFile(context), "rw")) {
            raf.seek(raf.length());
            raf.write((line + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // OVERWRITE from index (truncate after index)
    public static void overwriteFromIndex(Context context, String data, long index) {
        try (RandomAccessFile raf = new RandomAccessFile(getOrCreateFile(context), "rw")) {

            long fileLength = raf.length();
            if (index > fileLength) {
                index = fileLength;
            }

            raf.setLength(index); // delete everything after index
            raf.seek(index);
            raf.write(data.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}