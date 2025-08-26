package com.sanaa.tvapp.presentation.screens.myAccount

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.home.component.Title
import com.sanaa.tvapp.presentation.screens.login.LoginActivity
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.NavigateToChangePasswordSetting
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.NavigateToLogin
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.NavigateToMyRating
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.NavigateToWatchingHistory
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.UpdateAppLanguage
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.Companion.ARABIC_LANGUAGE_CODE
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.Companion.ENGLISH_LANGUAGE_CODE
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.ContentRestrictionUiState
import com.sanaa.tvapp.presentation.screens.myAccount.component.LogOutDialog
import com.sanaa.tvapp.presentation.screens.myAccount.component.MyAccountUserInfo
import com.sanaa.tvapp.presentation.screens.myAccount.component.NotLoggedInStateComponent
import com.sanaa.tvapp.presentation.screens.myAccount.component.SettingOptionItem
import com.sanaa.tvapp.presentation.screens.myAccount.component.SettingOptions
import com.sanaa.tvapp.presentation.screens.myAccount.component.SettingSection
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.LocalDrawerFocusRequester
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.ChangePasswordScreenRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.MyRatingScreenRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.WatchingHistoryScreenRoute
import com.sanaa.tvapp.util.modifier.handleDPadKeyEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import repository.Language
import com.sanaa.designsystem.R as designSystemResource
import com.sanaa.tvapp.R as tvResource

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
    val scrollState = rememberScrollState()
    val drawerFocusRequester = LocalDrawerFocusRequester.current
    val layoutDirection = LocalLayoutDirection.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isUserLoggedIn.not()) {
            NotLoggedInStateComponent(
                interactionsListener::onLoginButtonClick,
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(horizontal = 36.dp, vertical = 24.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    MyAccountUserInfo(state.currentUser)


                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(30.dp),
                            painter = painterResource(tvResource.drawable.novix_logo),
                            contentDescription = null,
                        )

                        Text(
                            stringResource(tvResource.string.app_name),
                            color = Theme.colors.title,
                            style = Theme.textStyle.title.medium,
                        )
                    }
                }

                Title(
                    modifier = Modifier,
                    title = stringResource(tvResource.string.settings)
                )

                SettingSection(
                    modifier = Modifier,
                    title = stringResource(designSystemResource.string.language),
                )
                {
                    SettingOptions(
                        settingOptionItems = listOf(
                            SettingOptionItem(
                                title = stringResource(designSystemResource.string.english),
                                tag = Language.ENGLISH,
                                isSelected = state.selectedLanguage == ENGLISH_LANGUAGE_CODE
                            ),
                            SettingOptionItem(
                                title = stringResource(designSystemResource.string.arabic),
                                tag = Language.ARABIC,
                                isSelected = state.selectedLanguage == ARABIC_LANGUAGE_CODE
                            )
                        ),
                        onSelected = { interactionsListener.onSelectLanguage(it.tag.code) }
                    )
                }

                SettingSection(
                    modifier = Modifier,
                    title = stringResource(designSystemResource.string.content_restriction),
                ) {
                    SettingOptions(
                        settingOptionItems = listOf(
                            SettingOptionItem(
                                title = stringResource(designSystemResource.string.strict_restriction),
                                description = stringResource(designSystemResource.string.blurs_all_sensitive_content),
                                tag = ContentRestrictionUiState.RESTRICTED,
                                isSelected = state.selectedContentRestriction == ContentRestrictionUiState.RESTRICTED
                            ),
                            SettingOptionItem(
                                title = stringResource(designSystemResource.string.moderate_restriction),
                                description = stringResource(designSystemResource.string.blurs_explicit_scenes_only),
                                tag = ContentRestrictionUiState.MODERATE_RESTRICTION,
                                isSelected = state.selectedContentRestriction == ContentRestrictionUiState.MODERATE_RESTRICTION
                            ),
                            SettingOptionItem(
                                title = stringResource(designSystemResource.string.unrestricted),
                                description = stringResource(designSystemResource.string.no_content_is_blurred),
                                tag = ContentRestrictionUiState.UNRESTRICTED,
                                isSelected = state.selectedContentRestriction == ContentRestrictionUiState.UNRESTRICTED
                            ),
                        ),
                        onSelected = { interactionsListener.onSelectContentRestriction(it.tag) }
                    )
                }

                Button(
                    modifier = Modifier
                        .handleDPadKeyEvents(
                            onLeft = if (layoutDirection == LayoutDirection.Rtl) {
                                {
                                    // do nothing
                                }
                            } else null,
                            onRight = if (layoutDirection == LayoutDirection.Ltr) {
                                {
                                    // do nothing
                                }
                            } else null
                        ),
                    shape = ButtonDefaults.shape(RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.colors(containerColor = Theme.colors.surfaceHigh),
                    border = ButtonDefaults.border(
                        border = Border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = Theme.colors.stroke
                            ),
                        ),
                        focusedBorder = Border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = Theme.colors.statusColors.redAccent
                            ),
                        )
                    ),
                    onClick = {
                        interactionsListener.onLogoutButtonClick()
                    }
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.icon_logout),
                        contentDescription = "logout button"
                    )

                    AppText(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(tvResource.string.logout),
                        color = Theme.colors.statusColors.redAccent,
                        style = Theme.textStyle.label.medium
                    )
                }
            }
        }
        if (state.showLogoutDialog)
            LogOutDialog(
                onDismissRequest = interactionsListener::onDismissLogoutDialog,
                onLogOutConfirmed = interactionsListener::onConfirmLogoutButtonClick,
            )
    }
}


@Composable
private fun EffectHandler(
    effects: Flow<MyAccountScreenEffect>,
) {
    val navController = LocalAppNavController.current
    val view = LocalView.current
    val context = LocalContext.current
    val activity = view.context as? AppCompatActivity

    val loginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            activity?.recreate()
        }
    }
    LaunchedEffect(Unit) {
        effects.collectLatest {
            when (it) {
                NavigateToChangePasswordSetting -> {
                    navController.navigate(ChangePasswordScreenRoute)
                }

                is UpdateAppLanguage -> {
                    val localeList = LocaleListCompat.forLanguageTags(it.language)
                    val isLanguageSelected = AppCompatDelegate
                        .getApplicationLocales()
                        .toLanguageTags() != localeList.toLanguageTags()
                    if (isLanguageSelected) {
                        AppCompatDelegate.setApplicationLocales(localeList)
                    }
                }

                NavigateToMyRating -> {
                    navController.navigate(MyRatingScreenRoute)
                }

                NavigateToWatchingHistory -> {
                    navController.navigate(WatchingHistoryScreenRoute)
                }

                NavigateToLogin -> {
                    val intent = Intent(context, LoginActivity::class.java)
                    loginLauncher.launch(intent)
                }
            }
        }
    }
}