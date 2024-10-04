package cyril.brian.vacationmobile.infrastructure

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cyril.brian.vacationmobile.domain.VacationActivity
import cyril.brian.vacationmobile.infrastructure.responses.ActivitiesResponse
import cyril.brian.vacationmobile.infrastructure.responses.ActivityResponse
import cyril.brian.vacationmobile.infrastructure.responses.UserLoginResponse
import cyril.brian.vacationmobile.repositories.IVacationActivitiesRepository
import cyril.brian.vacationmobile.repositories.args.PlannifyArgs
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class VacationActivitiesRepositoryTest {
    private lateinit var mockServer: MockWebServer
    private lateinit var repository: IVacationActivitiesRepository

    val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val vacationState = VacationState()

    @Before
    fun setUp() {
        try {
            vacationState.user = UserLoginResponse(
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            )
            mockServer = MockWebServer()
            mockServer.start()
            val apiService = Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            repository = VacationActivitiesRepository(apiService, vacationState)
        } catch (e: Exception) {
            throw e
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getVacationActivities() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(
                ActivitiesResponse(
                    "vacation_id",
                    arrayOf(
                        ActivityResponse(
                            null,
                            "rekjgh fd gf",
                            "fd ghdfkj g",
                            3.0,
                            3.0,
                            "flmkjg ldfkjgofdmlj b"
                        )
                    )
                )
            ))
        mockServer.enqueue(response)
        val result = repository.addActivitiesToVacation(
            "vacation_id",
            arrayOf(
                VacationActivity(
                    null,
                    "rekjgh fd gf",
                    "fd ghdfkj g",
                    3.0,
                    3.0,
                    "flmkjg ldfkjgofdmlj b"
                )
            )
        )
        assertEquals(1, result.size)
    }

    @Test
    fun addActivitiesToVacation() {
    }

    @Test
    fun whenPlannifyActivityGoWellThenReturnActivity() {
        val id = "123456789"
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(
                ActivityResponse(
                    id,
                    "",
                    "",
                    0.0,
                    0.0,
                    ""
                )
            ))
        mockServer.enqueue(response)
        val result = repository.plannifyActivity(PlannifyArgs(
            "",
            "",
            "",
            "",
            ""
        ))!!
        assertEquals(id, result.activityId)
    }

    @Test(expected = AuthException::class)
    fun whenUserIsNotAuthorizeToPlannifyActivityThenThrowException() {
        val response = MockResponse()
            .setResponseCode(403)
        mockServer.enqueue(response)
        repository.plannifyActivity(PlannifyArgs(
            "",
            "",
            "",
            "",
            ""
        ))
    }

    @Test(expected = StockageException::class)
    fun whenSomethingGoesWrongThenThrowException() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
        mockServer.enqueue(response)
        repository.plannifyActivity(PlannifyArgs(
            "",
            "",
            "",
            "",
            ""
        ))
    }
}