package exceptions

open class DataSourceAccessException(message: String) : Exception(message)
open class StoringDataFailureException(message: String) : DataSourceAccessException(message)
open class RetrievingDataFailureException(message: String) : DataSourceAccessException(message)

class NotFoundException(date: String) : RetrievingDataFailureException("$date not found")
class FailedToAddException(data: String) : StoringDataFailureException("Failed to add $data")
class FailedToDeleteException(data: String) : StoringDataFailureException("Failed to delete $data")