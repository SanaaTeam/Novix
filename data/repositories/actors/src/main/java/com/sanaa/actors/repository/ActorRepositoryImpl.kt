package com.sanaa.actors.repository

import com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
import com.sanaa.actors.mapper.getFullImageUrl
import com.sanaa.actors.mapper.toDomain
import details.repository.ActorRepository
import entity.Actor
import entity.Movie
import entity.TvSeries
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import java.net.UnknownHostException

class ActorRepositoryImpl(
    private val remoteDataSource: ActorRemoteDataSource,
) : ActorRepository {

    override suspend fun getActorDetails(id: Int): Actor =
        safeCall("Failed to retrieve actor details for ID: $id") {
            remoteDataSource.getActorDetails(id).toDomain()
        }

    override suspend fun getProfileImages(id: Int, count: Int): List<String> =
        safeCall("Failed to retrieve profile images for actor ID: $id") {
            remoteDataSource.getActorImages(id).profiles
                .take(count)
                .map { getFullImageUrl(it.path) }
        }

    override suspend fun getGalleryImages(id: Int): List<String> =
        safeCall("Failed to retrieve gallery images for actor ID: $id") {
            remoteDataSource.getActorImages(id).profiles
                .drop(1)
                .map { getFullImageUrl(it.path) }
        }


    override suspend fun getActorTopMovies(id: Int): List<Movie> =
        safeCall("Failed to retrieve top movies for actor ID: $id") {
            remoteDataSource.getActorTopMovies(id)
                .cast.orEmpty()
                .sortedByDescending { it.voteAverage ?: 0.0 }
                .take(20)
                .map { it.toDomain() }
        }


    override suspend fun getActorTopTvSeries(id: Int): List<TvSeries> =
        safeCall("Failed to retrieve top TV series for actor ID: $id") {
            remoteDataSource.getActorTopTvSeries(id)
                .cast
                .sortedByDescending { it.voteAverage ?: 0.0 }
                .take(20)
                .map { it.toDomain() }
        }

    private inline fun <T> safeCall(errorMessage: String, block: () -> T): T {
        try {
            return block()
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("$errorMessage: ${e.message}")
        }
    }
}
