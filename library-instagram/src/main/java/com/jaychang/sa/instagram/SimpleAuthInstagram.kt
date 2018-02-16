package com.jaychang.sa.instagram

import com.jaychang.sa.*

fun SimpleAuth.connectInstagram(scopes: List<String> = listOf(), listener: AuthCallback) {
  AuthDataHolder.getInstance().instagramAuthData = AuthData(scopes, listener)
  InstagramAuthActivity.start(Initializer.context)
}

fun SimpleAuth.disconnectInstagram() {
  AuthDataHolder.getInstance().instagramAuthData = null
  CookiesUtils.clearCookies(Initializer.context)
}
