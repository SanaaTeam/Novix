package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.api.navigation.LocalNavControllerProvider
import com.sanaa.presentation.screen.movieDetails.SnackData
import com.sanaa.presentation.shared_component.DotSeparator
import com.sanaa.presentation.shared_component.IconWithText
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.CastSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsHeaderSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsTopBar
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.GenresRow
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.MoviesSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.TrailerAndRateSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.presentation.screens.navigation.TvAppRoute.ActorDetails
import com.sanaa.tvapp.presentation.screens.navigation.TvAppRoute.Login
import com.sanaa.tvapp.presentation.screens.navigation.TvAppRoute.MovieDetails

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    var snack by remember { mutableStateOf<SnackData?>(null) }
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val navController = LocalNavControllerProvider.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                MovieDetailsScreenUiEffect.NavigateBack -> { navController.popBackStack()}
                is MovieDetailsScreenUiEffect.NavigateToActorScreen -> navController.navigate(ActorDetails(it.actorId))
                is MovieDetailsScreenUiEffect.NavigateToAnotherMovieDetails ->  navController.navigate(MovieDetails(it.movieId))
                MovieDetailsScreenUiEffect.NavigateToLogin -> navController.navigate(Login)
                is MovieDetailsScreenUiEffect.OpenTrailer -> TODO()
                MovieDetailsScreenUiEffect.ShowErrorSnackBar -> TODO()
                MovieDetailsScreenUiEffect.ShowSuccessSnackBar -> TODO()
            }
        }
    }

    Box(modifier = modifier.systemBarsPadding()) {
        MovieDetailsContent(
            state = state.value,
            interactionListener = viewModel
        )
        NovixAnimatedSnackBarHost(
            data = snack,
            onDismiss = { snack = null }
        )
    }

}

@Composable
fun MovieDetailsContent(
    state: MovieDetailsScreenUiState,
    interactionListener: MovieDetailsViewModel,
    modifier: Modifier = Modifier,
) {
    val moviesPagingData: LazyPagingItems<MovieDetailsUiModel> = state.similarMovies.collectAsLazyPagingItems()

    NovixScaffold(
        backgroundShapes = { },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = state.isLoading || state.noInternetConnection,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            )
            { shouldShowLoadingOrError ->
                if (shouldShowLoadingOrError) {
                    if (state.noInternetConnection) {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryLoadDetails() },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LoadingIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    {
                        Column (
                            modifier = Modifier
                                .verticalScroll(
                                state = rememberScrollState()
                            )
                        ){
                            DetailsHeaderSection(
                                backgroundImageUrl = state.movieDetails.posterUrl?:"",
                                title = state.movieDetails.title,
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {

                                    GenresRow(
                                        genres = state.movieDetails.genres,
                                        onGenreClicked = {
//                                            interactionListener::onGenreClicked
                                        }
                                    )
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        {
                                            state.movieDetails.rating.let {
                                                IconWithText(
                                                    iconRes = R.drawable.icon_star,
                                                    text = state.movieDetails.rating?:"",
                                                    textColor = Theme.colors.title,
                                                    contentDescription = state.movieDetails.rating,
                                                    tint = Theme.colors.statusColors.yellowAccent
                                                )

                                                DotSeparator()
                                            }
                                            state.movieDetails.releaseDate.let { releaseDate ->
                                                IconWithText(
                                                    text = releaseDate,
                                                    iconRes = R.drawable.icon_calender,
                                                    contentDescription = releaseDate,
                                                    tint = Theme.colors.hint
                                                )

                                                DotSeparator()
                                            }
                                        }
                                    }
                                    Text(
                                        text = state.movieDetails.overview,
                                        style = Theme.textStyle.body.small,
                                        color = Theme.colors.body
                                    )

                                    TrailerAndRateSection(
                                        trailerUrl = state.movieDetails.trailerUrl,
                                        onPlayTrailerClicked = interactionListener::onWatchTrailerClick,
                                    )
                                }
                            }

                            if (state.cast.isNotEmpty()) {
                                CastSlider(
                                    cast = state.cast,
                                )
                            }
                            MoviesSlider(
                                moviesPagingData = moviesPagingData,
                                modifier = Modifier.height(231.dp)
                            )
                        }
                    }
                }
            }
            DetailsTopBar(onBackClick = interactionListener::onBackClick)
        }
    }
}
//
//@Preview(device = Devices.TV_1080p, showBackground = true)
//@Composable
//fun DetailsPreview(modifier: Modifier = Modifier) {
//    NovixTheme(isSystemInDarkTheme()) {
//        MovieDetailsContent(state = state.value, interactionListener = viewModel)
//    }
//}