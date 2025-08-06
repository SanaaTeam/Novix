package com.sanaa.presentation.screen.myAccount

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.selection.Option
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.api.navigation.LocalNavControllerProvider
import com.sanaa.presentation.api.navigation.MyRatingScreenRoute
import com.sanaa.presentation.api.navigation.WatchingHistoryScreenRoute
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToChangePasswordSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToContentRestrictionSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToMyRating
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToWatchingHistory
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.UpdateAppLanguage
import com.sanaa.presentation.screen.myAccount.component.AccountOptionItem
import com.sanaa.presentation.screen.myAccount.component.MyAccountUserInfo
import com.sanaa.presentation.screen.myAccount.component.SelectionBottomSheet
import com.sanaa.presentation.screen.myAccount.component.VerticalList

@Composable
fun MyAccountScreen(viewModel: MyAccountScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)
    val navController = LocalNavControllerProvider.current
    val view = LocalView.current
    val activity = view.context as? AppCompatActivity
    LaunchedEffect(effect) {
        when (val it = effect) {
            NavigateToChangePasswordSetting -> {}
            NavigateToContentRestrictionSetting -> {
            }
            is UpdateAppLanguage -> {
                val localeList = LocaleListCompat.forLanguageTags(it.language)
                if (AppCompatDelegate.getApplicationLocales()
                        .toLanguageTags() != localeList.toLanguageTags()
                ) {
                    AppCompatDelegate.setApplicationLocales(localeList)
                }
                activity?.recreate()
            }

            NavigateToMyRating -> navController.navigate(MyRatingScreenRoute)
            NavigateToWatchingHistory -> navController.navigate(WatchingHistoryScreenRoute)
            is MyAccountScreenEffect.UpdateAppTheme -> {
                if (it.isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

            null -> {}
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
                    painter = painterResource(R.drawable.icon_moon),
                    title = stringResource(R.string.appearance),
                    onClick = { interactionsListener.onClickAppearance() }),
                AccountOptionItem(
                    painter = painterResource(R.drawable.language_circle),
                    title = stringResource(R.string.language),
                    onClick = { interactionsListener.onClickLanguageSetting() }),
            )
        )
        SelectionBottomSheet(
            isVisible = uiState.showChangeLanguageBottomSheet,
            bottomSheetTitle = stringResource(R.string.language),
            options = listOf(
                Option(
                    label = stringResource(R.string.arabic), value = "ar"
                ),
                Option(
                    label = stringResource(R.string.english), value = "en"
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
            bottomSheetTitle = stringResource(R.string.content_restriction),
            options = listOf(
                Option(
                    label = stringResource(R.string.strict_restriction),
                    value = ContentRestrictionUiState.RESTRICTED,
                    description = stringResource(R.string.blurs_all_sensitive_content)
                ),
                Option(
                    label = stringResource(R.string.moderate_restriction),
                    value = ContentRestrictionUiState.MODERATE_RESTRICTION,
                    description = stringResource(R.string.blurs_explicit_scenes_only)
                ),
                Option(
                    label = stringResource(R.string.unrestricted),
                    value = ContentRestrictionUiState.UNRESTRICTED,
                    description = stringResource(R.string.no_content_is_blurred)
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
            bottomSheetTitle = stringResource(R.string.appearance),
            options = listOf(
                Option(
                    label = stringResource(R.string.light), value = ThemeUiState.LIGHT
                ),
                Option(
                    label = stringResource(R.string.dark), value = ThemeUiState.DARK
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

        override fun onClickAppearance() {

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