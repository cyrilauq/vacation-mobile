package cyril.brian.vacationmobile.infrastructure.responses

import cyril.brian.vacationmobile.infrastructure.dto.UserDTO

data class UserSearchResponse(
    val count: Int,
    val result: List<UserDTO>
)
