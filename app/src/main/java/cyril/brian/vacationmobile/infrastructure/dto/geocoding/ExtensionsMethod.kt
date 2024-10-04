package cyril.brian.vacationmobile.infrastructure.dto.geocoding

fun GeocodingDTO.getCountry(): String {
    return if(this.results.size == 0) "" else this.results[0].addressComponents[5].long_name
}

fun GeocodingDTO.getCoordinate(): GeocodingCoordinateDTO? {
    return if(this.results.size == 0) null else this.results[0].geometry.location
}