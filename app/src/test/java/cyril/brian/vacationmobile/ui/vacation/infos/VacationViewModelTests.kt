package cyril.brian.vacationmobile.ui.vacation.infos

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cyril.brian.vacationmobile.domain.Vacation
import cyril.brian.vacationmobile.infrastructure.UserApiRepository
import cyril.brian.vacationmobile.infrastructure.VacationApiRepository
import cyril.brian.vacationmobile.infrastructure.VacationState
import cyril.brian.vacationmobile.infrastructure.responses.UserLoginResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class VacationViewModelTests {
    lateinit var viewModel: VacationViewModel
    private var _vacation: Vacation? = null
    private var _userInfo: UserLoginResponse? = null

    private lateinit var mockServer: MockWebServer
    private lateinit var repository: VacationApiRepository

    val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private lateinit var _vacationState: VacationState

    @Before
    fun setUp() {
        try {
            _vacationState = VacationState()
            viewModel = VacationViewModel()
            mockServer = MockWebServer()
            mockServer.start()
            val apiService = Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            repository = VacationApiRepository(apiService, _vacationState)
            viewModel.repository = repository
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("{\n" +
                        "  \"token\": \"eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNobWFjLXNoYTUxMiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoidG91a2Ffa2kiLCJ1aWQiOiI0ZDhmOGIyNy1mOGJjLTQzOGUtYmU3MS1iN2I2N2Y5ZWViMjciLCJqdGkiOiI3ZTFkYmVlNS04ODI5LTQzN2EtYTYzNi03YWQxYThhNzMyZWEiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJVc2VyIiwiZXhwIjoxNjk5NDQ3NTQxLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjUwMDAiLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjQyMDAifQ.j014ma3gea5nSULpL4Kwa7MT2NOrabp-g72Ab7NnENrIQHiv3y4qfPj1gxp0JjI7vhd1LhsSRcDSfk476XfR4g\",\n" +
                        "  \"uid\": \"4d8f8b27-f8bc-438e-be71-b7b67f9eeb27\",\n" +
                        "  \"mail\": \"touka_ki@example.com\",\n" +
                        "  \"name\": \"Kirishima\",\n" +
                        "  \"firstname\": \"Touka\",\n" +
                        "  \"username\": \"touka_ki\",\n" +
                        "  \"imgPath\": \"url\"\n" +
                        "}")
            mockServer.enqueue(response)
            _userInfo = UserApiRepository(apiService, _vacationState).login("touka_ki", "Password123@")
        } catch (e: Exception) {
            throw e
        }
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun whenGivenIdExistThenNotErrorMessage() {

        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    " \"id\": \"string\",\n" +
                    "  \"title\": \"string\",\n" +
                    "  \"description\": \"string\",\n" +
                    "  \"place\": \"string\",\n" +
                    "  \"latitude\": \"550\",\n" +
                    "  \"longitude\": \"550\",\n" +
                    "  \"dateBegin\": \"5/02/2025\",\n" +
                    "  \"timeBegin\": \"08:50\",\n" +
                    "  \"dateEnd\": \"15/02/2025\",\n" +
                    "  \"timeEnd\": \"08:50\"\n" +
                    "}")
        mockServer.enqueue(response)

        _vacationState.user = UserLoginResponse(
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )

        viewModel.load()
        assertEquals("", viewModel.message)
    }

    @Test
    fun whenGivenIdNotExistThenErrorMessage() {

        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        mockServer.enqueue(response)

        _vacationState.user = UserLoginResponse(
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )

        assertFalse(viewModel.load())
        assertTrue(viewModel.message.isNotEmpty())
    }
}