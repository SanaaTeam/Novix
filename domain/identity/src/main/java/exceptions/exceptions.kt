package exceptions


open class IdentityException(message: String?= null) : Exception(message ?: "Unknown Error")

class NoLoggedInUserException : IdentityException("No logged in user found.")
class NoInternetException : IdentityException("No internet connection")

open class LoginErrorException(message: String?) : IdentityException(message)
class InvalidUserOrPasswordException : LoginErrorException("Invalid username or password")

