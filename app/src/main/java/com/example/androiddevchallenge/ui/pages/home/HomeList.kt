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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.androiddevchallenge.data.Puppy
import com.example.androiddevchallenge.ui.Details
import com.example.androiddevchallenge.ui.components.AppTopBar
import com.example.androiddevchallenge.ui.components.LoadingBody
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun HomeList(navController: NavController) {
    val viewModel = viewModel<HomeViewModel>()
    Scaffold(
        topBar = { AppTopBar() },
        floatingActionButton = {
            AddFab(
                onClick = { viewModel.loadPuppies() }
            )
        },
    ) {
        val puppies = viewModel.puppies
        when {
            puppies == null -> LoadingBody()
            puppies.isEmpty() -> EmptyListBody()
            else -> LazyColumn {
                items(puppies) { puppy ->
                    PuppyItem(
                        puppy = puppy,
                        onClick = {
                            val destination = Details.forPuppy(it.id)
                            navController.navigate(destination)
                        },
                        isFavourite = puppy.id in viewModel.favourites,
                        onToggleFavourite = { puppyId, favourite ->
                            viewModel.toggleFavourite(puppyId, favourite)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyListBody() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "No puppies found.",
            style = MaterialTheme.typography.h5.copy(
                color = MaterialTheme.typography.h5.color.copy(
                    alpha = 0.5f
                )
            ),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
fun AddFab(onClick: () -> Unit) {
    FloatingActionButton(onClick) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Add a new puppy."
        )
    }
}

@Composable
fun LazyItemScope.PuppyItem(
    puppy: Puppy,
    isFavourite: Boolean,
    onClick: (puppy: Puppy) -> Unit,
    onToggleFavourite: (puppyId: Long, favourite: Boolean) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillParentMaxWidth()
            .padding(4.dp)
            .clickable { onClick(puppy) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
        ) {
            CoilImage(
                data = puppy.imageSource,
                contentDescription = "Puppy image",
                fadeIn = true,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(128.dp)
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "${puppy.name} ${if (puppy.male) "♂️" else "♀️"}",
                    style = MaterialTheme.typography.body1,
                )
                Box(modifier = Modifier.height(2.dp))
                Text(
                    text = puppy.variety,
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.typography.caption.color.copy(
                            alpha = 0.4f
                        )
                    ),
                )

                IconToggleButton(
                    checked = isFavourite,
                    onCheckedChange = { onToggleFavourite(puppy.id, it) },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Icon(
                        imageVector = if (isFavourite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = "Favourite toggle button.",
                        tint = if (isFavourite) {
                            Color.Red.copy(alpha = 0.85f)
                        } else {
                            Color.Gray
                        }
                    )
                }
            }
        }
    }
}
