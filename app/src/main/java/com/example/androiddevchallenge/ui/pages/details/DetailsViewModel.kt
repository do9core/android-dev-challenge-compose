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
package com.example.androiddevchallenge.ui.pages.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.Puppy
import com.example.androiddevchallenge.data.PuppyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel : ViewModel() {

    private val _puppy: MutableStateFlow<Puppy?> = MutableStateFlow(null)
    val puppy: StateFlow<Puppy?> get() = _puppy.asStateFlow()

    val isFavourite: StateFlow<Boolean> =
        combine(puppy, PuppyRepository.favourite) { currentPuppy, favourites ->
            currentPuppy?.id in favourites
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), false)

    fun findPuppy(puppyId: Long): Job {
        return viewModelScope.launch {
            PuppyRepository.puppies().collect { puppies ->
                _puppy.value = withContext(Dispatchers.Default) {
                    puppies?.find { it.id == puppyId }
                }
            }
        }
    }

    fun toggleFavourite() {
        val puppyId = puppy.value?.id ?: return
        if (isFavourite.value) {
            PuppyRepository.cancelFavourite(puppyId)
        } else {
            PuppyRepository.favourite(puppyId)
        }
    }
}
