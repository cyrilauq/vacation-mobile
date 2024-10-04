package cyril.brian.vacationmobile.infrastructure

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.AdressComponent
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.GeocodingCoordinateDTO
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.GeocodingDTO
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.GeocodingGeometryDTO
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.GeocodingPartDTO
import cyril.brian.vacationmobile.repositories.exception.StockageException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class GeocodingRepositoryTests {
    var _repository: GeocodingRepository? = null
    private lateinit var mockServer: MockWebServer

    private val repository
        get() = _repository!!

    val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    @Before
    fun setUp() {
        try {
            mockServer = MockWebServer()
            mockServer.start()
            val apiService = Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            _repository = GeocodingRepository(apiService)
        } catch (e: Exception) {
            throw e
        }
    }

    @Test
    fun test() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                Gson().toJson(
                    GeocodingDTO(
                        listOf<GeocodingPartDTO>(
                            GeocodingPartDTO(
                                listOf(
                                    AdressComponent("Belgique"),
                                    AdressComponent("Belgique"),
                                    AdressComponent("Belgique"),
                                    AdressComponent("Belgique"),
                                    AdressComponent("Belgique"),
                                    AdressComponent("Belgique")
                                ),
                                GeocodingGeometryDTO(
                                    GeocodingCoordinateDTO(
                                        "50.5552275",
                                        "5.308621700000001"
                                    )
                                )
                            )
                        ),
                        ""
                    )
                )
            )
        mockServer.enqueue(response)
        val result = repository.findLocationFor("Rue Hodinfosse 31, 4540 Amay, Belgique")
        assertNotNull(result)
        assertEquals(50.5552275, result!!.latitude, 0.001)
        assertEquals(5.308621700000001, result.longitude, 0.001)
    }

    @Test
    fun test2() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                Gson().toJson(
                    GeocodingDTO(
                        listOf(
                            GeocodingPartDTO(
                                listOf(
                                    AdressComponent("Belgique"),
                                    AdressComponent("Belgique"),
                                    AdressComponent("Belgique"),
                                    AdressComponent("Belgique"),
                                    AdressComponent("Belgique"),
                                    AdressComponent("Belgique")
                                ),
                                GeocodingGeometryDTO(
                                    GeocodingCoordinateDTO(
                                        "37.4224428",
                                        "-122.0842467"
                                    )
                                )
                            )
                        ),
                        ""
                    )
                )
            )
        mockServer.enqueue(response)
        val result = repository.findLocationFor("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\"")
        assertNotNull(result)
        assertEquals(37.4224428, result!!.latitude, 0.001)
        assertEquals(-122.0842467, result.longitude, 0.001)
    }

    @Test(expected = StockageException::class)
    fun whenTimeOutThenThrowsException() {
        repository.findLocationFor("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\"")
    }

    @Test(expected = StockageException::class)
    fun whenWrongFormattedResponseThenThrowsException() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("[- }")
        mockServer.enqueue(response)
        repository.findLocationFor("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\"")
    }

}