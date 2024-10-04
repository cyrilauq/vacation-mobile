package cyril.brian.vacationmobile.infrastructure.api_methods

import cyril.brian.vacationmobile.infrastructure.dto.MessageDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface TchatServices {

    /**
     * Will let us make the request to the api and retrieve the message for the given vacation.
     *
     * @param vacationId    Id of the vacation
     * @param token         User's token
     *
     * @return              A response that will contains all the message that are related to the given vacation.
     */
    @GET("tchat/vacation/{id}/message")
    suspend fun getMessages(@Path("id") vacationId: String, @Header("Authorization") token: String): Response<List<MessageDTO>>


    /**
     * Will let us make the request to the api to send a message inside the tchat of the given vacation.
     *
     * @param vacationId    Id of the vacation
     * @param message    message to send in the tchat
     * @param token         User's token
     *
     * @return              A response that
     */
    @POST("tchat/vacation/{id}/message")
    suspend fun sendMessages(@Path("id") vacationId: String, @Body message: MessageDTO, @Header("Authorization") token: String): Response<ResponseBody> // Use of ResponseBody cause no body is needed for this

}