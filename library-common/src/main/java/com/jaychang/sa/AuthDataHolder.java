package com.jaychang.sa;

public class AuthDataHolder {
  private static final AuthDataHolder instance = new AuthDataHolder();

  public AuthData facebookAuthData;
  public AuthData googleAuthData;
  public AuthData twitterAuthData;
  public AuthData instagramAuthData;

  private AuthDataHolder() {
  }

  public static AuthDataHolder getInstance() {
    return instance;
  }
}
