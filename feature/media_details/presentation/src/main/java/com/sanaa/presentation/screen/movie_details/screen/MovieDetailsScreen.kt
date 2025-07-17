package com.sanaa.presentation.screen.movie_details.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.component.cards.ActorCard
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.component.DotSeparator
import com.sanaa.presentation.component.IconWithText
import com.sanaa.presentation.component.ImageSlider
import com.sanaa.presentation.component.InfoSection
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.component.boxContainerGradient
import com.sanaa.presentation.model.CastMemberUiModel
import com.sanaa.presentation.model.MovieDetailsUiModel
import com.sanaa.presentation.model.SimilarMovieUiModel
import com.sanaa.presentation.screen.movie_details.MovieDetailsUiEffect
import com.sanaa.presentation.screen.movie_details.interaction_listener.MovieDetailsScreenInteractionListener
import com.sanaa.presentation.screen.movie_details.view_model.MovieDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import com.sanaa.designsystem.R as designR
import com.sanaa.presentation.R as presentationR

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    navController: NavController,
    viewModel: MovieDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(null)

    LaunchedEffect(effect) {
        when (val e = effect) {
            is MovieDetailsUiEffect.OpenTrailer -> {
                val intent = Intent(Intent.ACTION_VIEW, e.url?.toUri())
                navController.context.startActivity(intent)
            }
            else -> Unit
        }
    }

    LaunchedEffect(movieId) {
        viewModel.onLoadMovieDetails(movieId)
    }

    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        if (!state.isLoading && state.data != null) {
            MovieDetailsContent(
                movieDetails = state.data!!,
                interactionListener = viewModel,
                modifier = Modifier
            )
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
@Composable
fun MovieDetailsContent(
    movieDetails: MovieDetailsUiModel,
    interactionListener: MovieDetailsScreenInteractionListener,
    modifier: Modifier = Modifier
) {
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(designR.drawable.icon_arrow_back),
                        onClick = { interactionListener.onBackClick() }
                    )
                },
                rightContent = {
                    SaveIconChip(
                        isSaved = movieDetails.isBookmarked,
                        onClick = { interactionListener.onBookmarkClick(movieDetails.id) }
                    )
                },
                modifier = Modifier
                    .systemBarsPadding()
                    .zIndex(10f)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    ImageSlider(
                        images = movieDetails.posterUrls,
                        contentDescription = movieDetails.title,
                        modifier = Modifier.fillMaxWidth()
                    )

                    InfoSection(
                        title = movieDetails.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 220.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(top = 16.dp),
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
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconWithText(
                                iconRes = designR.drawable.star,
                                contentDescription = null,
                                text = movieDetails.rating,
                                textColor = Theme.colors.title,
                                tint = Theme.colors.statusColors.yellowAccent
                            )
                            DotSeparator()
                            IconWithText(
                                iconRes = presentationR.drawable.icon_duration,
                                contentDescription = null,
                                text = movieDetails.duration,
                                tint = Theme.colors.body
                            )
                            DotSeparator()
                            IconWithText(
                                iconRes = presentationR.drawable.icon_calender,
                                contentDescription = null,
                                text = movieDetails.releaseDate,
                                tint = Theme.colors.body
                            )
                        }
                        TextButton(
                            text = stringResource(id = presentationR.string.view_reviews),
                            textColor = Theme.colors.primary,
                            onClick = {}
                        )
                    }
                }

                OverviewSection(
                    overview = movieDetails.overview,
                    onReadMore = { interactionListener.onReadMoreClick() },
                    modifier = Modifier.padding(16.dp),
                    titleResId = presentationR.string.overview
                )

                CastSection(cast = movieDetails.cast)

                MoreLikeThisSection(
                    similarMovies = movieDetails.similarMovies,
                    onBookmarkClick = { interactionListener.onBookmarkClick(it.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

            }

            if (movieDetails.trailerUrl != null) {
                BottomActionButtons(
                    onWatchTrailer = { interactionListener.onWatchTrailerClick() },
                    movieDetails = movieDetails,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CastSection(
    cast: List<CastMemberUiModel>,
    modifier: Modifier = Modifier
) {
    Column(

    ) {
        Text(
            text = "Cast",
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyRow(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
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
            style = Theme.textStyle.title.medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 7000.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(similarMovies.size) { index ->
                val movie = similarMovies[index]
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
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        designR.drawable.movie_placeholder_dark
    } else {
        designR.drawable.movie_placeholder_light
    }

    MovieSeriesPosterCard(
        modifier = modifier,
        poster = rememberAsyncImagePainter(model = movie.posterUrl),
        onCardClick = {  },
        boastImage = {
            RemoteBlurredHaramImageViewer(
                imageUrl = movie.posterUrl,
                modifier = Modifier.fillMaxSize(),
                blurRadius = 150,
                haramThreshold = 0.2f,
                nonHaramThreshold = 0.7f,
                placeholder = painterResource(placeholderResId),
                error = painterResource(placeholderResId),
                contentDescription = movie.title
            ) {
                OnBlurContent(
                    hintText = stringResource(designR.string.unsuitable_image),
                    textStyle = Theme.textStyle.body.small.copy(
                        color = Color(0x99FFFFFF)
                    ),
                    iconSize = 24.dp,
                    icon = painterResource(designR.drawable.icon_eye_slash)
                )
            }
        },
        topLeftContent = {
            SaveIconChip(
                isSaved = movie.isBookmarked,
                onClick = onBookmarkClick
            )
        }
    )
}
@Composable
fun BottomActionButtons(
    onWatchTrailer: () -> Unit,
    movieDetails: MovieDetailsUiModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .navigationBarsPadding()
    ) {
        Box(
            Modifier
                .fillMaxWidth().background(
                    brush = boxContainerGradient
                    )

        )
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PrimaryButton(
                text = null,
                onClick = {},
                modifier = Modifier,
                icon = painterResource(id = designR.drawable.outlined_star),
                iconTint = Theme.colors.onPrimary
            )

            PrimaryButton(
                text = "Play trailer",
                isEnabled = movieDetails.trailerUrl != null,
                onClick = onWatchTrailer,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}