package com.jaychang.sa.instagram

import com.jaychang.sa.*

object SimpleAuth {
  @JvmStatic
  fun connectInstagram(scopes: List<String> = listOf(), listener: AuthCallback) {
    AuthDataHolder.getInstance().instagramAuthData = AuthData(scopes, listener)
    InstagramAuthActivity.start(Initializer.context)
  }

  @JvmStatic
  fun disconnectInstagram() {
    AuthDataHolder.getInstance().instagramAuthData = null
    CookiesUtils.clearCookies(Initializer.context)
  }
}
