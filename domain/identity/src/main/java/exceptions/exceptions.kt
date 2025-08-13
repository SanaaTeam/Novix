package exceptions


open class NovixAppException(message: String) : Exception(message)
class NoNetworkException : NovixAppException("No internet connection ")

open class AuthenticationException(message: String?) : NovixAppException(message ?: "Unknown Error")
class NoLoggedInUserException : AuthenticationException("No logged in user found.")

open class LoginErrorException(message: String?) : AuthenticationException(message)
class InvalidUserOrPasswordException : LoginErrorException("Invalid username or password")

