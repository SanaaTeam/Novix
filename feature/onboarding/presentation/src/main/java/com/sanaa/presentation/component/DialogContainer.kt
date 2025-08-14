package com.sanaa.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun DialogContainer(
    pageContent: OnBoardingPageContentItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppText(text = stringResource(id = pageContent.title),
            textAlign = TextAlign.Center,
            color = Theme.colors.title,
            style = Theme.textStyle.title.large,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        AppText(text = stringResource(id = pageContent.description),
            textAlign = TextAlign.Center,
            color = Theme.colors.body,
            style = Theme.textStyle.body.medium,)
    }
}