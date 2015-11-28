package com.gdgistanbul.devfest.feedback.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gdgistanbul.devfest.feedback.models.Session;
import com.google.gson.Gson;

import timber.log.Timber;

public class PrefUtils {

  public static final String CURRENT_SESSION_KEY = "current_session";
  public static final String EXPORT_FILE_PATH_KEY = "export_file_path";

  public static void setCurrentSession(Context context, Session session) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString(CURRENT_SESSION_KEY, new Gson().toJson(session)).apply();
  }

  public static Session getCurrentSession(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    String json = prefs.getString(CURRENT_SESSION_KEY, null);
    try {
      return new Gson().fromJson(json, Session.class);
    } catch (Exception e) {
      Timber.e(e, "getCurrentSession");
    }
    return null;
  }

  public static void setExportFilePath(Context context, String path) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString(EXPORT_FILE_PATH_KEY, path).apply();
  }

  public static String getExportFilePath(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString(EXPORT_FILE_PATH_KEY, null);
  }

}
