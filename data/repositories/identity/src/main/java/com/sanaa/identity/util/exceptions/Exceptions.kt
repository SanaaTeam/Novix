package com.sanaa.identity.util.exceptions


open class AuthenticationException(message: String?) : Exception(message ?: "Unknown Error")
open class LoginErrorException(message: String?) : AuthenticationException(message)
class ResponseException(val errorCode: Int) :
    Exception("Something went wrong, Error Code::$errorCode")

class InvalidUserOrPasswordException :
    LoginErrorException("Invalid username and/or password: You did not provide a valid login.")

class InvalidTokenException(message: String?) : AuthenticationException(message)
