/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
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

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jaychang.sa.huawei.common.ICallBack;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Function Description
 * This is a demo for verify Id Token issued by HuaWei OAuth Server
 * The demo just show how to verify Id Token in your local server
 * The open source software depended by this demo may have vulnerabilities,
 * please refer to the open source software release website and update to
 * the latest version or replace it with other open source software.
 * Local validation is much more efficiently than by access the tokeninfo endpoint
 * You'd better learn more about the JWT and JWK for understanding this demo
 * See more about JWT in https://jwt.io/
 * See more about JWK in http://self-issued.info/docs/draft-ietf-jose-json-web-key.html
 */

public class IDTokenParser {
    private final static int MAX_PUBLIC_KEY_SIZE = 4;

    private JSONArray mJsonArray = null;

    private RSAPublicKey mRSAPublicKey = null;

    private static final String TAG = IDTokenParser.class.getSimpleName();

    /**
     * catch the public key in this map
     */
    private Map<String, RSAPublicKey> keyId2PublicKey = new HashMap<>();

    public IDTokenParser(){
    };

    /**
     * Verify Id Token
     *
     * @param idToken Your IdToken
     * @param callBack Asyn CallBack
     * @throws InvalidPublicKeyException throw when InvalidPublicKeyException happened
     */
    public void verify(String idToken, final ICallBack callBack) throws InvalidPublicKeyException, JWTDecodeException {
        final DecodedJWT decoder = JWT.decode(idToken);
        getRSAPublicKeyByKidAsyn(decoder.getKeyId(), new ICallBack() {
            @Override
            public void onSuccess() {
                try {
                    Algorithm algorithm = Algorithm.RSA256(mRSAPublicKey , null);
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    JSONObject jsonObject = new JSONObject(new String(Base64.decode(decoder.getPayload(), Base64.DEFAULT)));
                    // Verify the value of iss
                    if (!decoder.getIssuer().equals(Constant.ID_TOKEN_ISSUE)) {
                        callBack.onFailed();
                        return;
                    }
                    // Verify your app’s client ID.
                    String clientId = decoder.getAudience().get(0);
                    if(decoder.getAudience().size() > 0) {
                        if (!decoder.getAudience().get(0).equals(Constant.CLIENT_ID)) {
                            callBack.onFailed();
                            return;
                        }
                    }
                    // verify signature
                    verifier.verify(decoder);
                    jsonObject.put("alg", decoder.getAlgorithm());
                    jsonObject.put("typ", decoder.getType());
                    jsonObject.put("kid", decoder.getKeyId());
                    callBack.onSuccess(jsonObject.toString());
                } catch (JWTDecodeException | JSONException e){
                    callBack.onFailed();
                } catch (TokenExpiredException e) {
                    callBack.onFailed();
                    // jwt token is expire
                } catch (JWTVerificationException e) {
                    callBack.onFailed();
                    // VERIFY SIGNATURE failed
                } catch (Exception e) {
                    callBack.onFailed();
                } catch (Error e) {
                    callBack.onFailed();
                }
            }

            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onFailed() {
                callBack.onFailed();
            }
        });
    }

    /**
     * get the RSAPublicKey by kid
     * Please cache the RSAPublicKey
     * In the demo we cache it in a map
     *
     * @param keyId Input keyId
     * @param callBack asyn callback
     * @throws InvalidPublicKeyException throw when InvalidPublicKeyException happened
     */
    private void getRSAPublicKeyByKidAsyn(final String keyId, final ICallBack callBack) throws InvalidPublicKeyException {
        getJwks(new ICallBack() {
            @Override
            public void onSuccess() {
                if (keyId2PublicKey.get(keyId) != null) {
                    mRSAPublicKey = keyId2PublicKey.get(keyId);
                    callBack.onSuccess();
                } else {
                    if (mJsonArray == null) {
                        mRSAPublicKey = null;
                        return;
                    }
                    if (keyId2PublicKey.size() > MAX_PUBLIC_KEY_SIZE){
                        keyId2PublicKey.clear();
                    }

                    try {
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            String kid = mJsonArray.getJSONObject(i).getString("kid");
                            keyId2PublicKey.put(kid, getRsaPublicKeyByJwk(mJsonArray.getJSONObject(i)));
                        }
                        mRSAPublicKey = keyId2PublicKey.get(keyId);
                        callBack.onSuccess();
                    } catch (Exception e) {
                        mRSAPublicKey = null;
                        Log.i(TAG, "getRSAPublicKeyByKid failed: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onSuccess(String keys) {
            }

            @Override
            public void onFailed() {
                mRSAPublicKey = null;
            }
        });
    }

    /**
     * get jwks from the https://oauth-login.cloud.huawei.com/oauth2/v3/certs endpoint
     * because the jwk update each day, please cache the jwk,here is the example of jwks
     * See more about JWK in http://self-issued.info/docs/draft-ietf-jose-json-web-key.html
     * {
     *  "keys":[
     *   {
     *    "kty":"RSA",
     *    "e":"AQAB",
     *    "use":"sig",
     *    "kid":"670c64e7443941582167f405e0a62a08c6db5becb090f397a45aa572fa000553",
     *    "alg":"RS256",
     *    "n":"AK4C-h_gWpziPmzo6PEBuwxHHD2F9x_LgiE5zl73fVmzBTo3KzRu8nXURQA-uV857r_qEhfsJQyy0Nr_wIkfAU86JsFHcGwzLlJucN12EHXOFY6nLti9tSWUAWaa2HAZuJytyc-DyguLR_nH5IKmGhmcgI26zUG07UPUB2Xnsn-T-K1npnaNI7K7xlbGQy5UUPFjQPBRiV2R_-iGf5KIqCwebXe24wzhbWMnmfb0lilAZyYO7PiQ8UgJJTuZOMbCD7P0dUJwxitHo81OyoVJUGQZpLBZqHVSsRpC0UZuxMEMBPza4R55yZS3gAKkE1xILabCUV-CJ6Gp4c4J1tiFNcc"
     *   },
     *   {
     *    "kty":"RSA",
     *    "e":"AQAB",
     *    "use":"sig",
     *    "kid":"1226cda6b82e6aa140ffe2f32515f1929c3048b2cdba267935fb71963fd3e57a",
     *    "alg":"RS256",
     *    "n":"AMNDMIxlySrGqeV7V3s865ZwzBD0hXVq8ys0H_ZQGMbfWss0WuwHrmIRdq8OQrYoN_o2KZKtUPBsJpJMAZ26JeGqf3dsU_wFEEjNOfrDdyIs86K6gKfQFLewUNycmzMhlqDFlCujAAF33RYn-Xg7UXG3pO_2PdcD1zIrxtawQZ-TQTMgH5mgX_lWO1YfYSuB91xEvUZII1ZYDjLcbkzjZCbvfU4tT2_PV8-gU0UaHI-pcyreUwB1EhleDZUW8MiaIVSr6UIYfHflxBQRrwCcFy-q_u-OeYBS683djmbF-FKZVKlipKim6hGpqIb-PC8pHO_WmM01sNvvMAkF1D5bg0M"
     *   }
     *  ]
     * }
     */
    private void getJwks(final ICallBack iCallBack) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Constant.CERT_URL)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "Get ID Token failed.");
                iCallBack.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        mJsonArray = jsonObject.getJSONArray("keys");
                        iCallBack.onSuccess();
                    } catch (NullPointerException | JSONException | IOException e) {
                        Log.i(TAG, "parse JsonArray failed." + e.getMessage());
                        iCallBack.onFailed();
                    }
                }
            }
        });
    };

    /**
     * get RsaPublicKey from a JWK
     * @param jwkObject received JSONObject
     * @return RsaPublicKey from a JWK
     * @throws InvalidPublicKeyException
     */
    private RSAPublicKey getRsaPublicKeyByJwk(JSONObject jwkObject) throws InvalidPublicKeyException, JSONException {
        Map<String, Object> additionalAttributes = new HashMap<>();
        additionalAttributes.put("n", jwkObject.getString("n"));
        additionalAttributes.put("e", jwkObject.getString("e"));
        List<String> operations = new ArrayList<>();
        Jwk jwk = new Jwk(
                jwkObject.getString("kid"),
                jwkObject.getString("kty"),
                jwkObject.getString("alg"),
                jwkObject.getString("use"),
                operations,
                null,
                null,
                null,
                additionalAttributes);
        return (RSAPublicKey)jwk.getPublicKey();
    }
}

