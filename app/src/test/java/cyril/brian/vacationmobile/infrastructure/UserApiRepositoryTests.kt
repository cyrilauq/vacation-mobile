package cyril.brian.vacationmobile.infrastructure

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cyril.brian.vacationmobile.domain.User
import cyril.brian.vacationmobile.infrastructure.responses.UserLoginResponse
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.UserNotFoundException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class UserApiRepositoryTests {
    private lateinit var mockServer: MockWebServer
    private lateinit var repository: UserApiRepository

    val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val vacationState = VacationState()

    @Before
    fun setUp() {
        try {
            mockServer = MockWebServer()
            mockServer.start()
            val apiService = Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            repository = UserApiRepository(apiService, vacationState)
        } catch (e: Exception) {
            throw e
        }
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun whenCredentialsAreCorrectThenReturnUserInformations() {
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
        val userInfo = repository.login("touka_ki", "Password123@")
        assertNotNull(userInfo)
    }

    @Test(expected = AuthException::class)
    fun whenNoUserFoundThenThrowsException() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
        mockServer.enqueue(response)
        repository.login("touka_ki", "Password123@")
    }

    @Test
    fun whenSearchUserWithUserThatMatchThenGetUsers() {
        val response1 = MockResponse()
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
        mockServer.enqueue(response1)
        repository.login("touka_ki", "Password123@")
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "   count: 2," +
                    "   result: [" +
                    "       {\n" +
                    "           \"email\": \"touka_ki@example.com\",\n" +
                    "           \"name\": \"Kirishima3\",\n" +
                    "           \"firstname\": \"Touka3\",\n" +
                    "           \"uid\": \"4d8f8b27-f8bc-438e-be71-b7b67f9eeb27\"\n" +
                    "       }," +
                    "       {\n" +
                    "           \"uid\": \"4d8f8b27-f8bc-438e-be71-b7b67f9eeb27\",\n" +
                    "           \"name\": \"Kirishima2\",\n" +
                    "           \"email\": \"touka_ki@example.com\",\n" +
                    "           \"firstname\": \"Touka2\"\n" +
                    "       }" +
                    "   ]" +
                    "}")
        mockServer.enqueue(response)
        val result: List<User> = repository.search("t")
        assertTrue(result.isNotEmpty())
    }

    @Test(expected = UserNotFoundException::class)
    fun whenSearchUserWithNoUserThatMatchThenThrowException() {
        val response1 = MockResponse()
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
        mockServer.enqueue(response1)
        repository.login("touka_ki", "Password123@")
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "   \"count\": 0," +
                    "   \"result\": []" +
                    "}")
        mockServer.enqueue(response)
        repository.search("t")
    }

}