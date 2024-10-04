package cyril.brian.vacationmobile.repositories

import cyril.brian.vacationmobile.domain.Forecast

interface IOpenWeatherRepository {
    companion object {
        private var _repository: IOpenWeatherRepository? = null

        fun get() = _repository

        fun initialize(repository: IOpenWeatherRepository) {
            _repository = repository
        }
    }

    fun getForecast(lat: Double, lon: Double): List<Forecast>
}