package com.gdgistanbul.devfest.feedback.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gdgistanbul.devfest.feedback.models.Session;
import com.google.gson.Gson;

import timber.log.Timber;

public class PrefUtils {

  public static final String CURRENT_SESSION_KEY = "current_session";

  public static void setCurrentSession(Context context, Session session) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(CURRENT_SESSION_KEY, new Gson().toJson(session));
    editor.commit();
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

}
