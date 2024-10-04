package cyril.brian.vacationmobile.ui.vacation.create

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cyril.brian.vacationmobile.infrastructure.GeocodingRepository
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.AdressComponent
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.GeocodingCoordinateDTO
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.GeocodingDTO
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.GeocodingGeometryDTO
import cyril.brian.vacationmobile.infrastructure.dto.geocoding.GeocodingPartDTO
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class CreateVacationViewModelTests {
    private val emptyStr = "    "

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
    fun whenTitleIsEmptyThenViewModelIsNotValid() {
        val viewModel = CreateVacationViewModel()
        viewModel.title = ""
        assertFalse(viewModel.isValid())
        viewModel.title = emptyStr
        assertFalse(viewModel.isValid())
    }

    @Test
    fun whenDescriptionIsEmptyThenViewModelIsNotValid() {
        val viewModel = CreateVacationViewModel()
        viewModel.description = ""
        assertFalse(viewModel.isValid())
        viewModel.description = emptyStr
        assertFalse(viewModel.isValid())
    }

    @Test
    fun whenPlaceIsEmptyThenViewModelIsNotValid() {
        val viewModel = CreateVacationViewModel()
        viewModel.place = ""
        assertFalse(viewModel.isValid())
        viewModel.place = emptyStr
        assertFalse(viewModel.isValid())
    }

    @Test
    fun whenDateBeginIsEmptyThenViewModelIsNotValid() {
        val viewModel = CreateVacationViewModel()
        viewModel.date_begin = ""
        assertFalse(viewModel.isValid())
        viewModel.date_begin = emptyStr
        assertFalse(viewModel.isValid())
    }

    @Test
    fun whenDateEndIsEmptyThenViewModelIsNotValid() {
        val viewModel = CreateVacationViewModel()
        viewModel.date_end = ""
        assertFalse(viewModel.isValid())
        viewModel.date_end = emptyStr
        assertFalse(viewModel.isValid())
    }

    @Test
    fun whenTimeBeginIsEmptyThenViewModelIsNotValid() {
        val viewModel = CreateVacationViewModel()
        viewModel.hour_begin = ""
        assertFalse(viewModel.isValid())
        viewModel.hour_begin = emptyStr
        assertFalse(viewModel.isValid())
    }

    @Test
    fun whenTimeEndIsEmptyThenViewModelIsNotValid() {
        val viewModel = CreateVacationViewModel()
        viewModel.hour_end = ""
        assertFalse(viewModel.isValid())
        viewModel.hour_end = emptyStr
        assertFalse(viewModel.isValid())
    }

    @Test
    fun whenPeriodEndWhenItsBeginThenViewModelIsNotValid() {
        val viewModel = CreateVacationViewModel()
        viewModel.hour_end = "15:00"
        viewModel.hour_begin = "15:00"
        viewModel.date_end = "15/08/2025"
        viewModel.date_begin = "15/08/2025"
        viewModel.title = "One test"
        viewModel.description = "The TEST"
        viewModel.place = "the place"
        viewModel.lat = 150.0
        viewModel.lon = 150.0
        assertFalse(viewModel.isValid())
    }

    @Test
    fun whenAllFieldAreCorrectThenViewModelIsValid() {
        val viewModel = CreateVacationViewModel()
        viewModel.hour_end = "15:00"
        viewModel.hour_begin = "5:00"
        viewModel.date_end = "25/08/2025"
        viewModel.date_begin = "15/08/2025"
        viewModel.title = "One test"
        viewModel.description = "The TEST"
        viewModel.place = "the place"
        viewModel.lat = 150.0
        viewModel.lon = 150.0
        assertTrue(viewModel.isValid())
    }

    @Test
    fun whenCoordinatesForPlaceAreAskedThenReturnTheCoordinate() {
        val viewModel = CreateVacationViewModel()
        viewModel.geoRepository = repository
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
        val result = viewModel.getCoordinatesFor("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\"")
        assertNotNull(result)
        assertEquals(37.4224428, result!!.lat, 0.001)
        assertEquals(-122.0842467, result.lon, 0.001)
    }

}