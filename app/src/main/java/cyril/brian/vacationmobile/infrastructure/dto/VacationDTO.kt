package cyril.brian.vacationmobile.infrastructure.dto

data class VacationDTO(
    val title: String,
    var place: String,
    var dateBegin: String,
    var hourBegin: String,
    var dateEnd: String,
    var hourEnd: String,
    var latitude: String,
    var longitude: String,
    val description: String = "",
    val userId: String = "",
    val isPublished: Boolean = false,
    val ownerName: String = ""
)
