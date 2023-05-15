package util

import dao.ForecastDay
import dao.Hour
import dao.WeatherResponse
import model.WeatherCard
import model.WeatherResults

class WeatherTransformer {

    private fun extractCurrentWeatherFrom(response: WeatherResponse): WeatherCard {
        return WeatherCard(
            condition = response.current.condition.text,
            iconUrl = "https:" + response.current.condition.icon.replace("64x64", "128x128"),
            feelsLike = response.current.feelsLikeC,
            temperature = response.current.tempC
        )
    }

    private fun extractForecastWeatherFrom(response: WeatherResponse): List<WeatherCard>{
        return response.forecast.forecastDays.map {forecastDay ->
            WeatherCard(
                condition = forecastDay.day.condition.text,
                iconUrl = "https:" + forecastDay.day.condition.icon.replace("64x64", "128x128"),
                feelsLike = avgFeelsLike(forecastDay),
                temperature = forecastDay.day.averageTempC,
                chanceOfRain = avgChanceOfRain(forecastDay)
            )
        }
    }

    private fun avgChanceOfRain(forecastDay: ForecastDay): Double? =
        forecastDay.hour.map(Hour::chanceOfRain).average()

    private fun avgFeelsLike(forecastDay: ForecastDay): Double =
        forecastDay.hour.map(Hour::feelsLikeC).average()

    fun transform(response: WeatherResponse): WeatherResults {
        val current = extractCurrentWeatherFrom(response)
        val forecast = extractForecastWeatherFrom(response)

        return WeatherResults(
            currentWeather = current,
            forecast = forecast
        )
    }

}
