package cyril.brian.vacationmobile.ui.vacation.infos

data class VacationModel(
    val title: String,
    val description: String,
    val place: String,
    val beginDate: String,
    val beginTime: String,
    val endDate: String,
    val endTime: String,
    val lon: String,
    val lat: String,
    val isPublished: Boolean
)
