package cyril.brian.vacationmobile.infrastructure.api_methods

import cyril.brian.vacationmobile.infrastructure.dto.PlannifyActivityDTO
import cyril.brian.vacationmobile.infrastructure.responses.ActivitiesResponse
import cyril.brian.vacationmobile.infrastructure.responses.ActivityResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VacationActivitiesMethods {

    /**
     * Add one or more activities to a vacation
     *
     * @param data      Object that contains all the information to add the activities to a vacation
     * @param token     Token of the user that make the request
     */
    @POST("VacationActivities")
    suspend fun addActivities(@Body data: ActivitiesResponse, @Header("Authorization") token: String): Response<ActivitiesResponse>

    /**
     * Api request to plannify an activity
     *
     * @param id        Id of the activity to plannify
     * @param data      Data to permeit the plannification of the activity
     * @param token     Token of the user that make the request
     *
     * @return      A response with the update activity
     */
    @PUT("VacationActivities/{id}/planning")
    suspend fun plannify(@Path("id") id: String, @Body data: PlannifyActivityDTO, @Header("Authorization") token: String): Response<ActivityResponse>

    /**
     * Get the activities that are linked to the given vacation
     *
     * @param id        Id of the vacation
     * @param token     Token of the user that make the request
     *
     * @return          An array that contains all the activity of the vacation.
     */
    @GET("vacation/{id}/activities")
    suspend fun getActivities(@Path("id") id: String ,@Header("Authorization") token: String): Response<Array<ActivityResponse>>

}