package cyril.brian.vacationmobile.domain

data class Forecast(
    var timestamp: Long,
    var temp: Double,
    var weather: String,
    var description: String,
    var icon: String
)