package com.sanaa.presentation.screen.movie_details.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sanaa.designsystem.design_system.component.cards.ActorCard
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.model.CastMemberUiModel
import com.sanaa.presentation.R as presentationR

@Composable
fun CastSection(
    cast: List<CastMemberUiModel>,
) {
    Column {
        Text(
            text = stringResource(id = presentationR.string.cast),
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

