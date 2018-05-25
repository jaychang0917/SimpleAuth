package com.jaychang.sa.facebook

import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.jaychang.sa.*

object SimpleAuth {
  @JvmStatic
  fun connectFacebook(scopes: List<String> = listOf(), listener: AuthCallback) {
    AuthDataHolder.getInstance().facebookAuthData = AuthData(scopes, listener)
    FacebookAuthActivity.start(Initializer.context)
  }

  @JvmStatic
  fun disconnectFacebook() {
    AuthDataHolder.getInstance().facebookAuthData = null
    LoginManager.getInstance().logOut()
  }

  @JvmStatic
  fun revokeFacebook(callback: RevokeCallback? = null) {
    GraphRequest(AccessToken.getCurrentAccessToken(),
      "/me/permissions/", null, HttpMethod.DELETE
    ) { _ ->
      disconnectFacebook()
      callback?.onRevoked()
    }.executeAsync()
  }
}

