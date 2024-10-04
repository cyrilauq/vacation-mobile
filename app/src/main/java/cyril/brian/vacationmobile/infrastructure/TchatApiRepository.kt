package cyril.brian.vacationmobile.infrastructure

import com.google.gson.JsonParseException
import cyril.brian.vacationmobile.domain.Message
import cyril.brian.vacationmobile.infrastructure.api_methods.TchatServices
import cyril.brian.vacationmobile.infrastructure.dto.MessageDTO
import cyril.brian.vacationmobile.repositories.ITchatRepository
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import cyril.brian.vacationmobile.repositories.exception.VacationNotFoundException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

class TchatApiRepository(
    val retrofit: Retrofit,
    val state: VacationState
): ITchatRepository {
    val tchatMethods by lazy {
        retrofit.create(TchatServices::class.java)
    }

    override fun getUserName(): String {
        return state.user.username
    }

    override suspend fun getMessages(vacationId: String): List<Message> {
        val result = mutableListOf<Message>()
        try {
            runBlocking {
                val response = async {
                    tchatMethods.getMessages(vacationId, "Bearer ${state.user.token}")
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
                    val date = it.sendDateTime!!
                    result += Message(
                        it.uid!!,
                        it.username!!,
                        it.message!!,
                        date.substring(0, date.indexOf("T")),
                        date.substring(date.indexOf("T") + 1, date.lastIndexOf(":")),
                        it.username != state.user.username
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
        return result
    }

    override suspend fun sendMessage(vacationId: String, message: String): Boolean {
        try {
            runBlocking {
                val response = async {
                    tchatMethods.sendMessages(vacationId, MessageDTO(
                        vacationId = vacationId,
                        content = message
                    ), "Bearer ${state.user.token}")
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
        return true
    }
}