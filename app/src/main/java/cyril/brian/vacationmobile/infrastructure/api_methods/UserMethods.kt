package cyril.brian.vacationmobile.infrastructure.api_methods

import cyril.brian.vacationmobile.infrastructure.dto.InvitationsDTO
import cyril.brian.vacationmobile.infrastructure.dto.LoginDTO
import cyril.brian.vacationmobile.infrastructure.responses.UserLoginResponse
import cyril.brian.vacationmobile.infrastructure.responses.UserRegisterResponse
import cyril.brian.vacationmobile.infrastructure.responses.UserSearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserMethods {

    @POST("Auth/login")
    suspend fun login(@Body data: LoginDTO): Response<UserLoginResponse>

    @FormUrlEncoded
    @POST("Auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("firstname") firstname: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("mail") mail: String,
        @Field("picturepath") picturepath: String): Response<UserRegisterResponse>

    @GET("Users/list/{query}")
    suspend fun search(@Path("query") query: String, @Header("Authorization") token: String): Response<UserSearchResponse>

    @GET("users/invitations")
    suspend fun getInvitations(@Header("Authorization") token: String): Response<List<InvitationsDTO>>
}