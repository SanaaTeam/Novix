package repository

interface AuthenticationRepository {
    suspend fun login(userName: String, password: String)
    suspend fun createGuestSession()
}