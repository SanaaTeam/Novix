package com.sanaa.actors.repository

import com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
import com.sanaa.actors.mapper.fullImageUrlOrEmpty
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

    override suspend fun getActorDetails(actorId: Int): Actor {
        return try {
            remoteDataSource.getActorDetails(actorId).toDomain()
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve actor details for ID: $actorId")
        }
    }

    override suspend fun getProfileImages(actorId: Int): List<String> {
        return try {
            remoteDataSource.getActorImages(actorId).profiles.take(3)
                .map { it.path.fullImageUrlOrEmpty() }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve profile images for actor ID: $actorId")
        }
    }

    override suspend fun getGalleryImages(actorId: Int): List<String> {
        return try {
            remoteDataSource.getActorImages(actorId).profiles.drop(1)
                .map { it.path.fullImageUrlOrEmpty() }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve gallery images for actor ID: $actorId")
        }
    }

    override suspend fun getActorTopMovies(actorId: Int): List<Movie> {
        return try {
            remoteDataSource.getActorTopMovies(actorId).cast.sortedByDescending { it.voteAverage ?: 0.0 }
                .take(20).map { it.toDomain() }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve top movies for actor ID: $actorId")
        }
    }

    override suspend fun getActorTopTvSeries(actorId: Int): List<TvSeries> {
        return try {
            remoteDataSource.getActorTopTvSeries(actorId).cast.sortedByDescending {
                    it.voteAverage ?: 0.0
                }.take(20).map { it.toDomain() }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve top TV series for actor ID: $actorId")
        }
    }
}
