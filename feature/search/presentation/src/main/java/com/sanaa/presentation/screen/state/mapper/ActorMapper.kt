package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.screen.state.ActorUiModel
import entity.Actor

fun Actor.toUiState(): ActorUiModel {
    return ActorUiModel(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl
    )
}