package cyril.brian.vacationmobile.infrastructure

import android.util.Log
import com.google.gson.JsonParseException
import cyril.brian.vacationmobile.infrastructure.api_methods.GeocodingServices
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.GeocodingDTO
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.getCountry
import cyril.brian.vacationmobile.repositories.IGeocodingRepository
import cyril.brian.vacationmobile.repositories.exception.StockageException
import cyril.brian.vacationmobile.repositories.results.GeoResults
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

class GeocodingRepository(
    retrofit: Retrofit
): IGeocodingRepository {
    private val geocodingServices: GeocodingServices by lazy {
        retrofit.create(
            GeocodingServices::class.java
        )
    }

    override fun findLocationFor(address: String): GeoResults? {
        var result: GeoResults? = null
        try {

            runBlocking {
                val response = async {
                    geocodingServices.findLocation(
                        address,
                        "AIzaSyCEDO2UJWu6I_H0vXQ-qFAGHJAm2A5phUg")
                }.await()
                val body: GeocodingDTO = response.body() ?: throw StockageException("Nothing was found")

                val temp = if (body.results.isNotEmpty()) body.results[0].geometry.location else throw StockageException("Nothing was found")
                if(temp != null) {
                    result = GeoResults(temp.lat.toDouble(), temp.lng.toDouble(), body.getCountry())
                }

                Log.d("GeocodingRepository", "Response: ${response}")
            }
        } catch (e: SocketTimeoutException) {
            throw StockageException("No response found")
        } catch (e: SSLHandshakeException) {
            throw StockageException("Maybe you forgot to open your vpn")
        } catch (e: JsonParseException) {
            throw StockageException("Error in the received data")
        } catch (e: IOException) {
            throw StockageException("The connection with the server is not possible")
        }
        return result
    }
}