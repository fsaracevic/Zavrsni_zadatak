package model

data class WeatherResults(
    val currentWeather: WeatherCard,
    val forecast: List<WeatherCard>
)
