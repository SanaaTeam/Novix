package service

interface VodStringProvider {
    val noInternetConnectionError: String
    val somethingWentWrongError: String
    val addToListFailed: String
    val addToListSuccess: String
    val submitRatingSuccess: String
    val submitRatingFailed: String
}