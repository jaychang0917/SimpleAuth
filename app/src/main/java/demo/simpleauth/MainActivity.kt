package demo.simpleauth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.jaychang.sa.AuthCallback
import com.jaychang.sa.SimpleAuth
import com.jaychang.sa.SocialUser
import com.jaychang.sa.facebook.connectFacebook
import com.jaychang.sa.google.connectGoogle
import com.jaychang.sa.instagram.connectInstagram
import com.jaychang.sa.twitter.connectTwitter
import java.util.*

class MainActivity : AppCompatActivity() {

  companion object {
    const val FACEBOOK = "FACEBOOK"
    const val GOOGLE = "GOOGLE"
    const val TWITTER = "TWITTER"
    const val INSTAGRAM = "INSTAGRAM"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun connectFacebook(view: View) {
    val scopes = Arrays.asList("user_birthday", "user_friends")

    SimpleAuth.connectFacebook(scopes, object : AuthCallback {
      override fun onSuccess(socialUser: SocialUser) {
        ProfileActivity.start(this@MainActivity, FACEBOOK, socialUser)
      }

      override fun onError(error: Throwable) {
        toast(error.message ?: "")
      }

      override fun onCancel() {
        toast("Canceled")
      }
    })
  }

  fun connectGoogle(view: View) {
    val scopes = Arrays.asList(
      "https://www.googleapis.com/auth/youtube",
      "https://www.googleapis.com/auth/youtube.upload"
    )

    SimpleAuth.connectGoogle(scopes, object : AuthCallback {
      override fun onSuccess(socialUser: SocialUser) {
        ProfileActivity.start(this@MainActivity, GOOGLE, socialUser)
      }

      override fun onError(error: Throwable) {
        toast(error.message ?: "")
      }

      override fun onCancel() {
        toast("Canceled")
      }
    })
  }

  fun connectTwitter(view: View) {
    SimpleAuth.connectTwitter(object : AuthCallback {
      override fun onSuccess(socialUser: SocialUser) {
        ProfileActivity.start(this@MainActivity, TWITTER, socialUser)
      }

      override fun onError(error: Throwable) {
        toast(error.message ?: "")
      }

      override fun onCancel() {
        toast("Canceled")
      }
    })
  }

  fun connectInstagram(view: View) {
    val scopes = Arrays.asList("follower_list", "likes")

    SimpleAuth.connectInstagram(scopes, object : AuthCallback {
      override fun onSuccess(socialUser: SocialUser) {
        ProfileActivity.start(this@MainActivity, INSTAGRAM, socialUser)
      }

      override fun onError(error: Throwable) {
        toast(error.message ?: "")
      }

      override fun onCancel() {
        toast("Canceled")
      }
    })
  }

  private fun toast(msg: String) {
    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
  }
}
