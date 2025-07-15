package com.sanaa.presentation.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.ActorViewModel
import com.sanaa.presentation.screen.componants.SectionHeader
import entity.Actor
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActorScreen(
    viewModel: ActorViewModel = koinViewModel<ActorViewModel>(),
    actorId: Int
) {

    LaunchedEffect(actorId) {
        viewModel.loadActor(actorId)
    }

    NovixTheme(isSystemInDarkTheme()) {

        ActorScreenContent(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorScreenContent(
    modifier: Modifier = Modifier,
    viewModel: ActorViewModel
) {
    Log.d("ActorScreenContent", "viewModel.actor: ${viewModel.actor}")
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(viewModel.actor?.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(252.dp)                                // fixed height
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 12.dp, bottomEnd = 12.dp
                        )
                    ),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter                   // ← keep the TOP part
            )
            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = com.sanaa.designsystem.R.drawable.icon_arrow_back),
                        onClick = {})
                }, modifier = Modifier.padding(top = 12.dp)
            )
        }

        ActorInfoCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .offset(y = -(16).dp)
                .clip(RoundedCornerShape(16.dp))
                .border(                                        // ← 1 dp stroke
                    width = 1.dp,
                    color = Theme.colors.stroke,               // pick whatever colour you need
                    shape = RoundedCornerShape(16.dp)
                )
                .background(Theme.colors.surface)
                .padding(12.dp),
            actor = viewModel.actor ?:Actor(
                id = 1,
                imageUrl = "https://image.tmdb.org/t/p/w500/xyz.jpg",
                name = "John Doe",
                region = "US",
                lastShow = "Some Movie (2024)",
                gender = Actor.Gender.MALE,
                character = "Acting",
                birthDate = LocalDate(1980, 1, 1),
                deathDate = null,
                placeOfBirth = "Somewhere, USA",
                biography = "A short bio text.",
                department = "Acting"
            )
        )


        LazyColumn(
            modifier = Modifier, contentPadding = PaddingValues(bottom = 24.dp, top = 12.dp)
        ) {
            if (viewModel.topMovies.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Gallery",
                        actionText = "All ->",
                        onActionClick = {} // interactionsListener::onClearRecentViewClicked
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(
                            start = 16.dp, end = 16.dp, bottom = 24.dp
                        )
                    ) {
                        itemsIndexed(viewModel.topMovies) { _, item ->
                            MovieSeriesPosterCard(
                                modifier = Modifier
                                    .width(158.dp)
                                    .height(210.dp),
                                boastImage = {
                                    RemoteCensoredImageViewer(
                                        imageUrl = item.posterImageUrl,
                                        modifier = Modifier,
                                        contentScale = ContentScale.Crop,
                                        blurRadius = 150,
                                        sfwThreshold = 0.75f,
                                        nsfwThreshold = 0.15f,
                                        contentDescription = null,
                                        placeholderBackgroundColor = Theme.colors.surface,
                                        hintText = stringResource(com.sanaa.presentation.R.string.unsuitable_image),
                                        textStyle = Theme.textStyle.body.small,
                                        iconSize = 24.dp,
                                    )
                                },
                                topLeftContent = {
                                    SaveIconChip(
                                        onClick = {} //interactionsListener::onSaveIconClicked
                                    )
                                },
                            )
                        }
                    }
                }
            }

        }

    }
}

@Composable
private fun ActorInfoCard(
    modifier: Modifier = Modifier,
    actor: Actor
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column() {
            Text(
                text = "Tom Hanks", style = Theme.textStyle.title.medium
            )

            Spacer(Modifier.height(8.dp))

            /* profession + place */
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = actor.name, style = Theme.textStyle.label.small
                )
                Box(
                    modifier
                        .size(3.dp, 3.dp)
                        .background(Theme.colors.body)
                        .clip(CircleShape)
                )
                Row {
                    Icon(
                        painterResource(com.sanaa.designsystem.R.drawable.icon_home),
                        null,
                        Modifier.size(14.dp)
                    )
                    Text(
                        text = actor.placeOfBirth.toString(), style = Theme.textStyle.label.small
                    )
                }
                Box(
                    modifier
                        .size(3.dp, 3.dp)
                        .background(Theme.colors.body)
                        .clip(CircleShape)
                )
            }

            Spacer(Modifier.height(12.dp))

            /* birthday … deathday */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(com.sanaa.designsystem.R.drawable.icon_home),
                    null,
                    Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = actor.birthDate.toString(), style = Theme.textStyle.label.small
                )
                actor.deathDate?.let {
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = it.toString(),
                        style = Theme.textStyle.label.small
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ActorScreenPreview() {
    NovixTheme(isSystemInDarkTheme()) {
//        ActorScreenContent(Modifier.fillMaxSize())
    }
}