package cyril.brian.vacationmobile.infrastructure.utils

import cyril.brian.vacationmobile.infrastructure.api_methods.GeocodingServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeocodingHelper {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/geocode/"
    val geocodingHelper: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    val geocodingServices: GeocodingServices by lazy {
        geocodingHelper.create(
            GeocodingServices::class.java
        )
    }
}