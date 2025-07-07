package repository

import model.Actor

interface ActorRepository {
    suspend fun searchActors(query: String, language: String): List<Actor>
}
