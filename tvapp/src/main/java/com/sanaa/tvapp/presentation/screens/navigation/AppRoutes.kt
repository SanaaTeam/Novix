
package com.sanaa.tvapp.presentation.screens.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sanaa.designsystem.R
import kotlinx.serialization.Serializable

open class AppRoutes

@Serializable
sealed class TvAppRoute(
    @StringRes val titleRes: Int,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int
) : AppRoutes() {

    @Serializable
    object Home : TvAppRoute(R.string.home, R.drawable.icon_home, R.drawable.icon_home_selected)

    @Serializable
    object Search : TvAppRoute(R.string.search, R.drawable.icon_search, R.drawable.icon_search_selected)

    @Serializable
    object Categories : TvAppRoute(R.string.categories, R.drawable.icon_category, R.drawable.icon_category_selected)

    @Serializable
    object MyList : TvAppRoute(R.string.my_list, R.drawable.icon_save, R.drawable.icon_save_selected)

    @Serializable
    object MyAccount : TvAppRoute(R.string.my_account, R.drawable.icon_account, R.drawable.icon_account_selected)

    @Serializable
    object Login : TvAppRoute(
        R.string.login_title,
        R.drawable.icon_saved,
        R.drawable.icon_saved
    )}