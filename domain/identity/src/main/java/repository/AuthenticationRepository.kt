package repository

interface AuthenticationRepository {
    suspend fun login(userName: String, password: String)
    suspend fun requestAccessToken(): String
    suspend fun createAccessToken(requestToken: String)
    suspend fun createGuestSession()
}