package repository

import model.Actor
import model.Language

interface ActorRepository {
    suspend fun searchActors(query: String, language: Language): List<Actor>
}
