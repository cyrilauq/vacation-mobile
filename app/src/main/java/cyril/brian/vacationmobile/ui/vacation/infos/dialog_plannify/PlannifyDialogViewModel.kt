package cyril.brian.vacationmobile.ui.vacation.infos.dialog_plannify

import androidx.lifecycle.ViewModel
import cyril.brian.vacationmobile.repositories.IVacationActivitiesRepository
import cyril.brian.vacationmobile.repositories.args.PlannifyArgs
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PlannifyDialogViewModel: ViewModel() {
    private var _repository: IVacationActivitiesRepository? = IVacationActivitiesRepository.get()

    var repository
        get() = _repository!!
        set(value) {
            _repository = value
        }

    var beginDate: String = ""
    var beginTime: String = ""
    var endDate: String = ""
    var endTime: String = ""
    var message: String = ""

    fun isValid(): Boolean {
        return beginDate.isNotEmpty() && beginTime.isNotEmpty() && endDate.isNotEmpty() && endTime.isNotEmpty()
    }

    fun plannify(id: String = ""): Boolean {
        val begin = LocalDateTime.parse("$beginDate $beginTime", DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm"))
        val end = LocalDateTime.parse("$endDate $endTime", DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm"))
        if(!isValid()) {
            message = "One or multiple input is invalid."
            return false
        }
        if(begin >= end) {
            message = "Activity cannot end before it's beginning"
            return false
        }
        return try {
            repository.plannifyActivity(
                PlannifyArgs(
                    id,
                    beginDate,
                    endDate,
                    beginTime,
                    endTime
                )
            )
            message = "Activity plannified"
            true
        } catch (e: StockageException) {
            message = e.message!!
            false
        } catch (e: AuthException) {
            message = e.message!!
            false
        }
    }

}