/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument
import com.example.androiddevchallenge.utils.BaseRoute

object Home : BaseRoute("home")

private const val DETAILS_FORMAT = "details/%s"

const val DETAILS_PUPPY_ID = "puppy_id"

object Details : BaseRoute(DETAILS_FORMAT.format("{$DETAILS_PUPPY_ID}")) {

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(DETAILS_PUPPY_ID) {
                type = NavType.LongType
                nullable = false
            }
        )

    fun forPuppy(puppyId: Long): String {
        return DETAILS_FORMAT.format(puppyId.toString())
    }
}
