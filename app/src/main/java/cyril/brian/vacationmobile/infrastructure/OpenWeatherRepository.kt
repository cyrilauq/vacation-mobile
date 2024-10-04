package cyril.brian.vacationmobile.infrastructure

import com.google.gson.JsonParseException
import cyril.brian.vacationmobile.domain.Forecast
import cyril.brian.vacationmobile.infrastructure.api_methods.WeatherServices
import cyril.brian.vacationmobile.infrastructure.dto.weather.ForecastDTO
import cyril.brian.vacationmobile.infrastructure.dto.weather.OpenWeatherDTO
import cyril.brian.vacationmobile.repositories.IOpenWeatherRepository
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

class OpenWeatherRepository(retrofit: Retrofit): IOpenWeatherRepository {
    private val weatherServices: WeatherServices by lazy {
        retrofit.create(
            WeatherServices::class.java
        )
    }

    override fun getForecast(
        lat: Double,
        lon: Double
    ): List<Forecast> {
        val result: MutableList<Forecast> = mutableListOf()
        try {
            runBlocking {
                val response = async {
                    weatherServices.getForecast(lat, lon, "metric", "fr", 33, "77d73a663d7dc5d1a3d7069205f62b0c")
                }.await()
                val body: OpenWeatherDTO = response.body() ?: throw AuthException("An exception occured when trying to contact OpenWeather API")
                if(body.list.isEmpty()) {
                    throw Exception("An error occured, the forecast shouldn't be empty")
                }

                val forecastsDTO: List<ForecastDTO> = body.list
                val now: ForecastDTO = forecastsDTO[0]
                val tomorrow: ForecastDTO = forecastsDTO[8]
                val twoDays: ForecastDTO = forecastsDTO[16]
                val threeDays: ForecastDTO = forecastsDTO[24]
                val fourDays: ForecastDTO = forecastsDTO[32]

                result.add(Forecast(now.dt, now.main.temp, now.weather[0].main, now.weather[0].description, now.weather[0].icon))
                result.add(Forecast(tomorrow.dt, tomorrow.main.temp, tomorrow.weather[0].main, tomorrow.weather[0].description, tomorrow.weather[0].icon))
                result.add(Forecast(twoDays.dt, twoDays.main.temp, twoDays.weather[0].main, twoDays.weather[0].description, twoDays.weather[0].icon))
                result.add(Forecast(threeDays.dt, threeDays.main.temp, threeDays.weather[0].main, threeDays.weather[0].description, threeDays.weather[0].icon))
                result.add(Forecast(fourDays.dt, fourDays.main.temp, fourDays.weather[0].main, fourDays.weather[0].description, fourDays.weather[0].icon))
            }
        } catch (e: SocketTimeoutException) {
            throw StockageException("No response found")
        } catch (e: SSLHandshakeException) {
            throw StockageException("Maybe you forgot to open your vpn")
        } catch (e: JsonParseException) {
            throw JsonParseException("Error in the received data")
        } catch (e: IOException) {
            throw StockageException("The connection with the server is not possible")
        }
        return result
    }
}
