package cyril.brian.vacationmobile.ui.vacation.create

import android.util.Log
import androidx.lifecycle.ViewModel
import cyril.brian.vacationmobile.domain.Coordinate
import cyril.brian.vacationmobile.domain.PeriodTime
import cyril.brian.vacationmobile.domain.Vacation
import cyril.brian.vacationmobile.repositories.IGeocodingRepository
import cyril.brian.vacationmobile.repositories.IVacationRepository
import cyril.brian.vacationmobile.repositories.exception.AddVacationException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import java.text.SimpleDateFormat
import java.util.Locale

class CreateVacationViewModel : ViewModel() {
    var _repository: IVacationRepository? = IVacationRepository.get()

    var repository
        get() = _repository!!
        set(value) {
            _repository = value
        }
    var _geoRepository: IGeocodingRepository? = IGeocodingRepository.get()

    var geoRepository
        get() = _geoRepository!!
        set(value) {
            _geoRepository = value
        }

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

    var date_begin: String = "28/12/2023"
        set(value) {
            if(value.isEmpty()) return
            field = value
        }

    var date_end: String = "28/12/2024"
        set(value) {
            if(value.isEmpty()) return
            field = value
        }

    var hour_begin: String = "18:00"
        set(value) {
            if(value.isEmpty()) return
            field = value
        }

    var hour_end: String = "18:00"
        set(value) {
            if(value.isEmpty()) return
            field = value
        }

    var lat: Double = 0.0
    var lon: Double = 0.0
    var country: String = ""

    fun isValid(): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
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
        if(date_begin.trim().isEmpty()) {
            error = "The date_begin shouldn't be empty."
            return false
        }
        if(date_end.trim().isEmpty()) {
            error = "The date_end shouldn't be empty."
            return false
        }
        if(hour_begin.trim().isEmpty()) {
            error = "The hour_begin shouldn't be empty."
            return false
        }
        if(hour_end.trim().isEmpty()) {
            error = "The hour_end shouldn't be empty."
            return false
        }
        if(dateFormat.parse("$date_end $hour_end")!! <= dateFormat.parse("$date_begin $hour_begin")) {
            error = "The period shouldn't end before or at the same time as its beginning."
            return false
        }
        return true
    }

    var error = ""

    fun save(): Boolean {
        return try {
            if(!isValid()) {
                throw AddVacationException("The viewmodel fields are not all valids!")
            }
            val data: Vacation? = repository.newVacation(
                title,
                place,
                country,
                description,
                PeriodTime(hour_begin, date_begin),
                PeriodTime(hour_end, date_end),
                Coordinate(lon, lat)
            )
            true
        } catch (e: AddVacationException) {
            error = "${e.message}"
            Log.d("LoginViewModel", "Error ${e.message}")
            false
        } catch (e: Exception) {
            error = "${e.message}"
            Log.d("LoginViewModel", "Error ${e.message}")
            false
        }
    }

    fun getCoordinatesFor(place: String): Coordinate? {
        return try {
            val result = geoRepository.findLocationFor(place)!!
            lat = result.latitude
            lon = result.longitude
            country = result.country
            Coordinate(result.longitude, result.latitude)
        } catch (e: StockageException) {
            error = "No place found"
            null
        }
    }
}