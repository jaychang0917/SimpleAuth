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

/**
 * Request code
 */
public class Constant {
    //login
    public static final int REQUEST_SIGN_IN_LOGIN = 1002;
    /**
     * your app’s client ID,please replace it of yours
     */
    public static final String CLIENT_ID = "101090009";

    /**
     * JWK JSON Web Key endpoint, developer can get the JWK of the last two days from this endpoint
     * See more about JWK in http://self-issued.info/docs/draft-ietf-jose-json-web-key.html
     */
    public static final String CERT_URL = "https://oauth-login.cloud.huawei.com/oauth2/v3/certs";
    /**
     *  Id Token issue
     */
    public static final String ID_TOKEN_ISSUE = "https://accounts.huawei.com";
}
