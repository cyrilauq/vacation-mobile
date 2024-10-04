package cyril.brian.vacationmobile.ui.vacation.infos.dialog_add_activity

import cyril.brian.vacationmobile.repositories.IGeocodingRepository
import cyril.brian.vacationmobile.repositories.results.GeoResults

class GeoTestRepository: IGeocodingRepository {
    var coordinate: GeoResults? = null

    fun setCoordinate(lat: Double, lon: Double) {
        coordinate = GeoResults(
            lat,
            lon,
            "Belgique"
        )
    }
    override fun findLocationFor(address: String): GeoResults? {
        return coordinate!!
    }
}