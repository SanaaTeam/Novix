package com.sanaa.identity.exceptions


open class AuthenticationException(message: String?) : Exception(message ?: "Unknown Error")
open class LoginErrorException(message: String?) : AuthenticationException(message)
class ResponseException(message: String?) : Exception(message ?: "Unknown Error")

class InvalidUserOrPasswordException(
    userName: String,
) : LoginErrorException("Invalid username($userName) and/or password: You did not provide a valid login.")

class EmailNotVerifiedException(
    userName: String,
) : LoginErrorException("Email not verified for ($userName): Your email address has not been verified.")

class InvalidTokenException(message: String?) : AuthenticationException(message)
class CreateGuestTokenException(message: String?) : AuthenticationException(message)
