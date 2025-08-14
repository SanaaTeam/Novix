package service

interface VodStringProvider {
    val noInternetConnectionError: String
    val somethingWentWrongError: String
    val addToListFailed: String
    val addToListSuccess: String
    val deleteRatingSuccess: String
    val deleteRatingFailed: String
}