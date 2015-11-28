package com.gdgistanbul.devfest.feedback.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import timber.log.Timber;

public class FileUtil {

  public static final String FOLDER_NAME = "/devfest_feedback/";
  public static final String FILE_NAME = "feedbacks.csv";

  public static boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
      return true;
    }
    return false;
  }

  public static File getExportFile(Context context) {
    File file = null;
    String path = PrefUtils.getExportFilePath(context);
    if (TextUtils.isEmpty(path)) {
      file = getNewExportFile(context);
    } else {
      file = new File(path);
      if (!file.exists()) {
        file = getNewExportFile(context);
      }
    }
    return file;
  }

  public static File getNewExportFile(Context context) {
    File file = null;
    File folder = null;
    if (isExternalStorageWritable()) {
      folder = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
    } else {
      folder = new File(context.getFilesDir(), FOLDER_NAME);
    }
    if (!folder.exists()) {
      folder.mkdirs();
    }
    file = new File(folder, FILE_NAME);
    if (!file.exists()) {
      try {
        file.createNewFile();
        appendLine(file, "Session Id,Session Name,Feedback,Time");
      } catch (Exception e) {
        Timber.e(e, "error while creating file");
      }
    }
    return file;
  }

  public static void appendLine(Context context, String line) {
    appendLine(getExportFile(context), line);
  }

  public static void appendLine(File file, String line) {
    FileWriter writer = null;
    try {
      writer = new FileWriter(file, true);
      writer.append(line);
      writer.append("\n");
    } catch (Exception e) {
      Timber.e(e, "error while append file");
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          Timber.e(e, "error while close file writer");
        }
      }
    }
  }

}
