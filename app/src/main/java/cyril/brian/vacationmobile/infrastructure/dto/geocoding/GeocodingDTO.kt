package cyril.brian.vacationmobile.infrastructure.dto.geocoding

data class GeocodingDTO(
    val results: List<GeocodingPartDTO>,
    val status: String
)
