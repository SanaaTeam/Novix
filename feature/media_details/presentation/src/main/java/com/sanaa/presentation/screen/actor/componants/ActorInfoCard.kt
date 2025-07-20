package com.sanaa.presentation.screen.actor.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R
import com.sanaa.presentation.component.DotSeparator
import com.sanaa.presentation.component.IconWithText
import com.sanaa.presentation.component.InfoSection
import com.sanaa.presentation.model.ActorUiModel

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
                AttributeWithDot(text = it, iconRes = R.drawable.location)
            }

            actor.lifeSpan?.let { AttributeWithDot(it, R.drawable.birthday_cake) }
        }
    }
}

@Composable
private fun AttributeWithDot(text: String, iconRes: Int, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        DotSeparator()
        IconWithText(
            iconRes = iconRes,
            text = text,
            contentDescription = "",
            tint = Theme.colors.body
        )
    }
}