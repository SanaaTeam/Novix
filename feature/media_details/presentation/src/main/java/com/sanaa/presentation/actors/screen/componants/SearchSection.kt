package com.sanaa.presentation.screen.actors.screen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.text_field.NovixTextField


@Composable
fun SearchSection(
    text: String,
    onFilterClicked: () -> Unit = {},
    onTextChange: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NovixTextField(
            value = text,
            onValueChange = onTextChange,
            hint = stringResource(R.string.search_hint),
            icon = painterResource(R.drawable.icon_search),
            modifier = Modifier
                .weight(1f)

        )
        PrimaryButton(
            text = null,
            onClick = onFilterClicked,
            icon = painterResource(R.drawable.icon_filter)
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun SearchAppBarPreview() {
    var text by remember { mutableStateOf("") }
    SearchSection(
        text = text,
        onTextChange = { text = it }
    )
}