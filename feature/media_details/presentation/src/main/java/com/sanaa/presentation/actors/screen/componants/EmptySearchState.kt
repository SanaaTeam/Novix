package com.sanaa.presentation.screen.actors.screen.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
fun EmptySearchState(
    modifier: Modifier = Modifier,
    icon : Painter,
    text : String
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(128.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = icon,
                contentDescription = "Empty Search",
                modifier = Modifier
                    .size(128.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Theme.colors.body,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResultStatePreview() {
   NovixTheme(false) {
        EmptySearchState(
            icon = painterResource(id = R.drawable.search_icons),
            text = stringResource(id = R.string.no_search_result_message)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptySearchStatePreview() {
    NovixTheme(false) {
        EmptySearchState(
            icon = painterResource(id = R.drawable.empty_search),
            text = stringResource(id = R.string.empty_search_message)
        )
    }
}
