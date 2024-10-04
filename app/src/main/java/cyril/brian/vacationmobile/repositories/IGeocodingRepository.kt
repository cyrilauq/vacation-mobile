package cyril.brian.vacationmobile.repositories

import com.google.android.gms.maps.model.LatLng
import cyril.brian.vacationmobile.repositories.results.GeoResults

interface IGeocodingRepository {
    companion object {
        private var _repository: IGeocodingRepository? = null

        fun get() = _repository

        fun initialize(repository: IGeocodingRepository) {
            _repository = repository
        }
    }

    fun findLocationFor(address: String): GeoResults?

}