package com.jaychang.demo.sa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jaychang.sa.SocialUser
import com.jaychang.sa.facebook.SimpleAuth
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
  companion object {
    private const val EXTRA_USER = "EXTRA_USER"
    private const val EXTRA_TYPE = "EXTRA_TYPE"

    fun start(context: Context, type: String, socialUser: SocialUser) {
      val intent = Intent(context, ProfileActivity::class.java)
      intent.putExtra(EXTRA_USER, socialUser)
      intent.putExtra(EXTRA_TYPE, type)
      context.startActivity(intent)
    }
  }

  private var type: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_profile)

    val socialUser = intent.getParcelableExtra<SocialUser>(EXTRA_USER)

    userView.text = socialUser.toString()

    type = intent.getStringExtra(EXTRA_TYPE)

    handleVisibility()
  }

  fun disconnect(view: View) {
    when (type) {
      MainActivity.HUAWEI -> com.jaychang.sa.huawei.SimpleAuth.disconnectHuawei()
      MainActivity.FACEBOOK -> SimpleAuth.disconnectFacebook()
      MainActivity.GOOGLE -> com.jaychang.sa.google.SimpleAuth.disconnectGoogle()
      MainActivity.TWITTER -> com.jaychang.sa.twitter.SimpleAuth.disconnectTwitter()
      MainActivity.INSTAGRAM -> com.jaychang.sa.instagram.SimpleAuth.disconnectInstagram()
    }
    finish()
  }

  fun revoke(view: View) {
    when (type) {
      MainActivity.HUAWEI -> com.jaychang.sa.huawei.SimpleAuth.revokeHuawei()
      MainActivity.FACEBOOK -> SimpleAuth.revokeFacebook()
      MainActivity.GOOGLE -> com.jaychang.sa.google.SimpleAuth.revokeGoogle()
      MainActivity.TWITTER -> {
        // no-op
      }
      MainActivity.INSTAGRAM -> {
        // no-op
      }
    }
    finish()
  }

  private fun handleVisibility() {
    if (MainActivity.TWITTER == type || MainActivity.INSTAGRAM == type) {
      revokeButton.visibility = View.GONE
    }
  }
}
