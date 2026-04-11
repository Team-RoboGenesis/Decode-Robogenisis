package org.firstinspires.ftc.teamcode.Tests;

import android.content.Context;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;

/*
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

public class FileUtil {

    private static Context context;
    private static final String FILE_NAME = "log.txt";

    // ✅ Initialize ONCE in OpMode
    public static void init(Context ctx) {
        context = ctx;
    }

    // ✅ Get or create file
    private static File getOrCreateFile() throws IOException {
        if (context == null) {
            throw new IllegalStateException("FileUtil not initialized. Call init() first.");
        }

        File dir = context.getExternalFilesDir(null);

        if (dir == null) {
            throw new IOException("External files directory is null");
        }

        File file = new File(dir, FILE_NAME);

        if (!file.exists()) {
            boolean created = file.createNewFile();
            System.out.println("FILE CREATED: " + created);
        }
        return file;
    }

    // ✅ Append line (BEST for logging)
    public static void appendLine(String line) {
        try (RandomAccessFile raf = new RandomAccessFile(getOrCreateFile(), "rw")) {
            raf.seek(raf.length());
            raf.write((line + "\n").getBytes());
        } catch (IOException e) {
            System.out.println("WRITE ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Read range
    public static String readRange(long startIndex, long endIndex) {
        try (RandomAccessFile raf = new RandomAccessFile(getOrCreateFile(), "r")) {

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
            System.out.println("READ ERROR: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    // ✅ Write at index
    public static void writeStringAtIndex(String data, long index) {
        try (RandomAccessFile raf = new RandomAccessFile(getOrCreateFile(), "rw")) {
            raf.seek(index);
            raf.write(data.getBytes());
        } catch (IOException e) {
            System.out.println("WRITE INDEX ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 🔥 Overwrite from index
    public static void overwriteFromIndex(String data, long index) {
        try (RandomAccessFile raf = new RandomAccessFile(getOrCreateFile(), "rw")) {

            long fileLength = raf.length();
            if (index > fileLength) {
                index = fileLength;
            }

            raf.setLength(index);
            raf.seek(index);
            raf.write(data.getBytes());

        } catch (IOException e) {
            System.out.println("OVERWRITE ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}