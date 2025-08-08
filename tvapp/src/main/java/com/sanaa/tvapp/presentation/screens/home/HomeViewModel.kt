package com.sanaa.tvapp.presentation.screens.home

import com.sanaa.tvapp.base.BaseViewModel

class HomeViewModel : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(
    HomeScreenUiState()
) {
    init {
        updateState { copy(isLoading = true) }
    }

}