package cyril.brian.vacationmobile.infrastructure.dto.geocoding

import com.google.gson.annotations.SerializedName

data class GeocodingPartDTO(
    @SerializedName("address_components") val addressComponents: List<AdressComponent>,
    val geometry: GeocodingGeometryDTO
)
