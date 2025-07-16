package com.sanaa.presentation.screen.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import java.time.LocalDate
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.collections.isNotEmpty

@Composable
fun ReviewCard(
    review: Review,
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Theme.colors.surface, shape = RoundedCornerShape(12.dp))
            .border(1.dp, Theme.colors.stroke, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(review.avatarUrl)
                    .build(),
                contentDescription = review.authorName,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Theme.colors.stroke, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = review.authorName,
                    style = Theme.textStyle.title.medium,
                    color = Theme.colors.title
                )
                review.username?.let {
                    Text(
                        text = it,
                        style = Theme.textStyle.label.small,
                        color = Theme.colors.hint
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = stringResource(id = R.string.rating),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = review.rating?.toString() ?: "-",
                    style = Theme.textStyle.label.small,
                    color = Theme.colors.title
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = review.content,
            style = Theme.textStyle.body.small,
            color = Theme.colors.body,
            maxLines = if (expanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis
        )
        if (review.content.length > 150 && !expanded) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.read_more),
                style = Theme.textStyle.label.medium,
                color = Theme.colors.primary,
                modifier = Modifier.clickable { expanded = true }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.date_icon),
                contentDescription = stringResource(id = R.string.date),
                tint = Theme.colors.body,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = review.createdDate.toString(),
                style = Theme.textStyle.label.small,
                color = Theme.colors.body
            )
        }
    }
}

@Composable
fun ReviewScreen(
    reviews: List<Review>,
    onBackClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        AppTopBar(
            screenTitle = stringResource(id = R.string.reviews),
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(R.drawable.arrow_left),
                    onClick = onBackClicked
                )
            }
        )
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
            EmptyReviewsState(onBackClicked)
        }
    }
}

@Composable
fun EmptyReviewsState(
    onBackClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
    ) {
        AppTopBar(
            screenTitle = stringResource(id = R.string.reviews),
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(R.drawable.arrow_left),
                    onClick = onBackClicked
                )
            }
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chat),
                    contentDescription = "chat",
                    modifier = Modifier.size(128.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(id = R.string.no_review),
                    style = Theme.textStyle.body.small,
                    color = Theme.colors.body
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewReviewScreen() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        val reviews = listOf(
            Review(
                id = 1,
                authorName = "CinephileHub",
                username = "MovieBuff1967",
                content = "The show explores life's complexities through a whimsical metaphor involving a mouse in a man's pocket, which might perplex younger audiences. One episode poignantly addresses experiences her first period.",
                rating = 9.8f,
                createdDate = LocalDate.of(2001, 12, 3),
                avatarUrl = ""
            ),
            Review(
                id = 2,
                authorName = "CinephileHub",
                username = "MovieBuff1967",
                content = "The show explores life's complexities through a whimsical metaphor involving a mouse in a man's pocket.",
                rating = 9.8f,
                createdDate = LocalDate.of(2001, 12, 3),
                avatarUrl = ""
            ),
            Review(
                id = 3,
                authorName = "CinephileHub",
                username = "MovieBuff1967",
                content = "The show explores life's complexities through a whimsical metaphor involving a mouse in a man's pocket, which might perplex younger audiences. One episode poignantly addresses Anne's journey into womanhood.",
                rating = 9.8f,
                createdDate = LocalDate.of(2001, 12, 3),
                avatarUrl = ""
            )
        )
        ReviewScreen(reviews = reviews)
    }
}

data class Review(

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
        EmptyReviewsState()
    }
}
