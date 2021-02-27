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
package com.example.androiddevchallenge.ui.pages.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.Puppies
import com.example.androiddevchallenge.data.Puppy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random

class HomeViewModel : ViewModel() {

    private val mutex = Mutex()
    private var isLoading: Boolean = false

    var puppies: List<Puppy>? by mutableStateOf(null)
    var favourites: Set<Long> by mutableStateOf(emptySet())

    init {
        loadPuppies()
    }

    fun loadPuppies() {
        viewModelScope.launch {
            mutex.withLock {
                if (isLoading) {
                    return@launch
                }
                isLoading = true
                puppies = null
                delay(1500)
                puppies = if (Random.nextDouble() < 0.2) emptyList() else Puppies
                isLoading = false
            }
        }
    }

    fun toggleFavourite(puppyId: Long, favourite: Boolean) {
        favourites = if (favourite) {
            favourites + puppyId
        } else {
            favourites - puppyId
        }
    }
}
