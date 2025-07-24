package repository

interface AuthenticationRepository {
    suspend fun login(userName: String, password: String)
    suspend fun requestAccessToken(): String
    suspend fun createAccessToken()
    suspend fun createGuestSession()
}