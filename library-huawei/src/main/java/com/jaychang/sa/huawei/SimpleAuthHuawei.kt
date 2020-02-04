/*
 *    Copyright 2017 Jay Chang Copyright 2020 Huawei Technologies Co., Ltd
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
 *
 *     2020.1.10-Changed modify api implement
 *     Huawei Technologies Co., Ltd.
 */
package com.jaychang.sa.huawei

import com.jaychang.sa.AuthCallback
import com.jaychang.sa.AuthData
import com.jaychang.sa.AuthDataHolder
import com.jaychang.sa.Initializer

object SimpleAuth {
    @JvmStatic
    fun connectHuawei(listener: AuthCallback) {
        AuthDataHolder.getInstance().huaweiAuthData = AuthData(listOf(), listener)
        HuaweiIdActivity.start(Initializer.context)
    }

    @JvmStatic
    fun disconnectHuawei() {
        AuthDataHolder.getInstance().huaweiAuthData = null
        HuaweiIdActivity.signOut();
    }

    @JvmStatic
    fun revokeHuawei() {
        HuaweiIdActivity.revoke();
    }
}
