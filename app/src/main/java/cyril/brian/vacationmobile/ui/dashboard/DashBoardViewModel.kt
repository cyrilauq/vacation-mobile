package cyril.brian.vacationmobile.ui.dashboard

import androidx.lifecycle.ViewModel
import cyril.brian.vacationmobile.infrastructure.responses.GetVacationResultItemResponse
import cyril.brian.vacationmobile.repositories.IVacationRepository

class DashBoardViewModel : ViewModel() {
    private var vacationList: MutableList<GetVacationResultItemResponse> = mutableListOf()
    var _repository: IVacationRepository? = IVacationRepository.get()

    var repository
        get() = _repository!!
        set(value) {
            _repository = value
        }

    private var error: String = ""

    fun getVacations(): List<GetVacationResultItemResponse> {
        return vacationList.toList()
    }

    fun getVacation(position: Int): GetVacationResultItemResponse {
        val result = vacationList[position]
        repository.setCurrentVacation(result.id)
        return result
    }

    fun gatherUserVacations(): Boolean {
        error = ""
        return try {
            vacationList =
                repository.getVacationsUser("").vacations.toMutableList() // <-- State.user!!.token à la place de Bearer fakeToken
            true
        } catch(e: Exception) {
            error = e.message.toString()
            false
        }
    }

    fun getError(): String {
        return error
    }
}