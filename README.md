# SimpleAuth
[![Download](https://api.bintray.com/packages/jaychang0917/maven/simpleauth/images/download.svg) ](https://bintray.com/jaychang0917/maven/simpleauth/_latestVersion)

A easy to use social authentication android library. (Facebook, Google, Twitter, Instagram)

## Installation
In your app level build.gradle :

```java
dependencies {
    compile 'com.jaychang:simpleauth:2.1.4'
    // if you want to use facebook auth
    compile 'com.jaychang:simpleauth-facebook:2.1.4'
    // if you want to use google auth
    compile 'com.jaychang:simpleauth-google:2.1.4'
    // if you want to use instagram auth
    compile 'com.jaychang:simpleauth-instagram:2.1.4'
    // if you want to use twitter auth
    compile 'com.jaychang:simpleauth-twitter:2.1.4'
}
```


## Basic Usage
#### 1. Configure the SimpleAuth
In your app level build.gradle, set up the configs.

```xml
android.defaultConfig.manifestPlaceholders = [
        facebookAppId        : "your facebook app id",
        googleWebClientId    : "your google web client id",
        twitterConsumerKey   : "your twitter consumer key",
        twitterConsumerSecret: "your twitter consumer secret",
        instagramClientId    : "your instagram client id",
        instagramClientSecret: "your instagram client secret",
        instagramRedirectUrl : "your instagram redirect url"
]
```

#### 2. Connect it. Done!
```java
void connectFacebook() {
  List<String> scopes = Arrays.asList("user_birthday", "user_friends");

  SimpleAuth.connectFacebook(scopes, new AuthCallback() {
    @Override
    public void onSuccess(SocialUser socialUser) {
      Log.d(TAG, "userId:" + socialUser.userId)
      Log.d(TAG, "email:" + socialUser.email)
      Log.d(TAG, "accessToken:" + socialUser.accessToken)
      Log.d(TAG, "profilePictureUrl:" + socialUser.profilePictureUrl)
      Log.d(TAG, "username:" + socialUser.username)
      Log.d(TAG, "fullName:" + socialUser.fullName);
      Log.d(TAG, "pageLink:" + socialUser.pageLink)
    }

    @Override
    public void onError(Throwable error) {
      Log.d(TAG, error.getMessage());
    }

    @Override
    public void onCancel() {
      Log.d(TAG, "Canceled");
    }
  });
}
```
### Remark
#### Google auth
Please be reminded to create an **Android** oauth client and fill in **SHA1** of your keystore and **package name**

#### Twitter auth
Please be reminded to fill in the **Callback URLs** (e.g. `twittersdk://`) of your twitter app.

## Advanced Usage
#### Disconnection
The active session will be cleared if the social app is installed in the device, otherwise app cookies will be cleared (i.e. user need to login again)
```java
void disconnectFacebook() {
  SimpleAuth.disconnectFacebook();
}
```

#### Revoke connected application
After revocation, the permissions authorization page should be shown again. Only facebook and google provide this function.
```java
void revokeFacebook() {
  SimpleAuth.revokeFacebook();
}
```

## License
```
Copyright 2017 Jay Chang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
