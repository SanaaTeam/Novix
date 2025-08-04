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
import com.sanaa.designsystem.design_system.component.selection.Option
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToChangePasswordSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToContentRestrictionSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToLanguageSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToMyRating
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToWatchingHistory
import com.sanaa.presentation.screen.myAccount.component.AccountOptionItem
import com.sanaa.presentation.screen.myAccount.component.MyAccountUserInfo
import com.sanaa.presentation.screen.myAccount.component.SelectionBottomSheet
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
                    onClick = { interactionsListener.onClickWatchingHistory() }),
                AccountOptionItem(
                    painter = painterResource(R.drawable.star_square),
                    title = stringResource(R.string.my_rating),
                    onClick = { interactionsListener.onClickMyTopRating() }),
                AccountOptionItem(
                    painter = painterResource(R.drawable.shield_energy),
                    title = stringResource(R.string.content_restriction),
                    onClick = { interactionsListener.onClickContentRestriction() }),
                AccountOptionItem(
                    painter = painterResource(R.drawable.lock_key),
                    title = stringResource(R.string.change_password),
                    onClick = { interactionsListener.onClickChangePassword() }),
                AccountOptionItem(
                    painter = painterResource(R.drawable.language_circle),
                    title = stringResource(R.string.language),
                    onClick = { interactionsListener.onClickLanguageSetting() }),
            )
        )
        SelectionBottomSheet(
            isVisible = uiState.showChangeLanguageBottomSheet,
            bottomSheetTitle = "Language",
            options = listOf(
                Option(
                    label = "Arabic", value = "ar"
                ),
                Option(
                    label = "English", value = "en"
                ),
            ),
            onDismiss = {
                interactionsListener.onDismissBottomSheet()
            },
            onOptionSelected = {
                interactionsListener.onSelectLanguage(
                    it.toString()
                )
            },
            selectedValue = uiState.selectedLanguage,
            onSaveClick = {
                interactionsListener.onSaveLanguageClick()
            }
        )

        SelectionBottomSheet(
            isVisible = uiState.showContentRestrictionBottomSheet,
            bottomSheetTitle = "Content Restriction",
            options = listOf(
                Option(
                    label = "Restricted", value = ContentRestrictionUiState.RESTRICTED
                ),
                Option(
                    label = "Moderate Restriction",
                    value = ContentRestrictionUiState.MODERATE_RESTRICTION
                ),
                Option(
                    label = "Unrestricted", value = ContentRestrictionUiState.UNRESTRICTED
                )
            ),
            onDismiss = {
                interactionsListener.onDismissBottomSheet()
            },
            onOptionSelected = {
                interactionsListener.onSelectContentRestriction(it)
            },
            selectedValue = uiState.selectedContentRestriction,
            onSaveClick = { interactionsListener.onSaveContentRestrictionClick() }
        )

        SelectionBottomSheet(
            isVisible = uiState.showChangeThemeBottomSheet,
            bottomSheetTitle = "Theme",
            options = listOf(
                Option(
                    label = "Light", value = ThemeUiState.LIGHT
                ),
                Option(
                    label = "Dark", value = ThemeUiState.DARK
                ),
            ),
            onDismiss = {
                interactionsListener.onDismissBottomSheet()
            },
            onOptionSelected = {
                interactionsListener.onSelectTheme(it)
            },
            selectedValue = uiState.selectedTheme,
            onSaveClick = {
                interactionsListener.onSaveThemeClick()
            },
        )
    }
}


@PreviewLightDark
@Composable
private fun AccountScreenContentPreview() {
    val interactionsListener = object : MyAccountScreenInteractionsListener {
        override fun onClickChangePassword() {
        }

        override fun onClickContentRestriction() {
        }

        override fun onClickLanguageSetting() {
        }

        override fun onClickMyTopRating() {
        }

        override fun onClickWatchingHistory() {
        }

        override fun onSelectLanguage(language: String) {

        }

        override fun onDismissBottomSheet() {

        }

        override fun onSaveLanguageClick() {
        }

        override fun onSelectContentRestriction(contentRestriction: ContentRestrictionUiState?) {

        }

        override fun onSelectTheme(theme: ThemeUiState?) {
        }

        override fun onSaveThemeClick() {
        }

        override fun onSaveContentRestrictionClick() {
        }

    }
    NovixTheme(isSystemInDarkTheme()) {
        NovixScaffold {
            MyAccountScreenContent(
                uiState = MyAccountScreenUiState(
                    username = "User Name",
                ), interactionsListener = interactionsListener
            )
        }
    }
}