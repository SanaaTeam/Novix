package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

sealed interface SaveToListEffects {
    object AddedSuccessfully : SaveToListEffects
    object FailedToAdd : SaveToListEffects
}