package com.jaychang.sa.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import java.util.Set;

public final class PreferenceUtils {

  private PreferenceUtils() {
  }

  public static void saveString(Context context, String key, String value) {
    PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putString(key, value)
      .apply();
  }

  public static String getString(Context context, String key) {
    return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getString(key, "");
  }

  public static void saveBoolean(Context context, String key, boolean value) {
    PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putBoolean(key, value)
      .apply();
  }

  public static boolean getBoolean(Context context, String key) {
    return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getBoolean(key, false);
  }

  public static boolean getBoolean(Context context, String key, boolean defaultVal) {
    return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getBoolean(key, defaultVal);
  }

  public static void saveInt(Context context, String key, int value) {
    PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putInt(key, value)
      .apply();
  }

  public static int getInt(Context context, String key) {
    return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getInt(key, -1);
  }

  public static void saveLong(Context context, String key, long value) {
    PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putLong(key, value)
      .apply();
  }

  public static long getLong(Context context, String key) {
    return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getLong(key, -1L);
  }

  public static void saveFloat(Context context, String key, float value) {
    PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putFloat(key, value)
      .apply();
  }

  public static float getFloat(Context context, String key) {
    return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getFloat(key, -1f);
  }

  public static void saveStringSet(Context context, String key, Set<String> value) {
    PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putStringSet(key, value)
      .apply();
  }

  public static Set<String> getStringSet(Context context, String key) {
    return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getStringSet(key, null);
  }

  public static void remove(Context context, String key) {
    PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .remove(key)
      .apply();
  }

  public static void contains(Context context, String key) {
    PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .contains(key);
  }

  public static void clear(Context context) {
    PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit().clear().apply();
  }
}