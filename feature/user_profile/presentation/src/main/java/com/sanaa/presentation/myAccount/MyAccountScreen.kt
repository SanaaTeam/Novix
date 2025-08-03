package com.sanaa.presentation.myAccount

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.myAccount.MyAccountScreenEffect.NavigateToChangePasswordSetting
import com.sanaa.presentation.myAccount.MyAccountScreenEffect.NavigateToContentRestrictionSetting
import com.sanaa.presentation.myAccount.MyAccountScreenEffect.NavigateToLanguageSetting
import com.sanaa.presentation.myAccount.MyAccountScreenEffect.NavigateToMyRating
import com.sanaa.presentation.myAccount.MyAccountScreenEffect.NavigateToWatchingHistory
import com.sanaa.presentation.util.Listen

@Composable
fun MyAccountScreen(viewModel: MyAccountScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen {
        when (it) {
            NavigateToChangePasswordSetting -> TODO("Add Navigation Code")
            NavigateToContentRestrictionSetting -> TODO("Add Navigation Code")
            NavigateToLanguageSetting -> TODO("Add Navigation Code")
            NavigateToMyRating -> TODO("Add Navigation Code")
            NavigateToWatchingHistory -> TODO("Add Navigation Code")
        }
    }

    MyAccountScreenContent(uiState, viewModel)
}

@Composable
fun MyAccountScreenContent(
    uiState: MyAccountScreenUiState,
    interactionsListener: MyAccountScreenInteractionsListener,
) {
    Column {
        TopBar(
            modifier = Modifier.statusBarsPadding(),
            screenTitle = stringResource(R.string.my_account)
        )

        UserInfo(uiState.username)

        VerticalList(
            items = listOf(
                AccountOptionItem(
                    painter = painterResource(R.drawable.time_schedule),
                    title = stringResource(R.string.watching_history),
                    onClick = { interactionsListener.navigateToWatchingHistory() }
                ),
                AccountOptionItem(
                    painter = painterResource(R.drawable.star_square),
                    title = stringResource(R.string.my_rating),
                    onClick = { interactionsListener.navigateToMyRating() }
                ),
                AccountOptionItem(
                    painter = painterResource(R.drawable.shield_energy),
                    title = stringResource(R.string.content_restriction),
                    onClick = { interactionsListener.navigateContentRestrictionSetting() }
                ),
                AccountOptionItem(
                    painter = painterResource(R.drawable.lock_key),
                    title = stringResource(R.string.change_password),
                    onClick = { interactionsListener.navigateChangePasswordSetting() }
                ),
                AccountOptionItem(
                    painter = painterResource(R.drawable.language_circle),
                    title = stringResource(R.string.language),
                    onClick = { interactionsListener.navigateLanguageSetting() }
                ),
            )
        )
    }
}

@Composable
fun Divider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(Theme.colors.stroke)
    )
}

@Composable
fun VerticalList(items: List<AccountOptionItem>) {
    val verticalScroll = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .verticalScroll(verticalScroll)
    ) {
        items.forEachIndexed { index, item ->
            AccountOption(
                painter = item.painter,
                title = item.title,
                onClick = item.onClick
            )

            if (index != items.lastIndex)
                Divider()
        }
    }
}

data class AccountOptionItem(
    val painter: Painter,
    val title: String,
    val onClick: () -> Unit,
)

@Composable
fun AccountOption(painter: Painter, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Theme.colors.iconBackgroundLow)
                .border(
                    1.dp,
                    Theme.colors.stroke,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painter,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Theme.colors.primary)
            )
        }

        Text(
            modifier = Modifier,
            text = title,
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium
        )
    }
}

@Composable
private fun UserInfo(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Theme.colors.iconBackgroundLow)
                .border(
                    1.dp,
                    Theme.colors.stroke,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.user_profile),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Theme.colors.hint)
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = userName,
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium
        )

        Image(
            modifier = Modifier
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable {

                },
            painter = painterResource(id = R.drawable.more_vertical),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Theme.colors.body)
        )
    }
}

@Preview
@Composable
private fun AccountScreenContentPreview() {
    val interactionsListener = object : MyAccountScreenInteractionsListener {
        override fun navigateChangePasswordSetting() {
        }

        override fun navigateContentRestrictionSetting() {
        }

        override fun navigateLanguageSetting() {
        }

        override fun navigateToMyRating() {
        }

        override fun navigateToWatchingHistory() {
        }
    }
    NovixTheme(isSystemInDarkTheme()) {
        NovixScaffold {
            MyAccountScreenContent(
                uiState = MyAccountScreenUiState(
                    username = "User Name",
                ),
                interactionsListener = interactionsListener
            )
        }
    }
}