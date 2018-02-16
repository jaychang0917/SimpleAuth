package com.jaychang.sa.twitter

import com.jaychang.sa.*
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.TwitterCore

internal fun initTwitter() {
  val consumerKey = SimpleAuth.authConfig.twitterConsumerKey
  val consumerSecret = SimpleAuth.authConfig.twitterConsumerSecret
  if (consumerKey.isNotEmpty() && consumerSecret.isNotEmpty()) {
    val twitterConfig = TwitterConfig.Builder(Initializer.context)
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