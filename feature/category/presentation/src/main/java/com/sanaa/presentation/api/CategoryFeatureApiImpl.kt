package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import com.sanaa.api.CategoryFeatureApi
import com.sanaa.presentation.screen.CategoriesScreen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryFeatureApiImpl @Inject constructor() : CategoryFeatureApi {
    @Composable
    override fun CategoryScreenApi() {
        CategoriesScreen()
    }
}