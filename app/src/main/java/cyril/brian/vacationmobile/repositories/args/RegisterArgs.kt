package cyril.brian.vacationmobile.repositories.args

data class RegisterArgs(
    val name: String,
    val firstname: String,
    val email: String,
    var login: String,
    val password: String
)
