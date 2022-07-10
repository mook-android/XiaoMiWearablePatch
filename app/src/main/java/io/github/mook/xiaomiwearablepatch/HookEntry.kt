/**
 * Copyright 2022 Mook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.mook.xiaomiwearablepatch

import android.content.ContextWrapper
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HookEntry : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam?.packageName != "com.xiaomi.wearable") {
            return
        }

        findAndHookMethod(
            ContextWrapper::class.java,
            "getApplicationContext",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    param ?: return
                    if (param.result == null) {
                        param.result = param.thisObject
                    }
                }
            })
    }
}