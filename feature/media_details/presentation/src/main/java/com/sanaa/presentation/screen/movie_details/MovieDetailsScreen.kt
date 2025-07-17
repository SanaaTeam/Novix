package com.sanaa.presentation.screen.movie_details

import android.content.Intent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.component.DotSeparator
import com.sanaa.presentation.component.IconWithText
import com.sanaa.presentation.component.ImageSlider
import com.sanaa.presentation.component.InfoSection
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.model.MovieDetailsUiModel
import com.sanaa.presentation.screen.movie_details.components.BottomActionButtons
import com.sanaa.presentation.screen.movie_details.components.CastSection
import com.sanaa.presentation.screen.movie_details.components.MoreLikeThisSection
import com.sanaa.presentation.screen.movie_details.MovieDetailsViewModel
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

