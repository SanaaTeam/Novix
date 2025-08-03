package com.sanaa.presentation.screen.saved

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.FabButton
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.playlists.presentation.R

@Composable
fun EmptySavedListScreenContent() {
    NovixScaffold(
        modifier = Modifier.padding(top = 12.dp),
        topBar = { TopBar(screenTitle = stringResource(R.string.saved_list)) },
        floatingActionButton = {
            FabButton(
                icon = painterResource(id = com.sanaa.designsystem.R.drawable.icon_plus),
                onClick = {},
                isLoading = false,
                isEnabled = true
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.my_list_light),
                contentDescription = stringResource(R.string.my_list_is_empty)
            )
            AppText(
                modifier = Modifier.padding(horizontal = 68.dp)
                    .offset(y = (-30.dp))
                ,
                text = stringResource(R.string.there_is_no_saved_list_yet_click_on_button_to_add_a_new_list),
                style = Theme.textStyle.body.small,
                color = Theme.colors.body,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Preview
@Composable
private fun EmptySavedListScreenContentPrev() {
    EmptySavedListScreenContent()
}