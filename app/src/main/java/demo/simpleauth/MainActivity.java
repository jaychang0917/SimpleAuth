package demo.simpleauth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jaychang.sa.AuthCallback;
import com.jaychang.sa.SimpleAuth;
import com.jaychang.sa.SocialUser;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  public static final String FACEBOOK = "FACEBOOK";
  public static final String GOOGLE = "GOOGLE";
  public static final String TWITTER = "TWITTER";
  public static final String INSTAGRAM = "INSTAGRAM";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.connectFbButton)
  void connectFacebook() {
    List<String> scopes = Arrays.asList("user_birthday", "user_friends");

    SimpleAuth.getInstance().connectFacebook(scopes, new AuthCallback() {
      @Override
      public void onSuccess(SocialUser socialUser) {
        ProfileActivity.start(MainActivity.this, FACEBOOK, socialUser);
      }

      @Override
      public void onError(Throwable error) {
        toast(error.getMessage());
      }

      @Override
      public void onCancel() {
        toast("Canceled");
      }
    });
  }

  @OnClick(R.id.connectGoogleButton)
  void connectGoogle() {
    List<String> scopes = Arrays.asList(
      "https://www.googleapis.com/auth/youtube",
      "https://www.googleapis.com/auth/youtube.upload"
    );

    SimpleAuth.getInstance().connectGoogle(scopes, new AuthCallback() {
      @Override
      public void onSuccess(SocialUser socialUser) {
        ProfileActivity.start(MainActivity.this, GOOGLE, socialUser);
      }

      @Override
      public void onError(Throwable error) {
        toast(error.getMessage());
      }

      @Override
      public void onCancel() {
        toast("Canceled");
      }
    });
  }

  @OnClick(R.id.connectTwitterButton)
  void connectTwitter() {
    SimpleAuth.getInstance().connectTwitter(new AuthCallback() {
      @Override
      public void onSuccess(SocialUser socialUser) {
        ProfileActivity.start(MainActivity.this, TWITTER, socialUser);
      }

      @Override
      public void onError(Throwable error) {
        toast(error.getMessage());
      }

      @Override
      public void onCancel() {
        toast("Canceled");
      }
    });
  }

  @OnClick(R.id.connectIgButton)
  void connectInstagram() {
    List<String> scopes = Arrays.asList("follower_list", "likes");

    SimpleAuth.getInstance().connectInstagram(scopes, new AuthCallback() {
      @Override
      public void onSuccess(SocialUser socialUser) {
        ProfileActivity.start(MainActivity.this, INSTAGRAM, socialUser);
      }

      @Override
      public void onError(Throwable error) {
        toast(error.getMessage());
      }

      @Override
      public void onCancel() {
        toast("Canceled");
      }
    });
  }

  private void toast(String msg) {
    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
  }

}
