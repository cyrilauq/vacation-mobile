package cyril.brian.vacationmobile.infrastructure.responses

data class UserLoginResponse(
    var token: String,
    var uid: String,
    var mail: String,
    var name: String,
    var firstname: String,
    var username: String,
    var imgPath: String
)
