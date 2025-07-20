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

    override suspend fun getActorDetails(id: Int): Actor {
        return try {
            remoteDataSource.getActorDetails(id).toDomain()
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve actor details for ID: $id")
        }
    }

    override suspend fun getProfileImages(id: Int, count: Int): List<String> {
        return try {
            remoteDataSource.getActorImages(id).profiles.take(count)
                .map { getFullImageUrl(it.path) }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve profile images for actor ID: $id")
        }
    }

    override suspend fun getGalleryImages(id: Int): List<String> {
        return try {
            remoteDataSource.getActorImages(id).profiles.drop(1)
                .map { getFullImageUrl(it.path) }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve gallery images for actor ID: $id")
        }
    }

    override suspend fun getActorTopMovies(id: Int): List<Movie> = try {
        remoteDataSource
            .getActorTopMovies(id)
            .cast.orEmpty()
            .sortedByDescending { it.voteAverage ?: 0.0 }
            .take(20)
            .map { it.toDomain() }
    } catch (_: UnknownHostException) {
        throw NoNetworkException()
    } catch (e: Exception) {
        throw RetrievingDataFailureException("Failed to retrieve top movies for actor ID: $id")
    }


    override suspend fun getActorTopTvSeries(id: Int): List<TvSeries> {
        return try {
            remoteDataSource.getActorTopTvSeries(id).cast.sortedByDescending {
                it.voteAverage ?: 0.0
            }.take(20).map { it.toDomain() }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve top TV series for actor ID: $id")
        }
    }
}
