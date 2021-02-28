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
package com.example.androiddevchallenge.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.random.Random

object PuppyRepository {

    private val favouriteInternal: MutableStateFlow<Set<Long>> = MutableStateFlow(emptySet())
    val favourite: StateFlow<Set<Long>> get() = favouriteInternal

    fun puppies(): Flow<List<Puppy>?> {
        return flow {
            emit(null)
            delay(1000)
            val puppies = if (Random.nextDouble() > 0.2) Puppies else emptyList()
            emit(puppies.sortedBy { it.name })
        }.flowOn(Dispatchers.IO)
    }

    fun favourite(puppyId: Long) {
        favouriteInternal.value += puppyId
    }

    fun cancelFavourite(puppyId: Long) {
        favouriteInternal.value -= puppyId
    }
}
