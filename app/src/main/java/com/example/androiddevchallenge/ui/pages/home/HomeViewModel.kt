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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.Puppy
import com.example.androiddevchallenge.data.PuppyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

private typealias PuppyData = Map<Char, List<Puppy>>

class HomeViewModel : ViewModel() {

    private val mutex = Mutex()
    private var isLoading: Boolean = false

    private val _groupedPuppies: MutableStateFlow<PuppyData?> = MutableStateFlow(null)
    val groupedPuppies: StateFlow<PuppyData?> get() = _groupedPuppies

    val favourites: StateFlow<Set<Long>> get() = PuppyRepository.favourite

    init {
        loadPuppies()
    }

    fun loadPuppies() {
        viewModelScope.launch {
            mutex.withLock {
                if (isLoading) {
                    return@withLock
                }
                isLoading = true
                PuppyRepository.puppies().collect { sortedPuppies ->
                    _groupedPuppies.value = withContext(Dispatchers.Default) {
                        sortedPuppies?.groupBy { it.name[0] }
                    }
                }
                isLoading = false
            }
        }
    }

    fun toggleFavourite(puppyId: Long, favourite: Boolean) {
        if (favourite) {
            PuppyRepository.favourite(puppyId)
        } else {
            PuppyRepository.cancelFavourite(puppyId)
        }
    }
}
