package cyril.brian.vacationmobile.infrastructure.dto.activity

data class ActivityDTO(
    val title: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val place: String
)