package com.sanaa.presentation.bottomsheets.removeFromListBottomSheet

sealed interface RemoveFromListEffect {
    data class DismissBottomSheet(val deselectedListIds: List<Int>):RemoveFromListEffect
}