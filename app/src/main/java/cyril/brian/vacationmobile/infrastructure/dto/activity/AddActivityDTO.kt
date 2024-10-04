package cyril.brian.vacationmobile.infrastructure.dto.activity

data class AddActivityDTO (
    val activities: List<ActivityDTO>,
    val vacationId: String
)