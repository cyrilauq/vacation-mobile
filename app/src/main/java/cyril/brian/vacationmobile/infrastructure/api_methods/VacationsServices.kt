package cyril.brian.vacationmobile.infrastructure.api_methods

import cyril.brian.vacationmobile.infrastructure.dto.AddMembersDto
import cyril.brian.vacationmobile.infrastructure.dto.InvitationsDTO
import cyril.brian.vacationmobile.infrastructure.dto.UserDTO
import cyril.brian.vacationmobile.infrastructure.dto.VacationDTO
import cyril.brian.vacationmobile.infrastructure.dto.activity.AddActivityDTO
import cyril.brian.vacationmobile.infrastructure.responses.ActivityResponse
import cyril.brian.vacationmobile.infrastructure.responses.AddMembersResponse
import cyril.brian.vacationmobile.infrastructure.responses.GetVacationResultItemResponse
import cyril.brian.vacationmobile.infrastructure.responses.VacationResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Streaming

/**
 * Interface qui déclare toutes les méthodes qui nous permettront de dialoguer avec l'api.
 *
 * Les objets donnés en argument ou récupérés à la fin de la requête, doivent respecter le format de ce qui est attendu/renvoié par l'api.
 */
interface VacationsServices {

    @GET("Vacations/User")
    suspend fun getVacationsForUser(@Header("Authorization") token: String): Response<Array<GetVacationResultItemResponse>>

    @POST("Vacations")
    suspend fun addVacation(@Body vacation: VacationDTO, @Header("Authorization") token: String): Response<VacationResponse>

    @FormUrlEncoded
    @POST("Vacations")
    suspend fun addVacation(
        @Field("title") title: String,
        @Field("place") place: String,
        @Field("dateBegin") dateBegin: String,
        @Field("hourBegin") hourBegin: String,
        @Field("dateEnd") dateEnd: String,
        @Field("hourEnd") hourEnd: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("description") description: String,
        @Field("country") country: String,
        @Field("userId") userId: String,
        @Header("Authorization") token: String): Response<VacationResponse>

    @GET("Vacations/{id}")
    suspend fun getVacationById(@Path("id") id: String, @Header("Authorization") token: String): Response<VacationResponse>

    @POST("vacations/members")
    suspend fun addMembers(@Body body: AddMembersDto, @Header("Authorization") token: String): Response<AddMembersResponse>

    @POST("vacations/activities")
    suspend fun addActivities(@Body body: AddActivityDTO, @Header("Authorization") token: String): Response<Array<ActivityResponse>>

    @GET("Vacations/{vacationId}/members")
    suspend fun getMembers(@Path("vacationId") id: String, @Header("Authorization") token: String): Response<Array<UserDTO>>

    @GET("vacation/{id}/planning/ics")
    @Streaming
    suspend fun downloadPlanning(@Path("id") id: String, @Header("Authorization") token: String): Response<ResponseBody>

    @PUT("vacations/{vacationId}/invitation/{invitationId}")
    suspend fun acceptInvitation(@Path("vacationId") vacationId: String, @Path("invitationId") invitationId: String, @Body body: InvitationsDTO, @Header("Authorization") token: String): Response<ResponseBody>
}