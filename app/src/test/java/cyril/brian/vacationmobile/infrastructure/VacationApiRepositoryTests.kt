package cyril.brian.vacationmobile.infrastructure

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cyril.brian.vacationmobile.domain.Vacation
import cyril.brian.vacationmobile.infrastructure.dto.UserDTO
import cyril.brian.vacationmobile.infrastructure.responses.AddMembersResponse
import cyril.brian.vacationmobile.infrastructure.responses.GetVacationResultItemResponse
import cyril.brian.vacationmobile.infrastructure.responses.UserLoginResponse
import cyril.brian.vacationmobile.infrastructure.responses.VacationResponse
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.VacationNotFoundException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class VacationApiRepositoryTests {
    private var _userInfo: UserLoginResponse? = null

    private lateinit var mockServer: MockWebServer
    private lateinit var repository: VacationApiRepository

    val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }
    private val vacationState = VacationState()

    @Config(shadows = [NetworkSecurityPolicyShadow::class])
    @Before
    fun setUp() {
        try {
            mockServer = MockWebServer()
            mockServer.start()
            val apiService = Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            repository = VacationApiRepository(apiService, vacationState)
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(Gson().toJson(
                    UserLoginResponse(
                        "eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNobWFjLXNoYTUxMiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoidG91a2Ffa2kiLCJ1aWQiOiI0ZDhmOGIyNy1mOGJjLTQzOGUtYmU3MS1iN2I2N2Y5ZWViMjciLCJqdGkiOiI3ZTFkYmVlNS04ODI5LTQzN2EtYTYzNi03YWQxYThhNzMyZWEiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJVc2VyIiwiZXhwIjoxNjk5NDQ3NTQxLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjUwMDAiLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjQyMDAifQ.j014ma3gea5nSULpL4Kwa7MT2NOrabp-g72Ab7NnENrIQHiv3y4qfPj1gxp0JjI7vhd1LhsSRcDSfk476XfR4g",
                        "4d8f8b27-f8bc-438e-be71-b7b67f9eeb27",
                        "touka_ki@example.com",
                        "Kirishima",
                        "Touka",
                        "touka_ki",
                        "url"
                    )
                ))
            mockServer.enqueue(response)
            _userInfo = UserApiRepository(apiService, vacationState).login("touka_ki", "Password123@")
            vacationState.user = _userInfo!!
        } catch (e: Exception) {
            throw e
        }
    }

    @Config(shadows = [NetworkSecurityPolicyShadow::class])
    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Config(shadows = [NetworkSecurityPolicyShadow::class])
    @Test
    fun whenVacationWithGivenIdExistsThenRetrieveVacation() {
        val result: Vacation?
        try {
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(
                    Gson().toJson(
                        VacationResponse(
                            "string",
                            "string",
                            "string",
                            "5/02/2025",
                            "08:50",
                            "08:50",
                            "15/02/2025",
                            "550.0",
                            false,
                            "550.0",
                            "string"
                        )
                    )
                )
            mockServer.enqueue(response)
            result = repository.getVacationById("string")
            assertNotNull(result)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    @Config(shadows = [NetworkSecurityPolicyShadow::class])
    @Test(expected = VacationNotFoundException::class)
    fun whenVacationIdDoesNotExistThenThrowsError() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        mockServer.enqueue(response)
        repository.getVacationById("123456789")
    }

    @Config(shadows = [NetworkSecurityPolicyShadow::class])
    @Test(expected = AuthException::class)
    fun whenTokenNotValidThenThrowException() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
        mockServer.enqueue(response)
        repository.getVacationById("")
    }

    @Config(shadows = [NetworkSecurityPolicyShadow::class])
    @Test
    fun whenGetVacationsOfOneUserThenRetrieveThem() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(
                listOf(
                    GetVacationResultItemResponse(
                        "lkjlkjmj",
                        "Vacance FakeApi 1",
                        "",
                        "",
                        30.0,
                        30.0,
                        "5/02/2025T08:50",
                        "15/02/2025T08:50",
                        vacationState.user.uid,
                        false,
                        vacationState.user.username
                    ),
                    GetVacationResultItemResponse(
                        "lkjlkjmj",
                        "Vacance FakeApi 1",
                        "",
                        "",
                        30.0,
                        30.0,
                        "2024-11-09T08:45",
                        "2024-11-11T08:45",
                        vacationState.user.uid,
                        false,
                        vacationState.user.username
                    )
                )
            ))
        mockServer.enqueue(response)
        val result = repository.getVacationsUser(vacationState.user.token)
        assertEquals(2, result.vacations.size)
    }

    @Config(shadows = [NetworkSecurityPolicyShadow::class])
    @Test
    fun whenAddExistingUidThenReturnThem() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(
                AddMembersResponse(
                    arrayOf(
                        UserDTO(
                            "Auquier",
                            "Cyril",
                            "lmkjlmjmlj",
                            "fdgdfgfdg",
                            "",
                            "",
                            "",
                            ""
                        )
                    ),
                    1
                )
            ))
        mockServer.enqueue(response)
        val result = repository.addMembers(
            "fdgfdg",
            listOf(
                "fgdg"
            )
        )
        assertEquals(1, result.size)
    }

}