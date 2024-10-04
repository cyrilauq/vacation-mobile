package cyril.brian.vacationmobile.infrastructure.responses

data class ActivityResponse(
    val activityId: String?,
    val title: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val place: String
)