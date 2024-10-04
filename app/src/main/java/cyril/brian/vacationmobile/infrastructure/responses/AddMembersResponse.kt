package cyril.brian.vacationmobile.infrastructure.responses

import cyril.brian.vacationmobile.infrastructure.dto.UserDTO

data class AddMembersResponse(
    val members: Array<UserDTO>,
    val count: Int
)