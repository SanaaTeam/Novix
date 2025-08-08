package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

sealed interface SavetoListEffect {
    object AddedSuccessfully : SavetoListEffect
    object FailedToAdd : SavetoListEffect
}