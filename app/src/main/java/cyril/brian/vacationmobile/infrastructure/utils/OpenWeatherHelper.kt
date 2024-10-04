package cyril.brian.vacationmobile.infrastructure.utils

import cyril.brian.vacationmobile.infrastructure.api_methods.WeatherServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenWeatherHelper {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val openWeatherHelper: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    val openWeatherServices: WeatherServices by lazy {
        openWeatherHelper.create(
            WeatherServices::class.java
        )
    }
}