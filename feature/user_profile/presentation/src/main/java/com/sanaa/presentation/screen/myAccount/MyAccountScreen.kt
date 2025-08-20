package com.sanaa.presentation.screen.myAccount

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.AuthStartRoute
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.selection.Option
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.presentation.navigation.ChangePasswordScreenRoute
import com.sanaa.presentation.navigation.MyRatingScreenRoute
import com.sanaa.presentation.navigation.ProfileApiEntryPoint
import com.sanaa.presentation.navigation.WatchingHistoryScreenRoute
import com.sanaa.presentation.profileProvider.LocalNavControllerProvider
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToChangePasswordSetting
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToMyRating
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.NavigateToWatchingHistory
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect.UpdateAppLanguage
import com.sanaa.presentation.screen.myAccount.MyAccountScreenUiState.Companion.ARABIC_LANGUAGE_CODE
import com.sanaa.presentation.screen.myAccount.MyAccountScreenUiState.Companion.ENGLISH_LANGUAGE_CODE
import com.sanaa.presentation.screen.myAccount.MyAccountScreenUiState.ContentRestrictionUiState
import com.sanaa.presentation.screen.myAccount.MyAccountScreenUiState.ThemeUiState
import com.sanaa.presentation.screen.myAccount.component.MyAccountUserInfo
import com.sanaa.presentation.screen.myAccount.component.NotLoggedInStateComponent
import com.sanaa.presentation.screen.myAccount.component.SelectionBottomSheet
import com.sanaa.presentation.screen.myAccount.component.UserOptions
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyAccountScreen(viewModel: MyAccountScreenViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    EffectHandler(viewModel.effect)
    MyAccountScreenContent(state, viewModel)
}

@Composable
private fun MyAccountScreenContent(
    state: MyAccountScreenUiState,
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
        if (!state.isUserLoggedIn) {
            NotLoggedInStateComponent(
                onLoginClick = { interactionsListener.onLoginButtonClick() }
            )
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                MyAccountUserInfo(
                    state.currentUser,
                    onLogoutClick = {
                        interactionsListener.onLogoutButtonClick()
                    }
                )
                UserOptions(
                    interactionsListener,
                    state.savedLanguage
                )
                SelectionBottomSheet(
                    isVisible = state.showChangeLanguageBottomSheet,
                    bottomSheetTitle = stringResource(R.string.language),
                    options = listOf(
                        Option(
                            label = stringResource(R.string.arabic), value = ARABIC_LANGUAGE_CODE
                        ),
                        Option(
                            label = stringResource(R.string.english), value = ENGLISH_LANGUAGE_CODE
                        ),
                    ),
                    onDismiss = {
                        interactionsListener.onDismissBottomSheet()
                    },
                    onOptionSelected = {
                        interactionsListener.onSelectLanguage(it.orEmpty())
                    },
                    selectedValue = state.selectedLanguage,
                    onSaveClick = {
                        interactionsListener.onSaveLanguageClick()
                    }
                )

                SelectionBottomSheet(
                    isVisible = state.showContentRestrictionBottomSheet,
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
                    selectedValue = state.selectedContentRestriction,
                    onSaveClick = { interactionsListener.onSaveContentRestrictionClick() }
                )

                SelectionBottomSheet(
                    isVisible = state.showChangeThemeBottomSheet,
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
                    selectedValue = state.selectedTheme,
                    onSaveClick = {
                        interactionsListener.onSaveThemeClick()
                    },
                )
            }
        }
    }
}

@Composable
private fun EffectHandler(
    effects: Flow<MyAccountScreenEffect>,
) {

    val navController = LocalNavControllerProvider.current
    val view = LocalView.current
    val activity = view.context as? AppCompatActivity
    val context = LocalContext.current

    val authApi = EntryPointAccessors.fromApplication(
        context,
        ProfileApiEntryPoint::class.java
    ).authenticationApi()

    LaunchedEffect(Unit) {
        effects.collectLatest {
            when (it) {
                NavigateToChangePasswordSetting -> {
                    navController.navigate(ChangePasswordScreenRoute)
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
                    authApi.launch(context, AuthStartRoute.Login)
                }

                MyAccountScreenEffect.PopBackStackToWelcomeScreen -> {
                    activity?.recreate()
                }
            }
        }
    }
}