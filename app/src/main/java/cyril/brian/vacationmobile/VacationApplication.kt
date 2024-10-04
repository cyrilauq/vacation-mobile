package cyril.brian.vacationmobile

import android.app.Application
import cyril.brian.vacationmobile.infrastructure.GeocodingRepository
import cyril.brian.vacationmobile.infrastructure.OpenWeatherRepository
import cyril.brian.vacationmobile.infrastructure.TchatApiRepository
import cyril.brian.vacationmobile.infrastructure.UserApiRepository
import cyril.brian.vacationmobile.infrastructure.VacationActivitiesRepository
import cyril.brian.vacationmobile.infrastructure.VacationApiRepository
import cyril.brian.vacationmobile.infrastructure.VacationState
import cyril.brian.vacationmobile.infrastructure.utils.ApiClient
import cyril.brian.vacationmobile.infrastructure.utils.GeocodingHelper
import cyril.brian.vacationmobile.infrastructure.utils.OpenWeatherHelper
import cyril.brian.vacationmobile.repositories.IGeocodingRepository
import cyril.brian.vacationmobile.repositories.IOpenWeatherRepository
import cyril.brian.vacationmobile.repositories.ITchatRepository
import cyril.brian.vacationmobile.repositories.IUserRepository
import cyril.brian.vacationmobile.repositories.IVacationActivitiesRepository
import cyril.brian.vacationmobile.repositories.IVacationRepository

class VacationApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val vacationState = VacationState()
        IVacationRepository.initialize(VacationApiRepository(ApiClient.retrofit, vacationState))
        IUserRepository.initialize(UserApiRepository(ApiClient.retrofit, vacationState))
        IGeocodingRepository.initialize(GeocodingRepository(GeocodingHelper.geocodingHelper))
        IOpenWeatherRepository.initialize(OpenWeatherRepository(OpenWeatherHelper.openWeatherHelper))
        IVacationActivitiesRepository.initialize(VacationActivitiesRepository(ApiClient.retrofit, vacationState))
        ITchatRepository.initialize(TchatApiRepository(ApiClient.retrofit, vacationState))
    }
}
