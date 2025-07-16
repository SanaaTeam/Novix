package com.sanaa.presentation.screen.review

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import java.time.LocalDate


@Composable
fun ReviewsScreen(
    viewModel: ReviewViewModel,
    onBackClicked: () -> Unit = {}
) {
    ReviewsScreenContent(emptyList(), onBackClicked)
}

@Composable
fun ReviewsScreenContent(
    reviews: List<ReviewUiState>,
    onBackClicked: () -> Unit = {}
) {
    NovixScaffold(
        topBar = {
            AppTopBar(
                screenTitle = stringResource(id = R.string.reviews),
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.arrow_left),
                        onClick = onBackClicked
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {

            if (reviews.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        reviews
                    ) { review ->
                        ReviewCard(review = review)
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyReviewsContent()
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
fun PreviewReviewScreen() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        val reviews = listOf(
            ReviewUiState(
                id = 1,
                authorName = "CinephileHub",
                username = "MovieBuff1967",
                content = "The show explores life's complexities through a whimsical metaphor involving a mouse in a man's pocket, which might perplex younger audiences. One episode poignantly addresses experiences her first period.",
                rating = 9.8f,
                createdDate = LocalDate.of(2001, 12, 3),
                avatarUrl = ""
            ),
            ReviewUiState(
                id = 2,
                authorName = "CinephileHub",
                username = "MovieBuff1967",
                content = "The show explores life's complexities through a whimsical metaphor involving a mouse in a man's pocket.",
                rating = 9.8f,
                createdDate = LocalDate.of(2001, 12, 3),
                avatarUrl = ""
            ),
            ReviewUiState(
                id = 3,
                authorName = "CinephileHub",
                username = "MovieBuff1967",
                content = "The show explores life's complexities through a whimsical metaphor involving a mouse in a man's pocket, which might perplex younger audiences. One episode poignantly addresses Anne's journey into womanhood.",
                rating = 9.8f,
                createdDate = LocalDate.of(2001, 12, 3),
                avatarUrl = ""
            )
        )
        ReviewsScreenContent(reviews = reviews)
    }
}

data class ReviewsUiState(
    val id: Int,
    val authorName: String,
    val username: String?,
    val content: String,
    val rating: Float?,
    val createdDate: LocalDate,
    val avatarUrl: String?
)

@PreviewLightDark
@Composable
fun EmptyReviewsStatePreview() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        ReviewsScreenContent(reviews = emptyList())
    }
}