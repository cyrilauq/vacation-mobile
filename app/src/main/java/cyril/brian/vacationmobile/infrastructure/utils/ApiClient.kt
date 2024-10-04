package cyril.brian.vacationmobile.infrastructure.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cyril.brian.vacationmobile.infrastructure.api_methods.UserMethods
import cyril.brian.vacationmobile.infrastructure.api_methods.VacationsServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://porthos-intra.cg.helmo.be/e200106/"

    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build()
    }

    val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    val userMethods : UserMethods by lazy{
        retrofit.create(UserMethods::class.java)
    }

    val vacationMethods : VacationsServices by lazy {
        retrofit.create(VacationsServices::class.java)
    }

}