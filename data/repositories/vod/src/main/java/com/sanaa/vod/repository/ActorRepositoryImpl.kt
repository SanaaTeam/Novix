package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.remote.RemoteActorDataSource
import com.sanaa.vod.repository.mapper.media.toEntity
import com.sanaa.vod.repository.mapper.media.toMovie
import com.sanaa.vod.repository.mapper.media.toTvShow
import com.sanaa.vod.util.safeCall
import entity.Actor
import entity.Movie
import entity.TvShow
import repository.ActorRepository
import javax.inject.Inject


class ActorRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteActorDataSource,
) : ActorRepository {

    override suspend fun getActorDetails(id: Int): Actor =
        safeCall("Failed to retrieve actor details for ID: $id") {
            remoteDataSource.getActorDetails(id).toEntity()
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

    override suspend fun getActorTopTvShows(id: Int): List<TvShow> =
        safeCall("Failed to retrieve top TV show for actor ID: $id") {
            remoteDataSource.getActorTvShows(id).map {
                it.toTvShow()
            }.take(20)
        }

    override suspend fun getTrendingActors(page: Int): List<Actor> {
        safeCall("Failed to fetch Trending People ") {
            return remoteDataSource.fetchTrendingPeople(page).map {
                it.toEntity()
            }
        }
    }
}