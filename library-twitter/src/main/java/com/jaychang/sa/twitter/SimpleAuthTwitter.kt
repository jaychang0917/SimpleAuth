package com.jaychang.sa.twitter

import android.content.Context
import com.jaychang.sa.*
import com.jaychang.utils.AppUtils
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.TwitterCore

internal fun initTwitter(context: Context) {
  val consumerKey = AppUtils.getMetaDataValue(context, context.getString(R.string.com_jaychang_sa_twitterConsumerKey))
  val consumerSecret = AppUtils.getMetaDataValue(context, context.getString(R.string.com_jaychang_sa_twitterConsumerSecret))
  if (consumerKey != null && consumerKey.isNotEmpty() && consumerSecret != null && consumerSecret.isNotEmpty()) {
    val twitterConfig = TwitterConfig.Builder(context)
      .twitterAuthConfig(TwitterAuthConfig(consumerKey, consumerSecret))
      .build()
    Twitter.initialize(twitterConfig)
  }
}

fun SimpleAuth.connectTwitter(listener: AuthCallback) {
  AuthDataHolder.getInstance().twitterAuthData = AuthData(listOf(), listener)
  TwitterAuthActivity.start(Initializer.context)
}

fun SimpleAuth.disconnectTwitter() {
  AuthDataHolder.getInstance().twitterAuthData = null
  TwitterCore.getInstance().sessionManager.clearActiveSession()
  CookiesUtils.clearCookies(Initializer.context)
}