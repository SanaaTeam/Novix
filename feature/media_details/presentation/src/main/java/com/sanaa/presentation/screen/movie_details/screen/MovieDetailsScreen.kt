package com.sanaa.presentation.screen.movie_details.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.cards.ActorCard
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R as presentationR
import com.sanaa.designsystem.R as designR
import com.sanaa.presentation.component.DotSeparator
import com.sanaa.presentation.component.IconWithText
import com.sanaa.presentation.component.ImageSlider
import com.sanaa.presentation.component.InfoSection
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.model.CastMemberUiModel
import com.sanaa.presentation.model.MovieDetailsUiModel
import com.sanaa.presentation.model.SimilarMovieUiModel
import com.sanaa.presentation.screen.movie_details.MovieDetailsViewModel
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MovieDetailsViewModel = koinViewModel() // Use Koin's viewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.onLoadMovieDetails(movieId)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colors.surface)
            .clip(RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }
            state.errorMessage != null -> {
                Text(
                    text = state.errorMessage ?: "",
                    modifier = Modifier.padding(16.dp)
                )
            }
            state.data != null -> {
                MovieDetailsContent(
                    movieDetails = state.data!!,
                    onWatchTrailer = {
                        viewModel.onWatchTrailerClick()
                        navController.navigate("trailer_screen/$movieId")
                    },
                    onReadMore = { viewModel.onReadMoreClick() },
                    onBookmarkClick = { movie -> viewModel.onBookmarkClick(movie.id) },
                    onBackClick = {
                        viewModel.onBackClick()
                        navController.popBackStack()
                    },
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        }
    }
}
@Composable
fun MovieDetailsContent(
    movieDetails: MovieDetailsUiModel,
    modifier: Modifier = Modifier,
    onWatchTrailer: () -> Unit = {},
    onReadMore: () -> Unit = {},
    onBookmarkClick: (SimilarMovieUiModel) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box {
            ImageSlider(
                images = movieDetails.posterUrls,
                contentDescription = movieDetails.title,
                modifier = Modifier.fillMaxWidth()
            )

            AppTopBar(
                screenTitle = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp),
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = designR.drawable.icon_arrow_back),
                        onClick = onBackClick
                    )
                },
                rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = if (movieDetails.isBookmarked) designR.drawable.icon_saved else designR.drawable.icon_unsaved),
                        onClick = { onBookmarkClick(SimilarMovieUiModel(movieDetails.id, movieDetails.posterUrls.firstOrNull() ?: "", movieDetails.title, movieDetails.isBookmarked)) }
                    )
                }
            )
        }

        InfoSection(
            title = movieDetails.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconWithText(
                    iconRes = designR.drawable.star,
                    contentDescription = "",
                    text = movieDetails.rating,
                    tint = Theme.colors.statusColors.yellowAccent
                )

                DotSeparator()

                IconWithText(
                    iconRes = presentationR.drawable.icon_calender,
                    contentDescription = "",
                    text = movieDetails.releaseDate,
                    tint = Theme.colors.body
                )

                DotSeparator()

                IconWithText(
                    iconRes = presentationR.drawable.icon_duration,
                    contentDescription = "",
                    text = movieDetails.duration,
                    tint = Theme.colors.body
                )
            }

            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                movieDetails.genres.forEachIndexed { index, genre ->
                    Text(
                        text = genre,
                        style = Theme.textStyle.label.small,
                        color = Theme.colors.body
                    )
                    if (index < movieDetails.genres.lastIndex) {
                        DotSeparator()
                    }
                }
            }
        }

        OverviewSection(
            overview = movieDetails.overview,
            onReadMore = onReadMore,
            modifier = Modifier.padding(horizontal = 16.dp),
            titleResId = presentationR.string.overview
        )

        CastSection(
            cast = movieDetails.cast,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )

        MoreLikeThisSection(
            similarMovies = movieDetails.similarMovies,
            onBookmarkClick = onBookmarkClick,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(120.dp))
    }

    BottomActionButtons(
        onWatchTrailer = onWatchTrailer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    )
}
@Composable
fun CastSection(
    cast: List<CastMemberUiModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = 16.dp)
    ) {
        Text(
            text = "Cast",
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium
        )

        LazyRow(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cast) { actor ->
                ActorCard(
                    actorName = actor.name,
                    actorImage = rememberAsyncImagePainter(model = actor.imageUrl),
                    playedCharacter = actor.character
                )
            }
        }
    }
}

@Composable
fun MoreLikeThisSection(
    similarMovies: List<SimilarMovieUiModel>,
    onBookmarkClick: (SimilarMovieUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = 16.dp)
    ) {
        Text(
            text = "More like this",
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium
        )

        LazyRow(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(similarMovies) { movie ->
                MoreLikeThisCard(
                    movie = movie,
                    onBookmarkClick = { onBookmarkClick(movie) }
                )
            }
        }
    }
}

@Composable
fun MoreLikeThisCard(
    movie: SimilarMovieUiModel,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(158.dp)
            .aspectRatio(0.845f)
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colors.surface)
            .border(1.dp, Theme.colors.stroke, RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = onBookmarkClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(32.dp)
                .background(Theme.colors.surface.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .border(1.dp, Theme.colors.stroke, RoundedCornerShape(8.dp))
        ) {
            Icon(
                painter = painterResource(
                    id = if (movie.isBookmarked)
                        designR.drawable.icon_saved
                    else
                        designR.drawable.icon_unsaved
                ),
                contentDescription = "",
                tint = Theme.colors.body,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun BottomActionButtons(
    onWatchTrailer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryButton(
            text = null,
            onClick = {},
            modifier = Modifier.size(52.dp),
            icon = painterResource(id = designR.drawable.outlined_star),
            iconTint = Theme.colors.onPrimary
        )

        PrimaryButton(
            text = "Play trailer",
            onClick = onWatchTrailer,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        )
    }
}