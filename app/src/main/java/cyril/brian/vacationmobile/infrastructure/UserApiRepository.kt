package cyril.brian.vacationmobile.infrastructure

import android.util.Log
import com.google.gson.JsonParseException
import cyril.brian.vacationmobile.domain.User
import cyril.brian.vacationmobile.infrastructure.api_methods.UserMethods
import cyril.brian.vacationmobile.infrastructure.api_methods.VacationsServices
import cyril.brian.vacationmobile.infrastructure.dto.InvitationsDTO
import cyril.brian.vacationmobile.infrastructure.dto.LoginDTO
import cyril.brian.vacationmobile.infrastructure.responses.UserLoginResponse
import cyril.brian.vacationmobile.infrastructure.responses.UserRegisterResponse
import cyril.brian.vacationmobile.infrastructure.responses.UserSearchResponse
import cyril.brian.vacationmobile.repositories.IUserRepository
import cyril.brian.vacationmobile.repositories.args.RegisterArgs
import cyril.brian.vacationmobile.repositories.exception.AuthException
import cyril.brian.vacationmobile.repositories.exception.StockageException
import cyril.brian.vacationmobile.repositories.exception.UserNotFoundException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException


class UserApiRepository(
    private val retrofit: Retrofit,
    private val vacationState: VacationState
): IUserRepository {
    private val userMethods : UserMethods by lazy {
        retrofit.create(UserMethods::class.java)
    }

    override fun login(login: String, password: String): UserLoginResponse {
        var result: UserLoginResponse? = null
        Log.d("LoginViewModel", "Password:$password")
        try {
            runBlocking {
                val response = async { userMethods.login(LoginDTO(login, password)) }.await()
                result = response.body()

                if(result == null) {
                    throw AuthException("No user found")
                }
                vacationState.user = result!!
                Log.d("UserApiRepository", "Response: ${response}")
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
        return result!!
    }

    override fun register(args: RegisterArgs): UserRegisterResponse {
        var result: UserRegisterResponse? = null

        runBlocking {
            try {
                val response = async {
                    userMethods.register(
                        args.name,
                        args.firstname,
                        args.login,
                        args.password,
                        args.email,
                        "dummy")
                }.await()

                result = response.body()
                if (result == null) {
                    val errorMessage = JSONObject(response.errorBody()!!.string()).getString("message")
                    throw AuthException(errorMessage)
                }
                else if(!response.isSuccessful) {
                    throw AuthException(result!!.message)
                }

                Log.d("UserApiRepository", "Response: ${response}")
            } catch (e: Exception) {
                Log.d("UserApiRepository", "Error Occurred: ${e.message}")
                throw AuthException("Error Occurred: ${e.message}")
            }
        }
        return result!!
    }

    override fun search(query: String): List<User> {
        val result: MutableList<User> = mutableListOf()

        runBlocking {
            try {
                val response: Response<UserSearchResponse> = async { userMethods.search(query, "Bearer ${vacationState.user.token}") }.await()
                val body = response.body()

                if(body == null || body.count == 0) {
                    throw UserNotFoundException("No user were found")
                }
                body.result.forEach { user ->
                    result += User(
                        (user.firstName ?: user.firstname) + " " + user.name,
                        user.id ?: user.uid,
                        user.email ?: user.mail!!
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

    override fun getInvitations(): List<InvitationsDTO> {
        val result: MutableList<InvitationsDTO> = mutableListOf()
        try {
            runBlocking {
                val response = async {
                    userMethods.getInvitations("Bearer ${vacationState.user.token}")
                }.await()
                val body = response.body()

                if (body == null) {
                    if(response.code() == 401 || response.code() == 403) {
                        throw AuthException("You are not authorized to perform this action")
                    } else if(response.code() == 400) {
                        throw AuthException("The given period is already taken")
                    } else {
                        throw StockageException("HTTP error: ${response.code()}")
                    }
                }
                result.addAll(body!!)
                Log.d("UserApiRepository", "Response: ${response}")
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
        return result.toList()
    }

    override fun acceptInvitations(vacationId: String, invitationId: String): Boolean {
        try {
            runBlocking {
                val vacationMethod = retrofit.create(VacationsServices::class.java)
                val response = async {
                    vacationMethod.acceptInvitation(
                        vacationId,
                        invitationId,
                        InvitationsDTO(
                            invitationId,
                            null,
                            vacationId,
                            true
                        ),
                        "Bearer ${vacationState.user.token}")
                }.await()
                val body = response.body()

                if (body == null) {
                    if(response.code() == 401 || response.code() == 403) {
                        throw AuthException("You are not authorized to perform this action")
                    } else if(response.code() == 400) {
                        throw AuthException("The given period is already taken")
                    } else {
                        throw StockageException("HTTP error: ${response.code()}")
                    }
                }
                Log.d("UserApiRepository", "Response: ${response}")
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
        return false
    }

}