package repository

import entity.User
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun login(userName: String, password: String)
    suspend fun createGuestSession()
     fun getLoggedUser(): Flow<User>
     fun isLoggedIn(): Flow<Boolean>
    suspend fun logout()
}