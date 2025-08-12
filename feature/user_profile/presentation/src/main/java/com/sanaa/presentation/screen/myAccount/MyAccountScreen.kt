package com.sanaa.presentation.screen.myAccount

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.selection.Option
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.api.navigation.ChangePasswordScreenRoute
import com.sanaa.presentation.api.navigation.LocalNavControllerProvider
import com.sanaa.presentation.api.navigation.MyRatingScreenRoute
import com.sanaa.presentation.api.navigation.ProfileApiEntryPoint
import com.sanaa.presentation.api.navigation.WatchingHistoryScreenRoute
import com.sanaa.presentation.provider.LocalThemeMode
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToChangePasswordSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToContentRestrictionSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToMyRating
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToWatchingHistory
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.UpdateAppLanguage
import com.sanaa.presentation.screen.myAccount.component.AccountOptionItem
import com.sanaa.presentation.screen.myAccount.component.MyAccountUserInfo
import com.sanaa.presentation.screen.myAccount.component.NotLoggedInStateComponent
import com.sanaa.presentation.screen.myAccount.component.SelectionBottomSheet
import com.sanaa.presentation.screen.myAccount.component.VerticalList
import dagger.hilt.android.EntryPointAccessors

@Composable
fun MyAccountScreen(viewModel: MyAccountScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)
    val navController = LocalNavControllerProvider.current
    val view = LocalView.current
    val activity = view.context as? AppCompatActivity
    val context = LocalContext.current


    val authApi = EntryPointAccessors.fromApplication(
        context,
        ProfileApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult(
        loggedInWithSessionId = {
            activity?.recreate()
        },
        loggedInAsGuest = {
            activity?.recreate()
        }
    )


    LaunchedEffect(effect) {
        when (val it = effect) {
            NavigateToChangePasswordSetting -> {
                navController.navigate(ChangePasswordScreenRoute)
            }

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

            MyAccountScreenEffect.NavigateToLogin -> {
                launcher.launch(authApi.getLaunchIntent(context))
            }

            MyAccountScreenEffect.PopBackStackToWelcomeScreen -> {
                activity?.recreate()
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
    NovixScaffold(
        topBar = {
            TopBar(
                modifier = Modifier.statusBarsPadding(),
                screenTitle = stringResource(R.string.my_account)
            )
        }
    ) {

        if (!uiState.isUserLoggedIn) {
            NotLoggedInStateComponent(
                onLoginClick = { interactionsListener.onLoginButtonClick() }
            )
        } else {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                MyAccountUserInfo(
                    uiState.currentUser,
                    onLogoutClick = {
                        interactionsListener.onLogoutButtonClick()
                    }
                )

                VerticalList(
                    items = listOf(
                        AccountOptionItem(
                            painter = painterResource(R.drawable.icon_clock),
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
                            painter = painterResource(R.drawable.icon_lock),
                            title = stringResource(R.string.change_password),
                            onClick = { interactionsListener.onClickChangePassword() }),
                        AccountOptionItem(
                            painter = painterResource(R.drawable.icon_moon),
                            title = stringResource(R.string.appearance),
                            onClick = { interactionsListener.onClickAppearance() },
                            description = if (LocalThemeMode.current) stringResource(
                                R.string.dark
                            ) else stringResource(
                                R.string.light
                            )
                        ),
                        AccountOptionItem(
                            painter = painterResource(R.drawable.language_circle),
                            title = stringResource(R.string.language),
                            onClick = { interactionsListener.onClickLanguageSetting() },
                            description = if (uiState.savedLanguage == "ar") stringResource(
                                R.string.ar
                            ) else stringResource(
                                R.string.eng
                            )
                        ),
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

        override fun onLoginButtonClick() {
        }

        override fun onLogoutButtonClick() {

        }

    }
    NovixTheme(isSystemInDarkTheme()) {
        NovixScaffold {
            MyAccountScreenContent(
                uiState = MyAccountScreenUiState(
                    currentUser = UserUiState(
                        username = "mostafa name",
                        imageUrl = " "
                    ),
                    showChangeLanguageBottomSheet = false,
                    showContentRestrictionBottomSheet = false,
                    showChangeThemeBottomSheet = false,
                    selectedLanguage = "en",
                    selectedContentRestriction = ContentRestrictionUiState.UNRESTRICTED,
                    selectedTheme = ThemeUiState.DARK,
                    isLoading = false,
                    isUserLoggedIn = true
                ), interactionsListener = interactionsListener
            )
        }
    }
}