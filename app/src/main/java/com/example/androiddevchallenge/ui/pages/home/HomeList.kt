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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HomeList(navController: NavController) {
    val viewModel = viewModel<HomeViewModel>()
    val listState = rememberLazyListState()
    Scaffold(
        topBar = { AppTopBar() },
        floatingActionButton = {
            val fabSize by animateDpAsState(
                targetValue = if (listState.isScrollInProgress) 0.dp else 56.dp
            )
            FloatingActionButton(
                onClick = { viewModel.loadPuppies() },
                modifier = Modifier.requiredSize(fabSize),
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Add a new puppy.",
                )
            }
        },
    ) {
        val groupedPuppies by viewModel.groupedPuppies.collectAsState()
        val favouriteIds by viewModel.favourites.collectAsState()

        @Suppress("UnnecessaryVariable")
        val lGroupedPuppies = groupedPuppies
        when {
            lGroupedPuppies == null -> LoadingBody()
            lGroupedPuppies.isEmpty() -> EmptyListBody()
            else -> LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 80.dp),
            ) {
                lGroupedPuppies.forEach { (groupCharacter, puppies) ->
                    stickyHeader { GroupHeader(character = groupCharacter) }
                    itemsIndexed(puppies) { index, puppy ->
                        PuppyItem(
                            puppy = puppy,
                            onClick = {
                                val destination = Details.forPuppy(it.id)
                                navController.navigate(destination)
                            },
                            isFavourite = puppy.id in favouriteIds,
                            onToggleFavourite = { puppyId, favourite ->
                                viewModel.toggleFavourite(puppyId, favourite)
                            }
                        )
                        if (index != puppies.lastIndex) {
                            Divider()
                        }
                    }
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
fun LazyItemScope.GroupHeader(character: Char) {
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .background(MaterialTheme.colors.secondary.copy(alpha = 0.2f))
    ) {
        Text(
            text = character.toString(),
            style = MaterialTheme.typography.subtitle1.copy(
                color = MaterialTheme.typography.subtitle1.color.copy(alpha = 0.7f)
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .height(88.dp)
            .clickable { onClick(puppy) }
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
                .weight(1f)
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
        }
        IconToggleButton(
            checked = isFavourite,
            onCheckedChange = { onToggleFavourite(puppy.id, it) },
            modifier = Modifier.align(Alignment.CenterVertically),
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
