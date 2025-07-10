package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
fun EmptySearchState() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = com.sanaa.designsystem.R.drawable.empty_search),
            contentDescription = "Empty Search",
            modifier = Modifier
                .size(128.dp)


        )
        Text(
            text = "Start exploring! Search for your favorite\n movies, series and shows",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Theme.colors.body,
        )
    }

}

@Preview(showBackground = true, heightDp = 800, widthDp = 360)
@Composable
fun EmptySearchStatePreview() {
    MaterialTheme {
        EmptySearchState()
    }
}
