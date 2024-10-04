package cyril.brian.vacationmobile.infrastructure.api_methods

import cyril.brian.vacationmobile.infrastructure.dto.weather.OpenWeatherDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {
    @GET("forecast")
    suspend fun getForecast(@Query("lat") lat: Double, @Query("lon") long: Double, @Query("units") units: String, @Query("lang") lang: String, @Query("cnt") cnt: Int, @Query("appid") appid: String): Response<OpenWeatherDTO>
}