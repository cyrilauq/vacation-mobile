package cyril.brian.vacationmobile.ui.vacation.infos.dialog_add_activity

import android.util.Log
import androidx.lifecycle.ViewModel
import cyril.brian.vacationmobile.domain.Coordinate
import cyril.brian.vacationmobile.domain.VacationActivity
import cyril.brian.vacationmobile.repositories.IGeocodingRepository
import cyril.brian.vacationmobile.repositories.IVacationActivitiesRepository
import cyril.brian.vacationmobile.repositories.exception.StockageException

class AddActivityDialogViewModel : ViewModel() {
    private var _geoRepository: IGeocodingRepository? = IGeocodingRepository.get()
    private var _activitiesRepo: IVacationActivitiesRepository? = IVacationActivitiesRepository.get()

    var geoRepository
        get() = _geoRepository!!
        set(value) {
            _geoRepository = value
        }

    var activitiesRepo
        get() = _activitiesRepo!!
        set(value) {
            _activitiesRepo = value
        }

    var error = ""

    var lat: Double = 0.0
    var lon: Double = 0.0

    var title: String = ""
        set(value) {
            if(value.isEmpty()) return
            field = value
        }

    var description: String = ""
        set(value) {
            if(value.isEmpty()) return
            field = value
        }

    var place: String = ""
        set(value) {
            if(value.isEmpty()) return
            field = value
        }

    fun isValid(): Boolean {
        if(title.trim().isEmpty()) {
            error = "The title shouldn't be empty."
            return false
        }
        if(description.trim().isEmpty()) {
            error = "The description shouldn't be empty."
            return false
        }
        if(place.trim().isEmpty()) {
            error = "The place shouldn't be empty."
            return false
        }
        return true
    }

    fun getCoordinatesFor(place: String): Coordinate? {
        if(place.trim().length < 10) {
            return null
        }
        return try {
            val result = geoRepository.findLocationFor(place)!!
            lat = result.latitude
            lon = result.longitude
            Coordinate(result.longitude, result.latitude)
        } catch (e: StockageException) {
            error = e.message!!
            null
        }
    }

    fun save(): Boolean {
        try {
            val result = activitiesRepo.addActivitiesToVacation(
                "",
                    arrayOf(
                        VacationActivity(
                            null,
                            title,
                            description,
                            lon,
                            lat,
                            place
                        )
                    )
                )
            return true
        } catch (e: StockageException) {
            Log.d("AddActivityDialogViewModel", e.message!!)
            error = e.message!!
            return false
        }
    }
}