package com.jaychang.sa.google

import com.jaychang.sa.*

fun SimpleAuth.connectGoogle(scopes: List<String> = listOf(), listener: AuthCallback) {
  AuthDataHolder.getInstance().googleAuthData = AuthData(scopes, listener)
  GoogleAuthActivity.start(Initializer.context)
}

fun SimpleAuth.disconnectGoogle() {
  AuthDataHolder.getInstance().googleAuthData = null
  GoogleAuthActivity.setGoogleDisconnectRequested(Initializer.context,true)
}

fun SimpleAuth.revokeGoogle() {
  GoogleAuthActivity.setGoogleRevokeRequested(Initializer.context,true)
}
