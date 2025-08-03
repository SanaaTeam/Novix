package com.sanaa.presentation.screen.myAccount

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToChangePasswordSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToContentRestrictionSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToLanguageSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToMyRating
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToWatchingHistory
import com.sanaa.presentation.screen.myAccount.component.AccountOptionItem
import com.sanaa.presentation.screen.myAccount.component.MyAccountUserInfo
import com.sanaa.presentation.screen.myAccount.component.VerticalList
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

        MyAccountUserInfo(uiState.username)

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

@PreviewLightDark
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