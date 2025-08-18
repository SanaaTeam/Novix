package com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet

sealed interface SaveToListEffect {
    object DismissBottomSheet : SaveToListEffect
    object CreateNewList : SaveToListEffect
}