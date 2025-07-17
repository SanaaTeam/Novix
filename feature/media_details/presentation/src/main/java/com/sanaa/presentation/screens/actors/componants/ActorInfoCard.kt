package com.sanaa.presentation.screens.actors.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.component.DotSeparator
import com.sanaa.presentation.component.IconWithText
import com.sanaa.presentation.component.InfoSection
import com.sanaa.presentation.screens.actors.ActorUiModel

@Composable
fun ActorInfoCard(
    actor: ActorUiModel,
    modifier: Modifier = Modifier,
) {
    InfoSection(title = actor.name, modifier = modifier) {

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            actor.department?.let {
                Text(
                    text = it,
                    style = Theme.textStyle.label.small,
                    color = Theme.colors.body,
                )
            }

            actor.placeOfBirth?.let {
                AttributeWithDot(text = it)
            }

            actor.birthDate?.let {
                AttributeWithDot(text = it)
            }

            actor.deathDate?.let {
                AttributeWithDot(text = it)
            }
        }
    }
}

@Composable
private fun AttributeWithDot(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DotSeparator()
        IconWithText(
            iconRes = R.drawable.cancel,
            text = text,
            contentDescription = "",
            tint = Theme.colors.body
        )
    }
}