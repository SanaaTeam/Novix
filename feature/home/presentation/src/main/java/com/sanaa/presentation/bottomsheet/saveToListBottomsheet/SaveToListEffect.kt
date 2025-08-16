package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

sealed interface SaveToListEffect {
    object AddedSuccessfully : SaveToListEffect
    object FailedToAdd : SaveToListEffect
}