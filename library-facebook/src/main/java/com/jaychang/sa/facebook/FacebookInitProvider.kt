package com.jaychang.sa.facebook

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.facebook.FacebookSdk
import com.jaychang.sa.utils.AppUtils

class FacebookInitProvider : ContentProvider() {

  private fun initFacebook(context: Context) {
    val facebookAppId = AppUtils.getMetaDataValue(context, context.getString(R.string.com_jaychang_sa_facebookId))
    if (facebookAppId != null && facebookAppId.isNotEmpty()) {
      FacebookSdk.setApplicationId(facebookAppId)
      FacebookSdk.sdkInitialize(context)
      FacebookSdk.setWebDialogTheme(android.R.style.Theme_Holo_Light_NoActionBar)
    }
  }

  override fun onCreate(): Boolean {
    initFacebook(context.applicationContext)
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
