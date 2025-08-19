package com.sanaa.tvapp.presentation.screens.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sanaa.designsystem.R
import kotlinx.serialization.Serializable

open class AppRoutes

@Serializable
sealed class NavBarRoute(
    @StringRes val titleRes: Int,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int
) : AppRoutes() {
    @Serializable
    object Home : NavBarRoute(R.string.home, R.drawable.icon_home, R.drawable.icon_home_selected)

    @Serializable
    object Search :
        NavBarRoute(R.string.search, R.drawable.icon_search, R.drawable.icon_search_selected)

    @Serializable
    object Categories :
        NavBarRoute(R.string.categories, R.drawable.icon_category, R.drawable.icon_category_selected)

    @Serializable
    object MyAccount :
        NavBarRoute(R.string.my_account, R.drawable.icon_account, R.drawable.icon_account_selected)

}

