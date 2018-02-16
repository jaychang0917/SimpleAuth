package com.jaychang.sa.facebook

import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.jaychang.sa.*

internal fun initFacebook() {
  val facebookAppId = SimpleAuth.authConfig.facebookAppId
  if (facebookAppId.isNotEmpty()) {
    FacebookSdk.setApplicationId(facebookAppId)
    FacebookSdk.sdkInitialize(Initializer.context)
    FacebookSdk.setWebDialogTheme(android.R.style.Theme_Holo_Light_NoActionBar)
  }
}

fun SimpleAuth.connectFacebook(scopes: List<String> = listOf(), listener: AuthCallback) {
  AuthDataHolder.getInstance().facebookAuthData = AuthData(scopes, listener)
  FacebookAuthActivity.start(Initializer.context)
}

fun SimpleAuth.disconnectFacebook() {
  AuthDataHolder.getInstance().facebookAuthData = null
  LoginManager.getInstance().logOut()
}

fun SimpleAuth.revokeFacebook(callback: RevokeCallback? = null) {
  GraphRequest(AccessToken.getCurrentAccessToken(),
    "/me/permissions/", null, HttpMethod.DELETE
  ) { _ ->
    disconnectFacebook()
    callback?.onRevoked()
  }.executeAsync()
}
