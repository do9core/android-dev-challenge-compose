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

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.ui.pages.details.Details
import com.example.androiddevchallenge.ui.pages.home.HomeList
import com.example.androiddevchallenge.utils.composable

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController, Home.name) {
        composable(Home) {
            HomeList(navController)
        }
        composable(Details) { stackEntry ->
            val puppyId = stackEntry.arguments?.getLong(DETAILS_PUPPY_ID)
            checkNotNull(puppyId) { "Details should have a puppy id argument." }
            Details(navController, puppyId)
        }
    }
}
