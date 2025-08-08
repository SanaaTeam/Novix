package com.sanaa.presentation.screen.bottomsheet.saveToListBottomsheet

sealed interface SaveToListEffect {
    object AddedSuccessfully : SaveToListEffect
    object FailedToAdd : SaveToListEffect
}