package exceptions

open class AuthenticationException(message: String?) : Exception(message ?: "Unknown Error")
open class LoginErrorException(message: String?) : AuthenticationException(message)

class NoInternetConnectionException :
    Exception("No internet connection ")

class InvalidUserOrPasswordException :
    LoginErrorException("Invalid username and/or password: You did not provide a valid login.")

class NoLoggedInUserException : AuthenticationException(
    "No logged in user found."
)

