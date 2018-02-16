package demo.simpleauth

import android.app.Application
import com.jaychang.sa.AuthConfig
import com.jaychang.sa.SimpleAuth

class App : Application() {
  override fun onCreate() {
    super.onCreate()

    val authConfig = AuthConfig(
      facebookAppId = "189953571452011",
      googleWebClientId = "775503623453-sqk59kuvr2f8qkb9fkcnhef5uvn9riu4.apps.googleusercontent.com",
      twitterConsumerKey = "r59jTevJfWg6YDGldRGiks8Rl",
      twitterConsumerSecret = "MQAQtngew0imjGduM3mEbWE1fdUbGOekYEcvm0ybCoJy5FUrju",
      instagramClientId = "76bedc2d27dd4e52841e576d4ce8cc79",
      instagramClientSecret = "28d91eb23ab440beb55b3a3f112f4bb2",
      instagramRedirectUrl = "https://localhost/oauth2redirect"
    )
    SimpleAuth.init(authConfig)
  }
}