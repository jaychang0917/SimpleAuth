package com.jaychang.sa

import android.annotation.SuppressLint

@SuppressLint("StaticFieldLeak")
object SimpleAuth {
  lateinit var authConfig: AuthConfig

  fun init(config: AuthConfig) {
    authConfig = config
    Initializer.facebookInitHook?.invoke()
    Initializer.twitterInitHook?.invoke()
  }
}
