package service

interface IdentityStringProvider {
    val welcomeBack: String
    val noInternetConnectionError: String
    val invalidUserNameAndPasswordError: String
    val enterUserNameAndPasswordError: String
    val somethingWentWrongError: String
    val loginSuccess: String
}