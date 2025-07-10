package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
fun NoSearchResultState() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(128.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = com.sanaa.designsystem.R.drawable.empty_search),
                contentDescription = "Empty Search",
                modifier = Modifier
                    .size(128.dp)
            )
            Image(
                painter = painterResource(id = com.sanaa.designsystem.R.drawable.exclamation),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopStart)
                    .offset(x = (18).dp, y =14.dp)
            )
        }

        Text(
            text = "No search result, please try with another \nkeyword!",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Theme.colors.body,
        )
    }
}

@Preview(showBackground = true, heightDp = 800, widthDp = 360)
@Composable
fun NoSearchResultStatePreview() {
    MaterialTheme {
        NoSearchResultState()
    }
}
