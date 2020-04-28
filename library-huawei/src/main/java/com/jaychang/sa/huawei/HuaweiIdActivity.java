/*
 *    Copyright 2020 Huawei Technologies Co., Ltd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.jaychang.sa.huawei;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.jaychang.sa.AuthData;
import com.jaychang.sa.AuthDataHolder;
import com.jaychang.sa.SimpleAuthActivity;
import com.jaychang.sa.SocialUser;
import com.jaychang.sa.huawei.common.ICallBack;

public class HuaweiIdActivity extends SimpleAuthActivity {
    // Log tag
    private static final String TAG = "HuaweiIdActivity";

    private static HuaweiIdAuthService mAuthManager;

    private HuaweiIdAuthParams mAuthParam;

    public static void start(Context context) {
        Intent intent = new Intent(context, HuaweiIdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize the HuaweiIdSignInClient object by calling the getClient method of HuaweiIdSignIn
        mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();
        mAuthManager = HuaweiIdAuthManager.getService(HuaweiIdActivity.this, mAuthParam);

        signIn();
    }

    @Override
    protected AuthData getAuthData() {
        return AuthDataHolder.getInstance().huaweiAuthData;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void signIn() {
        startActivityForResult(mAuthManager.getSignInIntent(), Constant.REQUEST_SIGN_IN_LOGIN);
    }

    public static void signOut() {
        Task<Void> signOutTask = mAuthManager.signOut();
        signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "signOut Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "signOut fail");
            }
        });
    }

    public static void revoke() {
        Task<Void> revokeTask = mAuthManager.cancelAuthorization();
        revokeTask
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "revoke Success");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Log.i(TAG, "revoke fail");
                            }
                        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != Constant.REQUEST_SIGN_IN_LOGIN) {
            handCancel();
            return;
        }
        // login success
        // get user message by getSignedInAccountFromIntent
        Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
        if (authHuaweiIdTask.isSuccessful()) {
            AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
            Log.i(TAG, huaweiAccount.getDisplayName() + " signIn success ");
            Log.i(TAG, "AccessToken: " + huaweiAccount.getAccessToken());
            validateIdToken(huaweiAccount.getIdToken());
            handleSignInResult(huaweiAccount);
        } else {
            Log.i(TAG, "signIn failed: " + authHuaweiIdTask.getException().getMessage());
        }
    }

    private void validateIdToken(String idToken) {
        if (TextUtils.isEmpty(idToken)) {
            Log.i(TAG, "ID Token is empty");
        } else {
            IDTokenParser idTokenParser = new IDTokenParser();
            try {
                idTokenParser.verify(idToken, new ICallBack() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onSuccess(String idTokenJsonStr) {
                        if (!TextUtils.isEmpty(idTokenJsonStr)) {
                            Log.i(TAG, "id Token Validate Success, verify signature: " + idTokenJsonStr);
                        } else {
                            Log.i(TAG, "Id token validate failed.");
                        }
                    }

                    @Override
                    public void onFailed() {
                        Log.i(TAG, "Id token validate failed.");
                    }
                });
            } catch (Exception e) {
                Log.i(TAG, "id Token validate failed." + e.getClass().getSimpleName());
            } catch (Error e) {
                Log.i(TAG, "id Token validate failed." + e.getClass().getSimpleName());
                if (Build.VERSION.SDK_INT < 23) {
                    Log.i(TAG, "android SDK Version is not support. Current version is: " + Build.VERSION.SDK_INT);
                }
            }
        }
    }

    private void handleSignInResult(AuthHuaweiId account) {
        final SocialUser user = new SocialUser();
        user.userId = account.getUid();
        user.profilePictureUrl = account.getAvatarUriString() != null ? account.getAvatarUriString() : "";
        user.email = account.getEmail();
        user.fullName = account.getDisplayName();

        handleSuccess(user);
    }
}
