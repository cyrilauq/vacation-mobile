package cyril.brian.vacationmobile.repositories.exception

/**
 * This class will contains all the exceptions related to the activities
 */
data class ActivityFormatException(val msg: String): RuntimeException(msg)

/**
 * Represents an exception that will be thrown when the activity wasn't found.
 */
data class ActivityNotFoundException(val msg: String): RuntimeException(msg)