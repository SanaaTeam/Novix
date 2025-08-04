package com.sanaa.presentation.screen.savedDetails.screen

sealed interface SavedDetailsScreenEffect {
    object NavigateBack : SavedDetailsScreenEffect
    data object ShowErrorSnackBar : SavedDetailsScreenEffect
    data object ShowSuccessSnackBar : SavedDetailsScreenEffect
}