package com.sanaa.presentation.bottomsheets.deletebottomsheet

sealed interface DeleteListEffect {
    object Dismiss : DeleteListEffect
}