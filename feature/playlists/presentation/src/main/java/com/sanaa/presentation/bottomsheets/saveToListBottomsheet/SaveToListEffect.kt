package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

sealed interface SaveToListEffect {
    object AddedSuccessfully : SaveToListEffect
    object FailedToAdd : SaveToListEffect
}