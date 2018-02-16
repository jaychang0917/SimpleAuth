package com.jaychang.sa;

public interface AuthCallback {
  void onSuccess(SocialUser socialUser);

  void onError(Throwable error);

  void onCancel();
}
