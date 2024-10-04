package cyril.brian.vacationmobile.infrastructure

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import cyril.brian.vacationmobile.domain.VacationActivity
import cyril.brian.vacationmobile.infrastructure.api_methods.VacationActivitiesMethods
import cyril.brian.vacationmobile.infrastructure.dto.ErrorMessageDTO
import cyril.brian.vacationmobile.infrastructure.dto.PlannifyActivityDTO
import cyril.brian.vacationmobile.infrastructure.responses.ActivitiesResponse
import cyril.brian.vacationmobile.infrastructure.responses.ActivityResponse
import cyril.brian.vacationmobile.repositories.IVacationActivitiesRepository
import cyril.brian.vacationmobile.repositories.args.PlannifyArgs
import cyril.brian.vacationmobile.repositories.exception.ActivityFormatException
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

class VacationActivitiesRepository(
    val retrofit: Retrofit,
    private val vacationState: VacationState
): IVacationActivitiesRepository {
    private val vacationActivities by lazy {
        retrofit.create(VacationActivitiesMethods::class.java)
    }

    override fun addActivitiesToVacation(vacationId: String, activities: Array<VacationActivity>): List<VacationActivity> {
        val result: MutableList<VacationActivity> = mutableListOf()
        try {
            runBlocking {
                val response = async {
                    vacationActivities.addActivities(
                        ActivitiesResponse(
                            vacationState.currentVacationId,
                            activities.map {
                                ActivityResponse(
                                    null,
                                    it.title,
                                    it.description,
                                    it.longitude,
                                    it.latitude,
                                    it.place
                                )
                            }.toTypedArray()
                        ),
                        "Bearer ${vacationState.user.token}"
                    )
                }.await()

                val body = response.body()

                if(body == null) {
                    if(response.code() == 401) {
                        throw AuthException("You must be logged in to perform this.")
                    }
                    val errorBody: ErrorMessageDTO = Gson().fromJson(response.errorBody()!!.string(), ErrorMessageDTO::class.java)
                    throw StockageException(errorBody.message)
                }

                body.activities.forEach {
                    result += VacationActivity(
                        it.activityId,
                        it.title,
                        it.description,
                        it.longitude,
                        it.latitude,
                        it.place
                    )
                }

                Log.d("VacationActivitiesRepository", "Response: ${response}")
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
        return result.toList()
    }

    override fun getActivities(vacationId: String): List<VacationActivity> {
        val result: MutableList<VacationActivity> = mutableListOf()
        try {
            runBlocking {
                val response = async {
                    vacationActivities.getActivities(vacationState.currentVacationId, "Bearer ${vacationState.user.token}")
                }.await()

                val body = response.body()

                if(body == null) {
                    if(response.code() == 401) {
                        throw AuthException("You must be logged in to perform this.")
                    } else if (response.code() == 400) {
                        throw ActivityFormatException("Wrong vacation ID given to API")
                    } else {
                        throw StockageException("Unknown internal server error")
                    }
                }

                body.forEach {
                    result += VacationActivity(
                        it.activityId,
                        it.title,
                        it.description,
                        it.longitude,
                        it.latitude,
                        it.place
                    )
                }
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

        return result.toList()
    }

    override fun plannifyActivity(args: PlannifyArgs): VacationActivity? {
        var result: VacationActivity? = null
        try {
            runBlocking {
                val response = async {
                    vacationActivities.plannify(
                        args.activityId,
                        PlannifyActivityDTO(
                            args.dateBegin + " " + args.timeBegin,
                            args.dateEnd + " " + args.timeEnd
                        ),
                        "Bearer ${vacationState.user.token}"
                    )
                }.await()

                Log.d("VacationActivitiesRepository", "$response")

                val body: ActivityResponse? = response.body()

                if(body == null) {
                    if(response.code() == 401 || response.code() == 403) {
                        throw AuthException("You are not authorized to perform this action")
                    } else if(response.code() == 400) {
                        throw StockageException("The given period is already taken")
                    } else {
                        throw StockageException("HTTP error: ${response.code()}")
                    }
                }
                result = VacationActivity(
                    body.activityId,
                    body.title,
                    body.description,
                    body.longitude,
                    body.latitude,
                    body.place
                )
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