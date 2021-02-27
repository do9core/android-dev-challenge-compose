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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.androiddevchallenge.data.Puppy
import com.example.androiddevchallenge.data.displayWeight
import com.example.androiddevchallenge.ui.components.AppTopBar
import com.example.androiddevchallenge.ui.components.LoadingBody
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun Details(navController: NavController, puppyId: Long) {
    val viewModel = viewModel<DetailsViewModel>()
    DetailsInternal(navController, viewModel.puppy)
    DisposableEffect(puppyId) {
        val job = viewModel.findPuppy(puppyId)
        onDispose {
            job.cancel()
        }
    }
}

@Composable
private fun DetailsInternal(navController: NavController, puppy: Puppy?) {
    Scaffold(
        topBar = {
            AppTopBar(
                showCloseButton = true,
                onClose = { navController.navigateUp() },
            )
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (puppy == null) {
                LoadingBody()
            } else {
                DetailsBody(puppy)
            }
        }
    }
}

@Composable
fun DetailKeyText(text: String) {
    Text(
        text = "$text:",
        style = MaterialTheme.typography.body1.copy(
            fontWeight = Bold,
        ),
        modifier = Modifier.padding(4.dp),
    )
}

@Composable
fun DetailValueText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(4.dp)
    )
}

@Composable
fun DetailsBody(puppy: Puppy) {
    Column {
        CoilImage(
            data = puppy.imageSource,
            contentDescription = "Puppy's image",
            fadeIn = true,
        )
        Text(
            text = "Puppy details:",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column {
                DetailKeyText(text = "Name")
                DetailKeyText(text = "Gender")
                DetailKeyText(text = "Age")
                DetailKeyText(text = "Variety")
                DetailKeyText(text = "Weight")
            }
            Column {
                DetailValueText(text = puppy.name)
                DetailValueText(text = if (puppy.male) "Male" else "Female")
                DetailValueText(text = puppy.ageInYear.toString())
                DetailValueText(text = puppy.variety)
                DetailValueText(text = puppy.displayWeight)
            }
        }
    }
}
