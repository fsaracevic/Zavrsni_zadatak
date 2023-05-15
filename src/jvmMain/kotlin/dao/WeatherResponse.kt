package dao

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val current: Current,
    val forecast: Forecast
)
