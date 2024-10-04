package cyril.brian.vacationmobile.infrastructure.responses

data class VacationResponse(
    val id: String,
    val title: String,
    var place: String,
    var dateBegin: String,
    var timeBegin: String,
    var dateEnd: String,
    var timeEnd: String,
    var latitude: String,
    val isPublished: Boolean = false,
    var longitude: String,
    val description: String
)
