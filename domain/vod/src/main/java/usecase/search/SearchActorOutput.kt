package usecase.search

import entity.Actor

data class SearchActorOutput(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val gender: Actor.Gender
)