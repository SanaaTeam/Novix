package com.sanaa.presentation.screen.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import java.time.LocalDate

import androidx.compose.material3.Text

@Composable
fun ReviewCard(
    review: Review,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val contentPreview = if (review.content.length > 150 && !expanded)
        review.content.take(150) + "..."
    else review.content

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Theme.colors.surface, shape = RoundedCornerShape(12.dp))
            .border(0.5.dp, Theme.colors.stroke, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = review.avatarResId),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Theme.colors.stroke, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = review.authorName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Theme.colors.title
                )
                review.userHandle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = Theme.colors.body
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "rating",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = review.rating?.toString() ?: "-",
                    style = MaterialTheme.typography.labelSmall,
                    color = Theme.colors.title
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = contentPreview,
            fontSize = 14.sp,
            color = Color(0xFF49454F),
            lineHeight = 20.sp,
            maxLines = if (expanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis
        )

        if (review.content.length > 150 && !expanded) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Read more",
                style = MaterialTheme.typography.labelMedium,
                color = Theme.colors.primary,
                modifier = Modifier.clickable { expanded = true }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.date_icon),
                contentDescription = "date",
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = review.createdDate.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = Theme.colors.body
            )
        }
    }
}

@Composable
fun ReviewScreen(
    onBackClicked: () -> Unit = {}
) {
    val reviews = listOf(
        Review(
            id = 1,
            authorName = "CinephileHub",
            userHandle = "MovieBuff1967",
            content = "The show explores life's complexities through a whimsical metaphor involving a mouse in a man's pocket, which might perplex younger audiences. One episode poignantly addresses experiences her first period.",
            rating = 9.8f,
            createdDate = LocalDate.of(2001, 12, 3),
            avatarResId = R.drawable.cinephile1
        ),
        Review(
            id = 2,
            authorName = "CinephileHub",
            userHandle = "MovieBuff1967",
            content = "The show explores life's complexities through a whimsical metaphor involving a mouse in a man's pocket.",
            rating = 9.8f,
            createdDate = LocalDate.of(2001, 12, 3),
            avatarResId = R.drawable.cinephile2
        ),
        Review(
            id = 3,
            authorName = "CinephileHub",
            userHandle = "MovieBuff1967",
            content = "The show explores life's complexities through a whimsical metaphor involving a mouse in a man's pocket, which might perplex younger audiences. One episode poignantly addresses Anne's journey into womanhood.",
            rating = 9.8f,
            createdDate = LocalDate.of(2001, 12, 3),
            avatarResId = R.drawable.user_img
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp)
    ) {
        AppTopBar(
            screenTitle = "Reviews",
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(R.drawable.arrow_left),
                    onClick = onBackClicked
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        reviews.forEach {
            ReviewCard(review = it, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewReviewScreen() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        ReviewScreen()
    }
}

data class Review(
    val id: Int,
    val authorName: String,
    val userHandle: String?,
    val content: String,
    val rating: Float?,
    val createdDate: LocalDate,
    val avatarResId: Int
)
