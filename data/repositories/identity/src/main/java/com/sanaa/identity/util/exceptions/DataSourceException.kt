package com.sanaa.identity.util.exceptions

open class DataSourceException(message: String? = null) : Exception(message)

class ConnectionException(message: String? = null) : DataSourceException(message)
class TimeoutException(message: String? = null) : DataSourceException(message)
class ServerErrorException(message: String? = null) : DataSourceException(message)
class ParsingException(message: String? = null) : DataSourceException(message)
class UnknownDataSourceException(message: String? = null) : DataSourceException(message)