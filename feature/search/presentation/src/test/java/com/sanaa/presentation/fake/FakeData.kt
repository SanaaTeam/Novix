package com.sanaa.presentation.fake

import search.usecase.search_param.SearchActorOutput

object FakeData {
    val actorOutputs:List<SearchActorOutput> = listOf(
        SearchActorOutput(1, "Tom Hanks", "image.com"),
        SearchActorOutput(2, "Tom Holland", "image.com")
    )
}