package com.jaychang.sa.twitter

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.jaychang.sa.utils.AppUtils
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

class TwitterInitProvider : ContentProvider() {

  private fun initTwitter(context: Context) {
    val consumerKey = AppUtils.getMetaDataValue(context, context.getString(R.string.com_jaychang_sa_twitterConsumerKey))
    val consumerSecret = AppUtils.getMetaDataValue(context, context.getString(R.string.com_jaychang_sa_twitterConsumerSecret))
    if (consumerKey != null && consumerKey.isNotEmpty() && consumerSecret != null && consumerSecret.isNotEmpty()) {
      val twitterConfig = TwitterConfig.Builder(context)
        .twitterAuthConfig(TwitterAuthConfig(consumerKey, consumerSecret))
        .build()
      Twitter.initialize(twitterConfig)
    }
  }

  override fun onCreate(): Boolean {
    initTwitter(context.applicationContext)
    return false
  }

  override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
    return null
  }

  override fun getType(uri: Uri): String? {
    return null
  }

  override fun insert(uri: Uri, values: ContentValues?): Uri? {
    return null
  }

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
    return 0
  }

  override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
    return 0
  }

}
