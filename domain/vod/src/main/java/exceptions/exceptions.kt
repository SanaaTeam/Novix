package exceptions

open class NovixAppException(message: String? = null) : Exception(message)

class NoNetworkException : NovixAppException("No network connection")
class OperationFailureException : NovixAppException("Operation failed")
