package com.jaychang.sa.google

import com.jaychang.sa.AuthCallback
import com.jaychang.sa.AuthData
import com.jaychang.sa.AuthDataHolder
import com.jaychang.sa.Initializer

object SimpleAuth {
  @JvmStatic
  fun connectGoogle(scopes: List<String> = listOf(), listener: AuthCallback) {
    AuthDataHolder.getInstance().googleAuthData = AuthData(scopes, listener)
    GoogleAuthActivity.start(Initializer.context)
  }

  @JvmStatic
  fun disconnectGoogle() {
    AuthDataHolder.getInstance().googleAuthData = null
    GoogleAuthActivity.setGoogleDisconnectRequested(Initializer.context,true)
  }

  @JvmStatic
  fun revokeGoogle() {
    GoogleAuthActivity.setGoogleRevokeRequested(Initializer.context,true)
  }
}
