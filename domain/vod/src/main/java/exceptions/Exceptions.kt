package exceptions

open class DataSourceAccessException(message: String) : Exception(message)
open class StoringDataFailureException(message: String) : DataSourceAccessException(message)
open class RetrievingDataFailureException(message: String) : DataSourceAccessException(message)
open class NetworkFailureException(message: String) : DataSourceAccessException(message)
class NoNetworkException : Exception("No network connection. Please check your internet connection.")

class NotFoundException(data: String) : RetrievingDataFailureException("$data not found")
class FailedToAddException(data: String) : StoringDataFailureException("Failed to add $data")
class FailedToDeleteException(data: String) : StoringDataFailureException("Failed to delete $data")
