package com.sanaa.presentation.screen.savedDetails

sealed interface SavedDetailsScreenEffect {
    object NavigateBack : SavedDetailsScreenEffect
    data object ShowErrorSnackBar : SavedDetailsScreenEffect
    data object ShowSuccessSnackBar : SavedDetailsScreenEffect
}