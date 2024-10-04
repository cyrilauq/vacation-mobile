package cyril.brian.vacationmobile.infrastructure.dto

data class AddMembersDto(
    val vacationId: String,
    val membersUid: Array<String>
)