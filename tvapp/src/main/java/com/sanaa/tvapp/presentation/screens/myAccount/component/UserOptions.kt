package com.sanaa.tvapp.presentation.screens.myAccount.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sanaa.designsystem.R
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenInteractionsListener
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.Companion.ARABIC_LANGUAGE_CODE

@Composable
fun UserOptions(
    interactionsListener: MyAccountScreenInteractionsListener,
    savedLanguage: String?,
) {
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
                description = stringResource(R.string.dark)
            ),
            AccountOptionItem(
                painter = painterResource(R.drawable.language_circle),
                title = stringResource(R.string.language),
                onClick = { interactionsListener.onClickLanguageSetting() },
                description = if (savedLanguage == ARABIC_LANGUAGE_CODE) stringResource(
                    R.string.ar
                ) else stringResource(
                    R.string.eng
                )
            ),
        )
    )
}