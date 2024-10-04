package cyril.brian.vacationmobile.infrastructure.responses

data class GetVacationResultItemResponse(
    val id: String,
    val title: String,
    val description: String,
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val dateTimeBegin: String,
    val dateTimeEnd: String,
    val userId: String,
    val isPublished: Boolean,
    val userName: String?
)