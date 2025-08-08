package com.sanaa.tvapp.presentation.screens.navigation

import androidx.annotation.DrawableRes
import com.sanaa.designsystem.R

sealed class Screen(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int
) {
    object Home : Screen("home", "Home", R.drawable.icon_home)
    object Search : Screen("search", "Search", R.drawable.icon_search)
    object Categories : Screen("categories", "Categories", R.drawable.icon_category)
    object MyList : Screen("myList", "My List", R.drawable.icon_save)
    object MyAccount : Screen("myAccount", "My Account", R.drawable.icon_account)

}
