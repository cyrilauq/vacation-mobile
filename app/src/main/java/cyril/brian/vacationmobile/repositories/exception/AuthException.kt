package cyril.brian.vacationmobile.repositories.exception

class UserNotFoundException(message: String): RuntimeException(message)

class WrongCredentialsException(message: String): RuntimeException(message)

class AuthException(message: String): RuntimeException(message)