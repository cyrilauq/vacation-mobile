package cyril.brian.vacationmobile.infrastructure

import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import cyril.brian.vacationmobile.domain.Coordinate
import cyril.brian.vacationmobile.domain.PeriodTime
import cyril.brian.vacationmobile.domain.User
import cyril.brian.vacationmobile.domain.Vacation
import cyril.brian.vacationmobile.domain.VacationActivity
import cyril.brian.vacationmobile.infrastructure.api_methods.VacationsServices
import cyril.brian.vacationmobile.infrastructure.dto.AddMembersDto
import cyril.brian.vacationmobile.infrastructure.dto.ErrorMessageDTO
import cyril.brian.vacationmobile.infrastructure.dto.activity.ActivityDTO
import cyril.brian.vacationmobile.infrastructure.dto.activity.AddActivityDTO
import cyril.brian.vacationmobile.infrastructure.responses.ActivityResponse
import cyril.brian.vacationmobile.infrastructure.responses.AddMembersResponse
import cyril.brian.vacationmobile.infrastructure.responses.GetVacationResultItemResponse
import cyril.brian.vacationmobile.infrastructure.responses.GetVacationUserResponse
import cyril.brian.vacationmobile.infrastructure.responses.VacationResponse
import cyril.brian.vacationmobile.repositories.IVacationRepository
import cyril.brian.vacationmobile.repositories.exception.AddVacationException
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import cyril.brian.vacationmobile.repositories.exception.UserNotFoundException
import cyril.brian.vacationmobile.repositories.exception.VacationNotFoundException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Double.parseDouble
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

class VacationApiRepository(
    private val retrofit: Retrofit,
    private val vacationState: VacationState
): IVacationRepository {
    private val vacationMethods : VacationsServices by lazy {
        retrofit.create(VacationsServices::class.java)
    }

    override fun getVacationsUser(token: String): GetVacationUserResponse {
        var result: Array<GetVacationResultItemResponse>? = null

        runBlocking {
            try {
                val response = async { vacationMethods.getVacationsForUser("Bearer ${vacationState.user.token}") }.await()
                result = response.body()
                if(result == null) {
                    throw Exception("No vacations")
                } else if(!response.isSuccessful) {
                    val errorMessage = JSONObject(response.errorBody()!!.string()).getString("message")
                    throw Exception(errorMessage)
                }
            } catch(e: Exception) {
                Log.d("VacationApiRepository", e.message!!)
                throw Exception(e.message)
            }
        }
        return GetVacationUserResponse(result!!.asList())
    }

    override fun newVacation(title: String, place: String, country: String, description: String, begin: PeriodTime?, end: PeriodTime?, coordinate: Coordinate): Vacation? {
        var result: Vacation? = null
        runBlocking {
            try {
                val toAdd = Vacation.newVacation(title, place, description, begin, end, coordinate)
                val response = async { vacationMethods.addVacation(
                    title,
                    place,
                    begin!!.date,
                    begin.time,
                    end!!.date,
                    end.time,
                    "${coordinate.lat}",
                    "${coordinate.lon}",
                    description,
                    country,
                    vacationState.user.uid,
                    "Bearer ${vacationState.user.token}") }.await()
                val body: VacationResponse? = response.body()
                if(body == null || !response.isSuccessful) {
                    val errorBody: ErrorMessageDTO = Gson().fromJson(response.errorBody()!!.string(), ErrorMessageDTO::class.java)
                    throw AddVacationException(errorBody.message)
                }
                result = Vacation.from(
                    body.id,
                    body.title,
                    body.description,
                    body.place,
                    PeriodTime(
                        body.timeBegin,
                        body.dateBegin
                    ),
                    PeriodTime(
                        body.timeEnd,
                        body.dateEnd
                    ),
                    Coordinate(
                        parseDouble(body.longitude),
                        parseDouble(body.latitude)
                    )
                )
                Log.d("VacationApiRepository", "Response: $response")
            } catch(e: AddVacationException) {
                throw e
            }
        }
        return result
    }

    override fun getVacationById(id: String): Vacation {
        var result: Vacation? = null
        runBlocking {
            try {
                val response = async { vacationMethods.getVacationById(
                    id,
                    "Bearer ${vacationState.user.token}") }.await()
                val body: VacationResponse? = response.body()
                if(response.code() == 401) {
                    throw AuthException("You're not authorized to see these.")
                }
                if(body == null) {
                    throw VacationNotFoundException("The wanted vacation wasn't found")
                }
                if(!response.isSuccessful) {
                    throw AddVacationException("Error while saving the vacation: ${response.errorBody()}")
                }

                result = Vacation.from(
                    body.id,
                    body.title,
                    body.description,
                    body.place,
                    PeriodTime(
                        body.timeBegin,
                        body.dateBegin
                    ),
                    PeriodTime(
                        body.timeEnd,
                        body.dateEnd
                    ),
                    Coordinate(
                        parseDouble(body.longitude),
                        parseDouble(body.latitude)
                    ),
                    body.isPublished
                )

                Log.d("UserApiRepository", "Response: $response")
            } catch(e: VacationNotFoundException) {
                throw e
            } catch (e: Exception) {
                Log.d("VacationApiRepository", "Error Occurred: ${e.message}")
                throw e
            }
        }
        return result!!
    }

    override fun addMembers(vacationId: String, members: List<String>): List<User> {
        var result: List<User> = listOf()
        runBlocking {
            try {
                val response = async {
                    vacationMethods.addMembers(
                        AddMembersDto(
                            vacationId,
                            members.toTypedArray()
                        ),
                        "Bearer ${vacationState.user.token}"
                    )
                }.await()
                val body: AddMembersResponse? = response.body()
                if(response.code() == 401) {
                    throw AuthException("You're not authorized to see these.")
                }
                if(body == null) {
                    throw VacationNotFoundException("The wanted vacation wasn't found")
                }
                if(!response.isSuccessful) {
                    throw AddVacationException("Error while saving the vacation: ${response.errorBody()}")
                }

                Log.d("VacationApiRepository", "Response: $response")

                result = body.members.map { user ->
                    User(
                        user.name + " " + user.firstName,
                        user.id ?: user.uid,
                        user.email ?: user.mail!!
                    )
                }
            } catch(e: VacationNotFoundException) {
                throw e
            } catch (e: Exception) {
                Log.d("VacationApiRepository", "Error Occurred: ${e.message}")
                throw e
            }
        }
        return result
    }

    override fun setCurrentVacation(vacationId: String) {
        vacationState.currentVacationId = vacationId
    }

    override fun getCurrentVacation(): String {
        return vacationState.currentVacationId
    }

    override fun addActivities(
        vacationId: String,
        activities: Array<VacationActivity>
    ): Iterable<VacationActivity> {
        val result: MutableList<VacationActivity> = mutableListOf()
        runBlocking {
            try {
                val response: Response<Array<ActivityResponse>> = async {
                    vacationMethods.addActivities(
                        AddActivityDTO(
                            listOf<ActivityDTO>(),
                            vacationId
                        ),
                        "Bearer ${vacationState.user.token}"
                    )
                }.await()
                val body = response.body()

                if(response.code() == 401) {
                    throw AuthException("User can't perform this action.")
                }

                if(body.isNullOrEmpty()) {
                    throw UserNotFoundException("No user were found")
                }
                body.forEach { activity ->
                    result += VacationActivity(
                        activity.activityId,
                        activity.title,
                        activity.description,
                        activity.latitude,
                        activity.longitude,
                        activity.place
                    )
                }
            } catch (e: IOException) {
                throw StockageException("The connection with the server is not possible")
            } catch (e: SocketTimeoutException) {
                throw StockageException("No response found")
            } catch (e: SSLHandshakeException) {
                throw StockageException("Maybe you forgot to open your vpn")
            } catch (e: JsonParseException) {
                throw StockageException("Error in the received data")
            }
        }
        return result
    }

    override fun getMembers(vacationId: String): List<User> {
        val result: MutableList<User> = mutableListOf()
        try {
            runBlocking {
                val response = async {
                    vacationMethods.getMembers(vacationId, "Bearer ${vacationState.user.token}")
                }.await()

                val body = response.body()

                if (body == null) {
                    if (response.code() == 401) {
                        throw AuthException("You must be logged in to perform this.")
                    } else if (response.code() == 400) {
                        throw VacationNotFoundException("Wrong vacation ID given to API")
                    } else {
                        throw Exception("Unknown internal server error")
                    }
                }

                body.forEach {
                    result += User(it.firstname + " " + it.name, it.id ?: it.uid, it.email ?: it.mail!!)
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

        override fun downloadPlanning(vacationId: String): String {
            var filePath = ""
            runBlocking {
                val response = async { vacationMethods.downloadPlanning(vacationId, "Bearer ${vacationState.user.token}") }.await()

                val body = response.body()
                if(body == null) {
                    Log.d("VacationApiRepository", "no file found")
                }
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "planning.ics"
                )
                filePath = file.absolutePath

                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null
                try {
                    val fileReader= ByteArray(4096)

                    val fileSize = body!!.contentLength()
                    var fileSizeDownloaded = 0L

                    inputStream = body.byteStream()
                    outputStream = FileOutputStream(file)

                    while(true) {
                        val read = inputStream.read(fileReader)

                        if(read == -1) {
                            break
                        }

                        outputStream.write(fileReader, 0, read)

                        fileSizeDownloaded += read

                        Log.d("VacationApiRepository", "file download: $fileSizeDownloaded of $fileSize")
                    }

                    outputStream.flush()
                } catch(e: IOException) {
                    Log.d("VacationApiRepository", e.message!!)
                } finally {
                    inputStream?.close()
                    outputStream?.close()
                }
            }
            return filePath
        }
    }