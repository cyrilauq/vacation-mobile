package cyril.brian.vacationmobile.ui.vacation.infos

import android.util.Log
import androidx.lifecycle.ViewModel
import cyril.brian.vacationmobile.domain.Forecast
import cyril.brian.vacationmobile.domain.User
import cyril.brian.vacationmobile.domain.Vacation
import cyril.brian.vacationmobile.repositories.IOpenWeatherRepository
import cyril.brian.vacationmobile.repositories.IVacationActivitiesRepository
import cyril.brian.vacationmobile.repositories.IVacationRepository
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import cyril.brian.vacationmobile.repositories.exception.VacationNotFoundException
import cyril.brian.vacationmobile.repositories.exception.VacationPublishedException
import cyril.brian.vacationmobile.ui.vacation.infos.adapter.ActivityModel

class VacationViewModel : ViewModel() {
    private var _repository: IVacationRepository? = IVacationRepository.get()
    private var _activitiesRepository: IVacationActivitiesRepository? = IVacationActivitiesRepository.get()
    private var _weatherRepository: IOpenWeatherRepository? = IOpenWeatherRepository.get()

    var repository
        get() = _repository!!
        set(value) {
            _repository = value
        }

    var activitiesRepository
        get() = _activitiesRepository!!
        set(value) {
            _activitiesRepository = value
        }

    var weatherRepository
        get() = _weatherRepository!!
        set(value) {
            _weatherRepository = value
        }

    private var _vacation: Vacation? = null
    private val vacation get() = _vacation!!

    lateinit var data: VacationModel

    private var _activities: MutableList<ActivityModel> = mutableListOf()
    val activities get() = _activities

    private var _members: MutableList<User> = mutableListOf()
    private var _forecasts: List<Forecast> = listOf()

    var errorMsg: String? = null
    private var _message: String? = null
    val message get() = _message ?: ""

    val hasVacation get() = _vacation != null

    fun load(): Boolean {
        return try {
            _vacation = repository.getVacationById(repository.getCurrentVacation())
            data = VacationModel(
                vacation.title,
                vacation.description,
                vacation.place,
                vacation.begin.date,
                vacation.begin.time,
                vacation.end.date,
                vacation.end.time,
                vacation.coordinate.lon.toString(),
                vacation.coordinate.lat.toString(),
                vacation.isPublished
            )
            true
        } catch (e: VacationNotFoundException) {
            Log.d("VacationViewModel", "Vacation not found")
            _message = e.message
            false
        }
    }

    fun addMembers(members: List<String>): Boolean {
        return try {
            val result = repository.addMembers(_vacation!!.id!!, members)
            vacation.members += result
            true
        } catch (e: VacationNotFoundException) {
            errorMsg = "Vacation not found"
            false
        } catch (e: AuthException) {
            errorMsg = "You cannot perform this action"
            false
        } catch (e: VacationPublishedException) {
            errorMsg = "The vacation is published"
            false
        }
    }

    fun gatherActivities(): Boolean {
        errorMsg = ""
        return try {
            _activities = activitiesRepository.getActivities(_vacation!!.id!!).map {
                ActivityModel(
                    it.activityId,
                    it.title,
                    it.description,
                    it.longitude.toString(),
                    it.latitude.toString(),
                    it.place
                )
            }.toMutableList()
            true
        } catch (e: VacationNotFoundException) {
            errorMsg = "Vacation not found"
            false
        } catch (e: AuthException) {
            errorMsg = "You cannot perform this action"
            false
        } catch (e: VacationPublishedException) {
            errorMsg = "The vacation is published"
            false
        } catch (e: StockageException) {
            errorMsg = "Unkown internal error"
            false
        }
    }

    fun gatherMembers(): Boolean {
        errorMsg = ""
        return try {
            _members = repository.getMembers(_vacation!!.id!!).toMutableList()
            true
        } catch (e: VacationNotFoundException) {
            errorMsg = "Vacation not found"
            false
        } catch (e: AuthException) {
            errorMsg = "You cannot perform this action"
            false
        } catch(e: Exception) {
            errorMsg = e.message
            false
        }
    }

    fun downloadPlanning(): String {
        return "File is in ${repository.downloadPlanning(_vacation!!.id!!)}"
    }

    fun getMembers(): List<User> {
        return _members
    }

    fun gatherForecast(): Boolean {
        errorMsg = ""
        return try {
            Log.d("VacationViewModel", "Is the repository created correctly ? $weatherRepository")
            _forecasts = weatherRepository.getForecast(vacation.coordinate.lat, vacation.coordinate.lon)
            true
        } catch (e: Exception) {
            Log.d("VacationViewModel", "Caught an exception : $e")
            errorMsg = "An error occured when gathering the forecasts"
            false
        }
    }

    fun getForecast(): List<Forecast> {
        return _forecasts
    }

    fun getError(): String? {
        return errorMsg
    }
}