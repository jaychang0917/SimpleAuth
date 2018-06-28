package com.jaychang.sa.facebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.jaychang.sa.AuthData;
import com.jaychang.sa.AuthDataHolder;
import com.jaychang.sa.DialogFactory;
import com.jaychang.sa.SimpleAuthActivity;
import com.jaychang.sa.SocialUser;
import com.jaychang.sa.utils.DeviceUtils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class FacebookAuthActivity extends SimpleAuthActivity
  implements FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {

  private static final String PROFILE_PIC_URL = "https://graph.facebook.com/%1$s/picture?type=large";
  private static final List<String> DEFAULT_SCOPES = Arrays.asList("email", "public_profile");

  private CallbackManager callbackManager;
  private ProgressDialog loadingDialog;

  public static void start(Context context) {
    Intent intent = new Intent(context, FacebookAuthActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    loadingDialog = DialogFactory.createLoadingDialog(this);

    callbackManager = CallbackManager.Factory.create();

    if (DeviceUtils.isFacebookInstalled(this)) {
      LoginManager.getInstance().logOut();
    }

    LoginManager.getInstance().registerCallback(callbackManager, this);

    LoginManager.getInstance().logInWithReadPermissions(this, getScopes());
  }

  private List<String> getScopes() {
    List<String> scopes = getAuthData().getScopes();
    if (scopes.size() <= 0) {
      scopes = DEFAULT_SCOPES;
    } else if (!scopes.contains(DEFAULT_SCOPES.get(0))) {
      scopes.add(DEFAULT_SCOPES.get(0));
    } else if (!scopes.contains(DEFAULT_SCOPES.get(1))) {
      scopes.add(DEFAULT_SCOPES.get(1));
    }
    return scopes;
  }

  @Override
  protected AuthData getAuthData() {
    return AuthDataHolder.getInstance().facebookAuthData;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onSuccess(LoginResult loginResult) {
    loadingDialog.show();
    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name,email,link");
    request.setParameters(parameters);
    request.executeAsync();
  }

  @Override
  public void onCancel() {
    handCancel();
  }

  @Override
  public void onError(FacebookException error) {
    handleError(error);
    if (error instanceof FacebookAuthorizationException) {
      LoginManager.getInstance().logOut();
    }
  }

  @Override
  public void onCompleted(JSONObject object, GraphResponse response) {
    SocialUser user = new SocialUser();
    user.userId =  object.optString("id", "");
    user.accessToken = AccessToken.getCurrentAccessToken().getToken();
    user.profilePictureUrl = String.format(PROFILE_PIC_URL, user.userId);
    user.email = object.optString("email", "");
    user.fullName = object.optString("name", "");
    user.pageLink = object.optString("link", "");
    loadingDialog.dismiss();
    handleSuccess(user);
  }
}
