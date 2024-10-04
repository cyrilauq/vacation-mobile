package cyril.brian.vacationmobile.infrastructure.responses

import cyril.brian.vacationmobile.domain.Vacation

data class GetVacationUserResponse(var vacations: List<GetVacationResultItemResponse>)