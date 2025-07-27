package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.remote.actor.RemoteActorDataSource
import com.sanaa.vod.mapper.actor.toDomain
import com.sanaa.vod.mapper.actor.toMovie
import com.sanaa.vod.mapper.actor.toTvSeries
import com.sanaa.vod.mapper.media.toEntity
import entity.Actor
import entity.Movie
import entity.TvSeries
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import repository.ActorRepository
import java.net.UnknownHostException


class ActorRepositoryImpl(
    private val remoteDataSource: RemoteActorDataSource,
) : ActorRepository {

    override suspend fun getActorDetails(id: Int): Actor =
        safeCall("Failed to retrieve actor details for ID: $id") {
            remoteDataSource.getActorDetails(id).toDomain()
        }

    override suspend fun getProfileImageUrls(id: Int, count: Int): List<String> =
        safeCall("Failed to retrieve profile images for actor ID: $id") {
            remoteDataSource.getActorImages(id).map {
                it.toEntity()
            }.take(count)
        }

    override suspend fun getGalleryImageUrls(id: Int): List<String> =
        safeCall("Failed to retrieve gallery images for actor ID: $id") {
            remoteDataSource.getActorImages(id).map {
                it.toEntity()
            }
        }


    override suspend fun getActorTopMovies(id: Int): List<Movie> =
        safeCall("Failed to retrieve top movies for actor ID: $id") {
            remoteDataSource.getActorMovies(id).map {
                it.toMovie()
            }.take(20)
        }

    override suspend fun getActorTopTvSeries(id: Int): List<TvSeries> {
        return emptyList()
    }


    override suspend fun getActorTopTvShows(id: Int): List<TvSeries> =
        safeCall("Failed to retrieve top TV series for actor ID: $id") {
            remoteDataSource.getActorTvShows(id).map {
                it.toTvSeries()
            }.take(20)
        }


    override suspend fun getTrendingActors(page: Int): List<Actor> {
        safeCall("Failed to fetch Trending People ") {
            return remoteDataSource.fetchTrendingPeople(page).map {
                it.toDomain()
            }
        }
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
