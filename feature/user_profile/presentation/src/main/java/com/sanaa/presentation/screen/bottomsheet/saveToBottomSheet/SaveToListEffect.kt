package com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet

sealed interface SaveToListEffect {
    object AddedSuccessfully : SaveToListEffect
    object FailedToAdd : SaveToListEffect
}