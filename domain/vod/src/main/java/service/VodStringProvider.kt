package service

interface
VodStringProvider {
    val noInternetConnectionError: String
    val somethingWentWrongError: String
    val addToListFailed: String
    val addToListSuccess: String
    val deleteRatingSuccess: String
    val deleteRatingFailed: String
    val createListSuccess: String
    val createListFailed: String
    val deleteListFailed: String
    val deleteListSuccess: String
    val deleteFromListFailed: String
    val deleteFromListSuccess: String
}