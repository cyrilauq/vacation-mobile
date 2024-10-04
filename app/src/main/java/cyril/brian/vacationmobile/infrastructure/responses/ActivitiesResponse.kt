package cyril.brian.vacationmobile.infrastructure.responses

data class ActivitiesResponse(
    val vacationId: String?,
    val activities: Array<ActivityResponse>
)
