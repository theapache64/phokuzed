/*
 * Copyright 2021 Boil (https://github.com/theapache64/boil)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.theapache64.phokuzed.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.theapache64.phokuzed.BuildConfig
import timber.log.Timber

class Ref(var value: Int)

/**
 * Note the inline function below which ensures that this function is essentially
 * copied at the call site to ensure that its logging only recompositions from the
 * original call site.
 * Author: @vinaygaba
 */
@Composable
@Suppress("NOTHING_TO_INLINE")
inline fun LogCompositions(tag: String, msg: String) {
    if (BuildConfig.DEBUG) {
        val ref = remember { Ref(0) }
        SideEffect { ref.value++ }
        Timber.d("Compositions: $tag: $msg ${ref.value}")
    }
}
