package com.jaychang.sa.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringRes;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.util.UUID;

public class DeviceUtils {

  private DeviceUtils() {
  }

  public static String getDeviceManufacturer() {
    return Build.MANUFACTURER;
  }

  public static String getDeviceModel() {
    return Build.MODEL;
  }

  public static String getDeviceId(Context context) {
    return Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
      Settings.Secure.ANDROID_ID);
  }

  public static String getOsVersion() {
    return Build.VERSION.RELEASE;
  }

  @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
  public static String getMacAddress(Context context) {
    WifiManager wifi = (WifiManager) context.getApplicationContext()
      .getSystemService(Context.WIFI_SERVICE);
    WifiInfo info = wifi.getConnectionInfo();
    String macAddress = info.getMacAddress();
    if (macAddress == null) {
      return "";
    }
    return macAddress;
  }

  public static boolean isFacebookInstalled(Context context) {
    Intent facebook = context.getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
    return facebook != null;
  }

  public static boolean isTwitterInstalled(Context context) {
    Intent facebook = context.getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.twitter.android");
    return facebook != null;
  }

  public static boolean isInstagramInstalled(Context context) {
    Intent facebook = context.getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.instagram.android");
    return facebook != null;
  }

  public static boolean isWechatInstalled(Context context) {
    Intent facebook = context.getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
    return facebook != null;
  }

  public static void sendSMS(Context context,
                             String phoneNumber,
                             String smsContent) {
    if (TextUtils.isEmpty(phoneNumber) || !TextUtils.isDigitsOnly(phoneNumber)) {
      return;
    }

    Uri uri = Uri.parse("smsto:" + phoneNumber);
    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
    it.putExtra("sms_body", smsContent);
    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(it);
  }

  public static void makePhoneCall(Context context, String phoneNumber) {
    if (TextUtils.isEmpty(phoneNumber) || !TextUtils.isDigitsOnly(phoneNumber)) {
      return;
    }
    Uri uri = Uri.parse("tel:" + phoneNumber);
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  public static void sendEmail(Context context, String[] emails, String subject, File attachment) {
    Intent emailIntent = new Intent(Intent.ACTION_SEND);
    emailIntent.setType("vnd.android.cursor.dir/email");
    emailIntent.putExtra(Intent.EXTRA_EMAIL, emails);
    if (attachment != null) {
      emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(attachment));
    }
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
    context.startActivity(Intent.createChooser(emailIntent, ""));
  }

  public static void sendEmail(Context context, String[] emails, String subject) {
    sendEmail(context, emails, subject, null);
  }

  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  public static boolean isNetworkConnected(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnected();
  }

  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  public static boolean isUsingWifi(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isAvailable() && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
  }

  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  public static boolean isUsingMobileData(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isAvailable() && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
  }

  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  public static boolean isUsing4G(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isAvailable() && activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
  }

  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  public static void showNetworkDialogIfNotEnable(final Context context,
                                                  @StringRes int msg,
                                                  @StringRes int posText,
                                                  @StringRes int negText) {
    if (!isNetworkConnected(context)) {
      new AlertDialog.Builder(context)
        .setMessage(msg)
        .setPositiveButton(posText, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Intent intent = new Intent(Build.VERSION.SDK_INT > 10 ?
              Settings.ACTION_SETTINGS : Settings.ACTION_WIRELESS_SETTINGS
            );
            context.startActivity(intent);
          }
        })
        .setNegativeButton(negText, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        }).show();
    }
  }

  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  public static boolean isLocationEnabled(Context context) {
    LocationManager lm = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    boolean gpsEnabled =
      lm.getAllProviders().contains(LocationManager.GPS_PROVIDER)
        && lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    boolean networkEnabled =
      lm.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)
        && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    return gpsEnabled | networkEnabled;
  }

  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  public static void showLocationDialogIfNotEnable(final Context context,
                                                   @StringRes int msg,
                                                   @StringRes int posText,
                                                   @StringRes int negText) {
    if (!isLocationEnabled(context)) {
      new AlertDialog.Builder(context)
        .setMessage(msg)
        .setPositiveButton(posText, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
          }
        })
        .setNegativeButton(negText, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        }).show();
    }
  }

  @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN })
  public static void setBluetoothEnabled(boolean enabled) {
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    if (enabled) {
      adapter.enable();
    } else {
      adapter.disable();
    }
  }

  @RequiresPermission(Manifest.permission.BLUETOOTH)
  public static boolean isBluetoothEnabled() {
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    return adapter.isEnabled();
  }

  public static UUID getBluetoothDeviceID(byte[] recordData) {
    return UUID.nameUUIDFromBytes(recordData);
  }

  @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
  public static boolean isPhone(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
  }

  @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
  public static String getIMEI(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    return tm != null ? tm.getDeviceId() : "";
  }

  @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
  public static boolean hasSimCard(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
  }

  @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
  public static String getSimCardOperatorName(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    return tm != null ? tm.getSimOperatorName() : "";
  }

  @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
  public static String getSimCardOperatorCode(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    return tm != null ? tm.getSimOperator() : "";
  }

  @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
  public static String getPhoneStatus(Context context) {
    TelephonyManager tm = (TelephonyManager) context
      .getSystemService(Context.TELEPHONY_SERVICE);
    String str = "";
    str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
    str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
    str += "Line1Number = " + tm.getLine1Number() + "\n";
    str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
    str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
    str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
    str += "NetworkType = " + tm.getNetworkType() + "\n";
    str += "PhoneType = " + tm.getPhoneType() + "\n";
    str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
    str += "SimOperator = " + tm.getSimOperator() + "\n";
    str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
    str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
    str += "SimState = " + tm.getSimState() + "\n";
    str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
    str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
    return str;
  }

  public static void setLandscape(Activity activity) {
    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
  }

  public static void setPortrait(Activity activity) {
    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  }

  public static boolean isLandscape(Context context) {
    return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
  }

  public static boolean isPortrait(Context context) {
    return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
  }

  public static boolean isSDCardEnable() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }
}