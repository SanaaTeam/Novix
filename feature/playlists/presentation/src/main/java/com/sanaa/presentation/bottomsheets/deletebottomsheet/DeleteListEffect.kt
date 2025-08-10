package com.sanaa.presentation.bottomsheets.deletebottomsheet

sealed interface DeleteListEffect {
    object DeleteSuccess : DeleteListEffect
    object DeleteFailure : DeleteListEffect
}