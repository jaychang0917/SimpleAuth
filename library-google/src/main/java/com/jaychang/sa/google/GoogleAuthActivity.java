package com.jaychang.sa.google;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.jaychang.sa.AuthData;
import com.jaychang.sa.AuthDataHolder;
import com.jaychang.sa.DialogFactory;
import com.jaychang.sa.SimpleAuthActivity;
import com.jaychang.sa.SocialUser;
import com.jaychang.sa.utils.AppUtils;
import com.jaychang.sa.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class GoogleAuthActivity extends SimpleAuthActivity
  implements GoogleApiClient.OnConnectionFailedListener,
  GoogleApiClient.ConnectionCallbacks {

  private interface AccessTokenListener {
    void onTokenReady(String accessToken);
  }

  private static final String KEY_IS_GOOGLE_DISCONNECT_REQUESTED = SimpleAuth.class.getName() + "KEY_IS_GOOGLE_DISCONNECT_REQUESTED";
  private static final String KEY_IS_GOOGLE_REVOKE_REQUESTED = SimpleAuth.class.getName() + "KEY_IS_GOOGLE_REVOKE_REQUESTED";
  private static final int RC_SIGN_IN = 1000;

  private GoogleApiClient googleApiClient;
  private boolean retrySignIn;

  public static void start(Context context) {
    Intent intent = new Intent(context, GoogleAuthActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String clientId = AppUtils.getMetaDataValue(this, getString(R.string.com_jaychang_sa_googleWebClientId));

    GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestId()
      .requestProfile()
      .requestEmail()
      .requestIdToken(clientId);

    setupScopes(gsoBuilder);

    googleApiClient = new GoogleApiClient.Builder(this)
      .enableAutoManage(this, this)
      .addConnectionCallbacks(this)
      .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build())
      .build();
  }

  @Override
  protected AuthData getAuthData() {
    return AuthDataHolder.getInstance().googleAuthData;
  }

  private void startSignInFlows() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  private void setupScopes(GoogleSignInOptions.Builder builder) {
    List<Scope> scopes = getScopes();
    if (scopes.size() == 1) {
      builder.requestScopes(scopes.get(0));
    } else if (scopes.size() > 1) {
      List<Scope> restScopes = scopes.subList(1, scopes.size());
      Scope[] restScopesArray = new Scope[restScopes.size()];
      restScopesArray = scopes.toArray(restScopesArray);
      builder.requestScopes(scopes.get(0), restScopesArray);
    }
  }

  private List<Scope> getScopes() {
    List<Scope> scopes = new ArrayList<>();
    for (String str : getAuthData().getScopes()) {
      scopes.add(new Scope(str));
    }

    return scopes;
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Throwable error = new Throwable(connectionResult.getErrorMessage());
    handleError(error);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode != RC_SIGN_IN || resultCode != RESULT_OK) {
      handCancel();
      return;
    }

    GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

    if ((!isGoogleDisconnectRequested(this) && !isGoogleRevokeRequested(this)) || retrySignIn) {
      retrySignIn = false;
      handleSignInResult(signInResult);
    }
  }

  private void handleSignInResult(GoogleSignInResult result) {
    if (result == null) {
      handCancel();
      return;
    }

    if (result.isSuccess() && result.getSignInAccount() != null) {
      final GoogleSignInAccount acct = result.getSignInAccount();
      final SocialUser user = new SocialUser();
      user.userId = acct.getId();
      user.accessToken = acct.getIdToken();
      user.profilePictureUrl = acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : "";
      user.email = acct.getEmail();
      user.fullName = acct.getDisplayName();

      getAccessToken(acct, new AccessTokenListener() {
        @Override
        public void onTokenReady(String accessToken) {
          user.accessToken = accessToken;
          handleSuccess(user);
        }
      });
    } else {
      String errorMsg = result.getStatus().getStatusMessage();
      if (errorMsg == null) {
        handCancel();
      } else {
        Throwable error = new Throwable(result.getStatus().getStatusMessage());
        handleError(error);
      }
    }
  }

  private void getAccessToken(final GoogleSignInAccount account, final AccessTokenListener listener) {
    final ProgressDialog loadingDialog = DialogFactory.createLoadingDialog(this);
    loadingDialog.show();

    AsyncTask.execute(new Runnable() {
      @Override
      public void run() {
        try {
          if (account.getAccount() == null) {
            loadingDialog.dismiss();
            handleError(new RuntimeException("Account is null"));
          } else {
            loadingDialog.dismiss();
            setGoogleDisconnectRequested(GoogleAuthActivity.this, false);
            setGoogleRevokeRequested(GoogleAuthActivity.this, false);
            String token = GoogleAuthUtil.getToken(getApplicationContext(), account.getAccount().name, getAccessTokenScope());
            listener.onTokenReady(token);
          }
        } catch (Exception e) {
          e.printStackTrace();
          loadingDialog.dismiss();
          handleError(e);
        }
      }
    });
  }

  private String getAccessTokenScope() {
    String scopes = "oauth2:id profile email";
    if (getAuthData().getScopes().size() > 0) {
      scopes = "oauth2:" + TextUtils.join(" ", getAuthData().getScopes());
    }

    return scopes;
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    Runnable signIn = new Runnable() {
      @Override
      public void run() {
        retrySignIn = true;
        startSignInFlows();
      }
    };

    if (isGoogleDisconnectRequested(this)) {
      handleDisconnectRequest(signIn);
    } else if (isGoogleRevokeRequested(this)) {
      handleRevokeRequest(signIn);
    } else {
      startSignInFlows();
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
    handleError(new Throwable("connection suspended."));
  }

  private void handleDisconnectRequest(final Runnable onSignOut) {
    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
      @Override
      public void onResult(@NonNull Status status) {
        onSignOut.run();
        setGoogleDisconnectRequested(GoogleAuthActivity.this, false);
      }
    });
  }

  private void handleRevokeRequest(final Runnable onRevoke) {
    Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
      @Override
      public void onResult(@NonNull Status status) {
        onRevoke.run();
        setGoogleRevokeRequested(GoogleAuthActivity.this, false);
      }
    });
  }

  static boolean isGoogleDisconnectRequested(Context context) {
    return PreferenceUtils.getBoolean(context, KEY_IS_GOOGLE_DISCONNECT_REQUESTED);
  }

  public static void setGoogleDisconnectRequested(Context context, boolean isRequested) {
    PreferenceUtils.saveBoolean(context, KEY_IS_GOOGLE_DISCONNECT_REQUESTED, isRequested);
  }

  static boolean isGoogleRevokeRequested(Context context) {
    return PreferenceUtils.getBoolean(context, KEY_IS_GOOGLE_REVOKE_REQUESTED);
  }

  public static void setGoogleRevokeRequested(Context context, boolean isRequested) {
    PreferenceUtils.saveBoolean(context, KEY_IS_GOOGLE_REVOKE_REQUESTED, isRequested);
  }
}
