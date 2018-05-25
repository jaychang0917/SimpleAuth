package com.jaychang.sa.twitter

import com.jaychang.sa.*
import com.twitter.sdk.android.core.TwitterCore

object SimpleAuth {
  @JvmStatic
  fun connectTwitter(listener: AuthCallback) {
    AuthDataHolder.getInstance().twitterAuthData = AuthData(listOf(), listener)
    TwitterAuthActivity.start(Initializer.context)
  }

  @JvmStatic
  fun disconnectTwitter() {
    AuthDataHolder.getInstance().twitterAuthData = null
    TwitterCore.getInstance().sessionManager.clearActiveSession()
    CookiesUtils.clearCookies(Initializer.context)
  }
}
