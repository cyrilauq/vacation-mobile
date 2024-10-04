package cyril.brian.vacationmobile.repositories.exception

class VacationNotFoundException(message: String): RuntimeException(message)

data class VacationPublishedException(val msg: String): RuntimeException(msg)