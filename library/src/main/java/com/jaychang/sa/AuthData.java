package com.jaychang.sa;

import java.util.ArrayList;
import java.util.List;

class AuthData {

  private List<String> scopes;
  private AuthCallback callback;

  AuthData(List<String> scopes, AuthCallback callback) {
    this.scopes = new ArrayList<>(scopes);
    this.callback = callback;
  }

  List<String> getScopes() {
    return scopes;
  }

  AuthCallback getCallback() {
    return callback;
  }

}
