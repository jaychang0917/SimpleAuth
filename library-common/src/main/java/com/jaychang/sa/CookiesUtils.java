package com.jaychang.sa;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public final class CookiesUtils {
  private CookiesUtils() {
  }

  public static void clearCookies(Context context) {
    if (Build.VERSION.SDK_INT >= 21) {
      CookieManager.getInstance().removeAllCookies(null);
    } else {
      CookieSyncManager.createInstance(context.getApplicationContext());
      CookieManager.getInstance().removeAllCookie();
    }
  }
}
