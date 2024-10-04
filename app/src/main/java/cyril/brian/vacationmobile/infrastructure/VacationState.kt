package cyril.brian.vacationmobile.infrastructure

import cyril.brian.vacationmobile.infrastructure.responses.UserLoginResponse

class VacationState {
    private var _user: UserLoginResponse? = null

    var user
        get() = _user!!
        set(value) {
            _user = value
        }

    var currentVacationId: String = ""
}