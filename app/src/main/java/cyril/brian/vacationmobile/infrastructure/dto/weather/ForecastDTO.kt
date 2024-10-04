package cyril.brian.vacationmobile.infrastructure.dto.weather

data class ForecastDTO (
    var dt: Long,
    var main: TemperatureDTO,
    var weather: List<WeatherDTO>
)