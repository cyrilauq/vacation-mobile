package cyril.brian.vacationmobile.infrastructure.api_methods

import cyril.brian.vacationmobile.infrastructure.dto.geocoding.GeocodingDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingServices {

    /**
     * Lorsqu'il faut utiliser des query parameters dans nos requêtes, il faut utiliser l'annotation Query.
     * Exemple de query parameter: "parameter=value"
     */
    @GET("json")
    suspend fun findLocation(@Query("address") address: String, @Query("key") apiKey: String, @Query("language") lang: String = "fr"): Response<GeocodingDTO>

}