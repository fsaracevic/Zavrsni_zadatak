package model

data class WeatherCard(
    val condition: String,
    val iconUrl: String,
    val temperature: Double,
    val feelsLike: Double,
    val chanceOfRain: Double? = null
)
