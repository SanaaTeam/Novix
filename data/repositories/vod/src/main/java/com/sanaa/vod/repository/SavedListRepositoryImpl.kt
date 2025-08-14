package com.sanaa.vod.repository
import android.util.Log
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.local.cache.LocalSavedMovieDataSource
import com.sanaa.vod.dataSource.remote.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.repository.mapper.custom_list.toEntity
import com.sanaa.vod.repository.mapper.media.toEntity
import com.sanaa.vod.util.safeCall
import entity.Movie
import entity.SavedMovie
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject
class SavedListRepositoryImpl @Inject constructor(
    private val remoteSavedListDataSource: RemoteSavedListDataSource,
    private val savedMovieLocalDataSource: LocalSavedMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val preferencesManager: PreferencesManager,
) : SavedListRepository {

    override suspend fun getSavedLists(): List<SavedList> =
        safeCall("Failed to fetch saved lists") {
            val localMovies = savedMovieLocalDataSource.getAllSavedMovies()
            val remoteLists = remoteSavedListDataSource
                .fetchUserLists(obtainSessionId())
                .map { it.toEntity() }
            remoteLists.map { list ->
                val savedCount = localMovies.count { it.listId.toInt() == list.id }
                list.copy(itemCount = savedCount)
            }
        }
    override suspend fun createSavedList(title: String): SavedList =
        safeCall("Failed to create list") {
            remoteSavedListDataSource
                .createList(obtainSessionId(), title)
                .toEntity()
        }
    override suspend fun deleteSavedList(listId: Int) {
        safeCall("Failed to delete list") {
            remoteSavedListDataSource.deleteList(obtainSessionId(), listId)
            savedMovieLocalDataSource.deleteAllByListId(listId.toLong())
        }
    }
    override suspend fun getAllMoviesInList(listId: Int, page: Int): List<Movie> =
        safeCall("Failed to fetch list items") {
            val listItems = remoteSavedListDataSource.fetchListItems(listId, page)
            coroutineScope {
                listItems.map { listItem ->
                    async {
                        val movie = remoteMovieDataSource.fetchMovieDetails(listItem.id).toEntity()
                        val isSaved =
                            savedMovieLocalDataSource.isMovieSaved(
                                movie.id.toLong(),
                                listId.toLong()
                            ).first()
                        movie.copy(isSaved = isSaved)
                    }
                }.awaitAll()
            }
        }
    override suspend fun addMovieToList(listId: Int, movieId: Int): Boolean =
        safeCall("Failed to add movie to list") {
            val success = remoteSavedListDataSource.addItem(obtainSessionId(), listId, movieId)
            if (success) {
                savedMovieLocalDataSource.upsertMovie(
                    SavedMovie(
                        movieId = movieId.toLong(),
                        listId = listId.toLong()
                    )
                )
            }
            success
        }
    override suspend fun removeMovieFromList(listId: Int, movieId: Int): Boolean =
        safeCall("Failed to remove movie from list") {
            val success = remoteSavedListDataSource.removeItem(obtainSessionId(), listId, movieId)
            if (success) {
                savedMovieLocalDataSource.deleteMovieFromList(movieId.toLong(), listId.toLong())
            }
            success
        }
    private suspend fun obtainSessionId(): String = preferencesManager.sessionId.first()
}