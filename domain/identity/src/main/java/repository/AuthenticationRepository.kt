package repository

import entity.User

interface AuthenticationRepository {
    suspend fun login(userName: String, password: String)
    suspend fun createGuestSession()
    suspend fun getLoggedUser(): User
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
}